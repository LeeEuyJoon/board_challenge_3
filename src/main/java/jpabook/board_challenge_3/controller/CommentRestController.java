package jpabook.board_challenge_3.controller;

import jakarta.servlet.http.HttpSession;
import jpabook.board_challenge_3.domain.Comment;
import jpabook.board_challenge_3.domain.Post;
import jpabook.board_challenge_3.domain.User;
import jpabook.board_challenge_3.service.CommentService;
import jpabook.board_challenge_3.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentRestController {

    private final CommentService commentService;
    private final PostService postService;

    @PostMapping("/add")
    public ResponseEntity<Comment> add(
            @RequestParam String content,
            @RequestParam Long postId,
            HttpSession session
    ) {
        Post post = postService.find(postId);
        System.out.println("============== post = " + post);
        User loginUser = (User) session.getAttribute("loginUser");

        Comment comment = new Comment(loginUser, post, content);
        System.out.println("================ comment = " + comment);

        post.addComment(comment);
        commentService.save(comment);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(
            @RequestParam Long postId,
            @RequestParam Long commentId,
            HttpSession session
    ) {
        // 세션에서 로그인한 사용자 정보 확인
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        // Post 및 Comment 존재 여부 확인
        Post post = postService.find(postId);
        Comment comment = commentService.findById(commentId);

        if (post == null || comment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post 또는 Comment가 존재하지 않습니다.");
        }

        // 댓글 작성자와 로그인한 사용자가 같은지 확인
        if (!comment.getUser().getId().equals(loginUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("댓글 삭제 권한이 없습니다.");
        }

        // 댓글 삭제
        commentService.delete(comment.getId());
        return ResponseEntity.ok("댓글이 성공적으로 삭제되었습니다.");
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(
            @RequestParam Long commentId,
            @RequestParam String content,
            HttpSession session
    ) {

        User loginUser = (User) session.getAttribute("loginUser");
        Comment comment = commentService.findById(commentId);

        // 댓글이 존재해야 삭제 가능
        if (comment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("댓글이 존재하지 않습니다.");
        }

        // 댓글 작성자와 로그인한 유저 id 비교
        if (!comment.getUser().getId().equals(loginUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("댓글 삭제 권한이 없습니다.");
        }

        comment.updateComment(content);
        commentService.update(comment);
        return ResponseEntity.ok(comment);
    }

}
