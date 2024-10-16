package jpabook.board_challenge_3.repository;

import jakarta.persistence.EntityManager;
import jpabook.board_challenge_3.domain.Comment;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final EntityManager em;

    public void save(Comment comment) {
        em.persist(comment);
    }

    public Comment find(Long id) {
        return em.find(Comment.class, id);
    }

    public List<Comment> findByPostId(Long postId) {
        return em.createQuery(
                "select c from Comment c" +
                        " where c.post.id = :postId"
                , Comment.class
        )
                .setParameter("postId", postId)
                .getResultList();
    }

    public void delete(Comment comment) {
        em.remove(comment);
    }

    public void update(Comment comment) {
        em.merge(comment);
    }

}
