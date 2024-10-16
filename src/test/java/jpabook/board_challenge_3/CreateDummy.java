package jpabook.board_challenge_3;

import jpabook.board_challenge_3.domain.Comment;
import jpabook.board_challenge_3.domain.Post;
import jpabook.board_challenge_3.domain.User;
import jpabook.board_challenge_3.repository.PostRepository;
import jpabook.board_challenge_3.repository.UserRepository;
import jpabook.board_challenge_3.service.CommentService;
import jpabook.board_challenge_3.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@SpringBootTest
@Transactional
@Rollback(false)
public class CreateDummy {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private List<User> users;
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostService postService;

    @BeforeEach
    public void before() {

        postRepository.deleteAll();
        userRepository.deleteAll();

        User user1 = new User("user1_id", "user1_pwd", "user1_nickname", null);
        User user2 = new User("user2_id", "user2_pwd", "user2_nickname", null);
        User user3 = new User("user3_id", "user3_pwd", "user3_nickname", null);

        users = Arrays.asList(user1, user2, user3);

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
    }

    @Order(1)
    @Test
    public void createPostsAndComments() {
        Random random = new Random();

        for (int i = 0; i < 200; i++) {
            User randomUser = users.get(random.nextInt(users.size()));
            Post post = new Post(randomUser, "Title " + (i + 1), "Content for post number " + (i + 1));
            postRepository.save(post);

            Comment comment1 = new Comment(randomUser, post, "Comment1");
            Comment comment2 = new Comment(randomUser, post, "Comment2");
            post.addComment(comment1);
            post.addComment(comment2);
            commentService.save(comment1);
            commentService.save(comment2);
        }
    }

}