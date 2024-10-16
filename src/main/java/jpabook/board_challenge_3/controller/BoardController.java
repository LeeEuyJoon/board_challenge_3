package jpabook.board_challenge_3.controller;

import jakarta.servlet.http.HttpSession;
import jpabook.board_challenge_3.domain.Comment;
import jpabook.board_challenge_3.domain.PageHandler;
import jpabook.board_challenge_3.domain.Post;
import jpabook.board_challenge_3.domain.User;
import jpabook.board_challenge_3.service.CommentService;
import jpabook.board_challenge_3.service.PostService;
import jpabook.board_challenge_3.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;

    @GetMapping("/list")
    public String list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                       Model model, HttpSession session) {

        // 페이지 핸들러에 필요한 페이지 정보 지정
        Integer totalCount = postService.countAll();
        PageHandler ph = new PageHandler(totalCount, page, pageSize);
        model.addAttribute("ph", ph);

        List<Post> posts = postService.getPostsPage(page, pageSize);
        model.addAttribute("posts", posts);

        // 세션에서 사용자 정보 추가 (닉네임 표시용)
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser != null) {
            session.setAttribute("nickname", loginUser.getNickname());
        }

        return "list";
    }

    @GetMapping("/post")
    public String post(@RequestParam("postId") Long postId,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                       Model model, HttpSession session) {

        // postId 유효성 검사
        if (postId == null) {
            // 예외 처리 혹은 에러 페이지로 리다이렉트
            model.addAttribute("error", "Invalid Post ID");
            return "error123";
        }

        // 조회수 증가
        postService.plusViewCnt(postId);

        // 게시물 정보 조회 및 모델에 추가
        Post findPost = postService.find(postId);
        if (findPost == null) {
            model.addAttribute("error", "Post not found");
            return "error123";
        }
        model.addAttribute("post", findPost);

        // 댓글 목록 모델에 추가
        List<Comment> comments = commentService.findByPostId(postId);
        model.addAttribute("comments", comments);

        // 페이지 정보 모델에 추가
        model.addAttribute("page", page);
        model.addAttribute("pageSize", pageSize);

        // 세션에서 사용자 정보 추가 (닉네임 표시용)
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser != null) {
            session.setAttribute("nickname", loginUser.getNickname());
        }

        return "post";
    }

    @GetMapping("/addPost")
    public String addPost(HttpSession session, Model model) {
        // 세션에서 로그인한 유저 정보를 가져옴
        User loginUser = (User) session.getAttribute("loginUser");

        // 만약 유저가 로그인되지 않았을 경우 처리 (예: 로그인 페이지로 리다이렉트)
        if (loginUser == null) {
            return "redirect:/user/loginForm";
        }

        // 로그인한 유저의 ID를 모델에 추가
        model.addAttribute("loginUserId", loginUser.getId());

        return "addPost";
    }

    @PostMapping("/addPost")
    public String addPost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("userId") String userId,
            RedirectAttributes rattr
    ) {
        try {
            User user = userService.find(userId);

            Post post = new Post(user, title, content);

            postService.save(post);

            rattr.addFlashAttribute("msg","글 등록에 성공했습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            rattr.addFlashAttribute("msg", "글 등록에 실패했습니다.");
            return "redirect:/board/list";
        }

        return "redirect:/board/list";
    }

    @PostMapping("/deletePost")
    public String deletePost(
            @RequestParam("postId") Long postId,
            RedirectAttributes rattr,
            HttpSession session
    ) {

        try {
            Post post = postService.find(postId);
            User loginUser = (User) session.getAttribute("loginUser");

            if (post.getWriter().getId() != loginUser.getId()) {
                throw new IllegalStateException("글 작성자만 삭제할 수 있습니다.");
            }

            postService.delete(postId);
            rattr.addFlashAttribute("msg", "게시글이 삭제되었습니다.");
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return "redirect:/board/list";
    }

    @PostMapping("/editPost")
    public String editPost(
            @RequestParam("postId") Long postId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            RedirectAttributes rattr,
            HttpSession session
    ) {
        try {
            // 수정할 게시글을 찾기
            Post post = postService.find(postId);
            User loginUser = (User) session.getAttribute("loginUser");

            // 작성자 확인
            if (!post.getWriter().getId().equals(loginUser.getId())) {
                throw new IllegalStateException("글 작성자만 수정할 수 있습니다.");
            }

            // 게시글 수정
            post.updatePost(title, content);  // 엔티티 내부 메서드를 통해 수정

            postService.update(post);  // 수정된 게시글 저장

            rattr.addFlashAttribute("msg", "게시글 수정이 완료되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            rattr.addFlashAttribute("msg", "게시글 수정에 실패했습니다.");
        }
        return "redirect:/board/post?postId=" + postId;
    }
}

