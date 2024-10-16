package jpabook.board_challenge_3.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jpabook.board_challenge_3.domain.Address;
import jpabook.board_challenge_3.domain.Comment;
import jpabook.board_challenge_3.domain.User;
import jpabook.board_challenge_3.repository.dto.CommentDto;
import jpabook.board_challenge_3.repository.dto.PostDto;
import jpabook.board_challenge_3.service.PostService;
import jpabook.board_challenge_3.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final PostService postService;

    @GetMapping("/loginForm")
    public String loginForm() {

        return "loginForm";
    }

    @GetMapping("/signup")
    public String signup() {

        return "signup";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam("id") String id,
            @RequestParam("pwd") String pwd,
            HttpServletRequest request,
            RedirectAttributes rattr
    ) {
        try {
            if (!loginCheck(id, pwd)) {
                rattr.addFlashAttribute("errorMessage", "ID 또는 비밀번호가 일치하지 않습니다.");
                return "redirect:/loginForm";
            }

            // 로그인에 성공한 경우, User 객체를 가져와 세션에 저장합니다.
            User loginUser = userService.find(id); // id로 User 객체를 조회하는 메서드 필요
            HttpSession session = request.getSession();
            session.setAttribute("loginUser", loginUser);

            return "redirect:/board/list";
        } catch (Exception e) {
            logger.error("로그인 중 오류 발생: ", e);
            e.printStackTrace();
            return "redirect:/loginForm";
        }
    }

    @PostMapping("/signup")
    public String signup(
            @RequestParam("id") String id,
            @RequestParam("pwd") String pwd,
            @RequestParam("nickname") String nickname,
            @RequestParam("city") String city,
            @RequestParam("street") String street,
            @RequestParam("zipcode") String zipcode,
            RedirectAttributes rattr
    ) {
        try {
            Address address = new Address(city, street, zipcode);
            User user = new User(id, pwd, nickname, address);
            userService.join(user);
            rattr.addFlashAttribute("회원 가입이 완료되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            rattr.addFlashAttribute("회원 가입에 실패했습니다.");
            return "error123";
        }
        return "redirect:/user/loginForm";
    }

    private boolean loginCheck(String id, String pwd) {
        User chkUser = userService.find(id);

        if (chkUser == null) {
            return false;
        }

        if (!chkUser.getPwd().equals(pwd)) {
            return false;  // 비밀번호가 일치하지 않음
        }
        return true;
    }

    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes rattr) {

        session.invalidate();

        rattr.addFlashAttribute("msg", "로그아웃 되었습니다.");

        return "redirect:/board/list";
    }

    @GetMapping("/myPage")
    public String myPage(
            Model model,
            HttpSession session,
            RedirectAttributes rattr
    ) {

        try {
            // 유저 id로 게시물 가져와야되고
            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser == null) {
                rattr.addFlashAttribute("msg", "로그인을 먼저 해주세요.");
                return "redirect:/loginForm";
            }

            String userId = loginUser.getId();

            List<PostDto> posts = userService.postsPerUser(userId);
            model.addAttribute("posts", posts);


            // 유저 id로 댓글 가져와야된다
            List<CommentDto> comments = userService.commentsPerUser(userId);
            model.addAttribute("comments", comments);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return "myPage";
    }

}
