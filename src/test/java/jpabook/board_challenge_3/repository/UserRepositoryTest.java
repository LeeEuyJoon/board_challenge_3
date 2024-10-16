package jpabook.board_challenge_3.repository;

import jpabook.board_challenge_3.domain.Address;
import jpabook.board_challenge_3.domain.Comment;
import jpabook.board_challenge_3.domain.Post;
import jpabook.board_challenge_3.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;


    private User createTestUser(String id, String nickname) {
        return new User(id, "password", nickname, new Address("City", "Street", "Zipcode"));
    }

    @Test
    void saveUserTest() {
        // given
        User user = createTestUser("testUser1", "testNickname1");

        // when
        userRepository.save(user);
        User foundUser = userRepository.find(user.getId());

        // then
        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
    }

//    @Test
//    void findUserByIdTest() {
//        // given
//        User user = createTestUser("testUser2", "testNickname2");
//        userRepository.save(user);
//
//        // when
//        List<User> foundUsers = userRepository.findById(user.getId());
//
//        // then
//        assertFalse(foundUsers.isEmpty());
//        assertEquals(user.getId(), foundUsers.get(0).getId());
//    }

//    @Test
//    void findUserByNicknameTest() {
//        // given
//        User user = createTestUser("testUser3", "testNickname3");
//        userRepository.save(user);
//
//        // when
//        List<User> foundUsers = userRepository.findByNickname(user.getNickname());
//
//        // then
//        assertFalse(foundUsers.isEmpty());
//        assertEquals(user.getNickname(), foundUsers.get(0).getNickname());
//    }

    @Test
    void updateUserTest() {
        // given
        User user = createTestUser("testUser4", "testNickname4");
        userRepository.save(user);

        // when
        user = userRepository.find(user.getId());
        User updatedUser = new User(user.getId(), "newPassword", "newNickname", user.getAddress());
        userRepository.update(updatedUser);

        User foundUser = userRepository.find(user.getId());

        // then
        assertEquals("newNickname", foundUser.getNickname());
        assertEquals("newPassword", foundUser.getPwd());
    }

    @Test
    void deleteUserTest() {
        // given
        User user = createTestUser("testUser5", "testNickname5");
        userRepository.save(user);

        // when
        userRepository.delete(user);
        User foundUser = userRepository.find(user.getId());

        // then
        assertNull(foundUser);
    }

    @Test
    void findPostsByUserIdTest() {
        // given
        User user = createTestUser("testUser6", "testNickname6");
        userRepository.save(user);
        Post post1 = new Post(user,"title1","content1");
        Post post2 = new Post(user,"title2","content2");
        Post post3 = new Post(user,"title3","content3");

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        // when
        List<Post> foundPosts = userRepository.findPostsByUserId(user.getId());

        // then
        assertFalse(foundPosts.isEmpty());
        assertEquals(3, foundPosts.size());
    }

    @Test
    void findCommentsByUserIdTest() {
        // given
        User user = createTestUser("testUser7", "testNickname7");
        userRepository.save(user);

        Post post1 = new Post(user,"title1","content1");
        Post post2 = new Post(user,"title2","content2");
        postRepository.save(post1);
        postRepository.save(post2);


        Comment comment1 = new Comment(user, post1,"Test Comment1");
        Comment comment2 = new Comment(user, post1,"Test Comment2");
        Comment comment3 = new Comment(user, post1,"Test Comment3");
        Comment comment4 = new Comment(user, post2,"Test Comment4");
        Comment comment5 = new Comment(user, post2,"Test Comment5");
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        commentRepository.save(comment4);
        commentRepository.save(comment5);

        // when
        List<Comment> foundComments = userRepository.findCommentsByUserId(user.getId());

        // then
        assertFalse(foundComments.isEmpty());
        assertEquals(5, foundComments.size());
    }
}