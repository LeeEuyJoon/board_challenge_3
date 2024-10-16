package jpabook.board_challenge_3.repository;

import static org.junit.jupiter.api.Assertions.*;

import jpabook.board_challenge_3.domain.Post;
import jpabook.board_challenge_3.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("testUser", "password", "nickname", null);
        userRepository.save(user);
    }

    @Test
    void saveAndFindPostTest() {
        // given
        Post post = new Post(user, "Test Title", "Test Content");
        postRepository.save(post);

        // when
        Post foundPost = postRepository.find(post.getId());

        // then
        assertNotNull(foundPost);
        assertEquals(post.getTitle(), foundPost.getTitle());
        assertEquals(post.getContent(), foundPost.getContent());
    }

    @Test
    void findAllPostsTest() {
        // given
        Post post1 = new Post(user, "Title 1", "Content 1");
        Post post2 = new Post(user, "Title 2", "Content 2");
        postRepository.save(post1);
        postRepository.save(post2);

        // when
        List<Post> posts = postRepository.findAll();

        // then
        assertThat(posts).isNotEmpty();
        assertThat(posts.size()).isEqualTo(2);
    }

    @Test
    void getPageTest() {
        // given
        for (int i = 1; i <= 10; i++) {
            Post post = new Post(user, "Title " + i, "Content " + i);
            postRepository.save(post);
        }

        // when
        int offset = 0;
        int size = 5;
        List<Post> pagePosts = postRepository.getPage(offset, size);

        // then
        assertThat(pagePosts).hasSize(size);
        assertEquals("Title 10", pagePosts.get(0).getTitle()); // 최신순 정렬 확인
    }

    @Test
    void deletePostTest() {
        // given
        Post post = new Post(user, "Title", "Content");
        postRepository.save(post);

        // when
        postRepository.delete(post);
        Post deletedPost = postRepository.find(post.getId());

        // then
        assertNull(deletedPost);
    }

    @Test
    void updatePostTest() {
        // given
        Post post = new Post(user, "Original Title", "Original Content");
        postRepository.save(post);

        // when
        Post updatePost = new Post(post.getId(), user, "Updated Title", "Updated Content" );
        postRepository.update(updatePost);
        Post updatedPost = postRepository.find(post.getId());

        // then
        assertEquals("Updated Title", updatedPost.getTitle());
        assertEquals("Updated Content", updatedPost.getContent());
    }

    @Test
    void countAllTest() {
        Post post = new Post(user, "Title 1", "Content 1");
        postRepository.save(post);
        Post post2 = new Post(user, "Title 2", "Content 2");
        postRepository.save(post2);
        Post post3 = new Post(user, "Title 3", "Content 3");
        postRepository.save(post3);
        Post post4 = new Post(user, "Title 4", "Content 4");
        postRepository.save(post4);
        Post post5 = new Post(user, "Title 5", "Content 5");
        postRepository.save(post5);

        assertEquals(5, postRepository.countAll());
    }
}