package jpabook.board_challenge_3.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jpabook.board_challenge_3.domain.Comment;
import jpabook.board_challenge_3.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;

    public void save(Post post) {
        em.persist(post);
    }

    public Integer countAll() {
        return em.createQuery(
                "select count(*) from Post"
                , Long.class
        ).getSingleResult().intValue();
    }

    public Post find(Long postId) {
        Post post = em.find(Post.class, postId);
        return post;
    }

    public List<Post> findAll() {
        return em.createQuery("select p from Post p", Post.class)
                .getResultList();
    }

    public List<Post> getPage(int offset, int size) {
        TypedQuery<Post> query = em.createQuery(
                "select p from Post p" +
                        " ORDER BY p.regDate DESC"
                , Post.class);
        query.setFirstResult(offset);
        query.setMaxResults(size);
        return query.getResultList();
    }

    public void delete(Post post) {
        em.remove(post);
    }

    public void deleteAll() {
        em.createQuery("delete from Post").executeUpdate();
    }

    public void update(Post post) {
        em.merge(post);
    }

    public void incrementViewCount(Long postId) {
        Post post = em.find(Post.class, postId);
        if (post != null) {
            post.increaseViewCount();  // 조회수 증가 메서드 호출
        }
    }

}
