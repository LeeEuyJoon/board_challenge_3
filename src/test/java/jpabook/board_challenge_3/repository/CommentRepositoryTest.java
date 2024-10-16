package jpabook.board_challenge_3.repository;

import static org.junit.jupiter.api.Assertions.*;


import jakarta.persistence.EntityManager;
import jpabook.board_challenge_3.domain.Comment;
import jpabook.board_challenge_3.domain.Post;
import jpabook.board_challenge_3.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

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
        commentRepository.save(comment);

        // then
        assertThat(comment.getId()).isNotNull();
        Comment foundComment = em.find(Comment.class, comment.getId());
        assertThat(foundComment).isNotNull();
        assertThat(foundComment.getContent()).isEqualTo("Test Comment");
    }

    @Test
    void findCommentsByPostId() {
        // given
        Comment comment1 = new Comment(testUser, testPost, "First Comment");
        Comment comment2 = new Comment(testUser, testPost, "Second Comment");
        commentRepository.save(comment1);
        commentRepository.save(comment2);

        // when
        List<Comment> comments = commentRepository.findByPostId(testPost.getId());

        // then
        assertThat(comments).hasSize(2);
        assertThat(comments).extracting("content").containsExactlyInAnyOrder("First Comment", "Second Comment");
    }

    @Test
    void deleteComment() {
        // given
        Comment comment = new Comment(testUser, testPost, "Comment to be deleted");
        commentRepository.save(comment);

        // when
        commentRepository.delete(comment);
        Comment deletedComment = em.find(Comment.class, comment.getId());

        // then
        assertThat(deletedComment).isNull();
    }

    @Test
    void updateComment() {
        // given
        Comment comment = new Comment(testUser, testPost, "Original Comment");
        commentRepository.save(comment);

        // when
        Comment updateComment = new Comment(comment.getId(), testUser, testPost, "Updated Comment");
        commentRepository.update(updateComment);
        Comment updatedComment = em.find(Comment.class, comment.getId());

        // then
        assertThat(updatedComment.getContent()).isEqualTo("Updated Comment");
    }
}