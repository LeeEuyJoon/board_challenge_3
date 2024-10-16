package jpabook.board_challenge_3.repository;

import jakarta.persistence.EntityManager;
import jpabook.board_challenge_3.domain.Comment;
import jpabook.board_challenge_3.domain.Post;
import jpabook.board_challenge_3.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public void save(User user) {
        em.persist(user);
    }

    public User find(String id) {
        return em.find(User.class, id);
    }

    public void delete(User user) {
        em.remove(user);
    }

    public void update(User user) {
        em.merge(user);
    }

    public User findById(String id) {
        try {
            return em.createQuery("select u from User u where u.id =:id", User.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public User findByNickname(String nickname) {
        try {
            return em.createQuery("select u from User u where u.nickname =:nickname", User.class)
                    .setParameter("nickname", nickname)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Post> findPostsByUserId(String userId) {
        return em.createQuery("select p from Post p join fetch p.writer where p.writer.id = :id", Post.class) // 페치 조인 전략
                .setParameter("id", userId)
                .getResultList();
    }

    public List<Comment> findCommentsByUserId(String userId) {
        return em.createQuery("select c from Comment c join fetch c.user where c.user.id = :id", Comment.class) // 페치 조인 전략
                .setParameter("id", userId)
                .getResultList();
    }

    public void deleteAll() {
        em.createQuery("delete from User").executeUpdate();
    }
}
