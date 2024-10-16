package jpabook.board_challenge_3.service;


import jpabook.board_challenge_3.domain.Comment;
import jpabook.board_challenge_3.domain.Post;
import jpabook.board_challenge_3.domain.User;
import jpabook.board_challenge_3.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Transactional
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private EntityManager em;

    private Post testPost;
    private User testUser;

    @BeforeEach
    void setUp() {
        // Set up a user and a post for testing
        testUser = new User("testUser", "password", "nickname", null);
        em.persist(testUser);

        testPost = new Post(testUser, "Test Post Title", "Test Post Content");
        em.persist(testPost);
    }

    @Test
    void saveComment() {
        // given
        Comment comment = new Comment(testUser, testPost, "Test Comment");

        // when
        commentService.save(comment);

        // then
        assertNotNull(comment.getId());
        Comment foundComment = em.find(Comment.class, comment.getId());
        assertNotNull(foundComment);
        assertThat(foundComment.getContent()).isEqualTo("Test Comment");
    }

    @Test
    void findCommentById() {
        // given
        Comment comment = new Comment(testUser, testPost, "Test Comment");
        commentService.save(comment);

        // when
        Comment foundComment = commentService.findById(comment.getId());

        // then
        assertNotNull(foundComment);
        assertThat(foundComment.getContent()).isEqualTo("Test Comment");
    }

    @Test
    void findCommentsByPostId() {
        // given
        Comment comment1 = new Comment(testUser, testPost, "First Comment");
        Comment comment2 = new Comment(testUser, testPost, "Second Comment");
        commentService.save(comment1);
        commentService.save(comment2);

        // when
        List<Comment> comments = commentService.findByPostId(testPost.getId());

        // then
        assertThat(comments).hasSize(2);
        assertThat(comments).extracting("content").containsExactlyInAnyOrder("First Comment", "Second Comment");
    }

    @Test
    void updateComment() {
        // given
        Comment comment = new Comment(testUser, testPost, "Original Comment");
        commentService.save(comment);

        // when
        Comment updateComment = new Comment(comment.getId(), testUser, testPost, "Updated Comment");
        commentService.update(updateComment);
        em.flush();
        em.clear(); // 영속성 컨텍스트를 비워서 DB에서 새로 가져오도록 강제

        Comment updatedComment = em.find(Comment.class, updateComment.getId());

        // then
        assertThat(updatedComment.getContent()).isEqualTo("Updated Comment");
    }

    @Test
    void deleteComment() {
        // given
        Comment comment = new Comment(testUser, testPost, "Comment to be deleted");
        commentService.save(comment);

        // when
        commentService.delete(comment.getId());
        Comment deletedComment = em.find(Comment.class, comment.getId());

        // then
        assertNull(deletedComment);
    }
}