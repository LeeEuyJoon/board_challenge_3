package jpabook.board_challenge_3.service;

import jpabook.board_challenge_3.domain.Post;
import jpabook.board_challenge_3.domain.User;
import jpabook.board_challenge_3.repository.PostRepository;
import jpabook.board_challenge_3.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    private User testUser;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        testUser = new User("testUser", "password", "nickname", null);
        userRepository.save(testUser);
    }

    @Test
    void savePost() {
        // given
        Post post = new Post(testUser, "Test Title", "Test Content");

        // when
        postService.save(post);

        // then
        Post foundPost = postService.find(post.getId());
        assertThat(foundPost).isNotNull();
        assertEquals(post.getTitle(), foundPost.getTitle());
    }

    @Test
    void findPostById() {
        // given
        Post post = new Post(testUser, "Test Title", "Test Content");
        postService.save(post);

        // when
        Post foundPost = postService.find(post.getId());

        // then
        assertNotNull(foundPost);
        assertEquals(post.getId(), foundPost.getId());
    }

    @Test
    void findAllPosts() {
        // given
        Post post1 = new Post(testUser, "Title 1", "Content 1");
        Post post2 = new Post(testUser, "Title 2", "Content 2");
        postService.save(post1);
        postService.save(post2);

        // when
        List<Post> allPosts = postService.findAll();

        // then
        assertThat(allPosts).hasSize(2);
    }

    @Test
    void getPostsPage() {
        // given
        for (int i = 0; i < 100; i++) {
            Post post = new Post(testUser, "Title " + i, "Content " + i);
            postService.save(post);
        }

        // when
        List<Post> postsPage = postService.getPostsPage(3, 10);

        // then
        assertThat(postsPage).hasSize(10);
        assertThat(postsPage.getFirst().getTitle()).isEqualTo("Title 79");
        assertThat(postsPage.getLast().getTitle()).isEqualTo("Title 70");
    }

    @Test
    void deletePost() {
        // given
        Post post = new Post(testUser, "Title to Delete", "Content to Delete");
        postService.save(post);
        Long postId = post.getId();

        // when
        postService.delete(postId);

        // then
        Post deletedPost = postService.find(postId);
        assertNull(deletedPost);
    }

    @Test
    void updatePost() {
        // given
        Post post = new Post(testUser, "Initial Title", "Initial Content");
        postService.save(post);
        post.updateLastUpdated();

        // when
        Post updatePost = new Post(post.getId(), testUser, "Updated Title", "Updated Content");
        postService.update(updatePost);
        Post updatedPost = postService.find(updatePost.getId());

        // then
        assertNotNull(updatedPost.getLastUpdated());
        assertEquals("Updated Title", updatedPost.getTitle());
        assertEquals("Updated Content", updatedPost.getContent());
    }
}
