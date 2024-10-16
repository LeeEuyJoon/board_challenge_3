package jpabook.board_challenge_3.service;

import jpabook.board_challenge_3.domain.Comment;
import jpabook.board_challenge_3.domain.Post;
import jpabook.board_challenge_3.domain.User;
import jpabook.board_challenge_3.repository.PostRepository;
import jpabook.board_challenge_3.repository.UserRepository;
import jpabook.board_challenge_3.repository.dto.CommentDto;
import jpabook.board_challenge_3.repository.dto.PostDto;
import jpabook.board_challenge_3.repository.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository commentRepository;

    @Transactional
    public String join(User user) {

        if ((validateDuplicateMemberId(user.getId()))) { // 중복 id 검증
            throw new IllegalStateException("이미 사용중인 ID 입니다.");
        }
        if ((validateDuplicateMemberNickname(user.getNickname()))) {// 중복 닉네임 검증
            throw new IllegalStateException("이미 사용중인 닉네임 입니다.");
        }
        userRepository.save(user);
        return user.getId();
    }

    @Transactional
    public User find(String id) {
        User user = null;
        try {
            user = userRepository.findById(id);
        } catch (Exception e) {
            return null;
        }
        return user;
    }

    @Transactional
    public String update(User user) {

        // lastUpdated 갱신하고
        user.updateLastUpdated();

        // repository update 호출
        userRepository.update(user);

        return user.getId();
    }

    @Transactional
    public void delete(User user) {
        userRepository.delete(user);
    }

    public boolean validateDuplicateMemberId(String id) {
        User user = find(id);
        if (user == null) {
            return false;
        }
        return true;
    }


    public boolean validateDuplicateMemberNickname(String nickname) {
        User user = userRepository.findByNickname(nickname);

        if (user == null) {
            return false;
        }
        return true;
    }

    private void isCurrentUserWriter(Post post, User user) {
        String writerId = post.getWriter().getId();
        String userId = user.getId();
        if (!writerId.equals(userId)) {
            throw new IllegalStateException("수정 권한이 없습니다!");
        }
    }


    @Transactional(readOnly = true)
    public List<PostDto> postsPerUser(String userId) {
        List<Post> posts = userRepository.findPostsByUserId(userId);
        return posts.stream()
                .map(o -> new PostDto(o))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<CommentDto> commentsPerUser(String userId) {
        List<Comment> comments = userRepository.findCommentsByUserId(userId);
        return comments.stream()
                .map(o -> new CommentDto(o))
                .collect(Collectors.toList());
    }


}
