package jpabook.board_challenge_3.service;

import jpabook.board_challenge_3.domain.Comment;
import jpabook.board_challenge_3.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment findById(Long id) {
        return commentRepository.find(id);
    }

    public List<Comment> findByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    public void update(Comment comment) {

        comment.updateLastUpdated();

        commentRepository.update(comment);
    }

    public void delete(Long id) {

        Comment comment = findById(id);

        commentRepository.delete(comment);
    }

}
