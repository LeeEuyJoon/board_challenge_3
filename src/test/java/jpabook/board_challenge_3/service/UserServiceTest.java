package jpabook.board_challenge_3.service;

import jpabook.board_challenge_3.domain.Address;
import jpabook.board_challenge_3.domain.Post;
import jpabook.board_challenge_3.domain.User;
import jpabook.board_challenge_3.repository.PostRepository;
import jpabook.board_challenge_3.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    public User createTestUser(String id, String nickname) {
        return new User(id, "testPwd", nickname, new Address("City", "Street", "Zipcode"));
    }

    @Test
    void joinTest_Success() {
        // given
        User user = createTestUser("testUser", "testNickname");

        // when
        String savedUserId = userService.join(user);

        // then
        User foundUser = userRepository.findById(savedUserId);
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(user.getId());
        assertThat(foundUser.getNickname()).isEqualTo(user.getNickname());
    }

    @Test
    void joinTest_DuplicateId() {
        // given
        User user1 = createTestUser("testUser", "nickname1");
        User user2 = createTestUser("testUser", "nickname2"); // ID 중복
        userService.join(user1);

        // when & then
        assertThatThrownBy(() -> userService.join(user2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 사용중인 ID입니다.");
    }

    @Test
    void joinTest_DuplicateNickname() {
        // given
        User user1 = createTestUser("user1", "testNickname");
        User user2 = createTestUser("user2", "testNickname"); // 닉네임 중복
        userService.join(user1);

        // when & then
        assertThatThrownBy(() -> userService.join(user2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 사용중인 닉네임입니다.");
    }

    @Test
    public void updateTest() {
        // given
        User user = createTestUser("testUser", "testNickname");
        userService.join(user);
        User findBeforeUpdateUser = userService.find(user.getId());
        System.out.println("findBeforeUpdateUser : " + findBeforeUpdateUser);

        User updatedUser = createTestUser("testUser", "updatedNickname");

        // when
        userService.update(updatedUser);
        User findAfterUdpateUser = userService.find(updatedUser.getId());
        System.out.println("findAfterUpdateUser : " + findAfterUdpateUser);

        // then
        assertEquals(findBeforeUpdateUser.getId(), "testUser");
        assertEquals(findAfterUdpateUser.getNickname(), "updatedNickname");
        assertEquals(findBeforeUpdateUser.getPwd(), findAfterUdpateUser.getPwd());
        assertEquals(findBeforeUpdateUser.getRegDate(), findAfterUdpateUser.getRegDate());
    }
}