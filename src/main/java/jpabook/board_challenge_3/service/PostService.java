package jpabook.board_challenge_3.service;

import jpabook.board_challenge_3.domain.Post;
import jpabook.board_challenge_3.repository.PostRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;


    public void save(Post post) {
        postRepository.save(post);
    }

    public Integer countAll() {
        return postRepository.countAll();
    }


    public Post find(Long id) {
        return postRepository.find(id);
    }


    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public List<Post> getPostsPage(int page, int size) {
        int offset = (page - 1) * size;
        return postRepository.getPage(offset, size);
    }

    public void delete(Long id) {
        Post post = postRepository.find(id);
        postRepository.delete(post);
    }

    public void update(Post post) {
        post.updateLastUpdated();
        postRepository.update(post);
    }

    public void plusViewCnt(Long postId) {
        postRepository.incrementViewCount(postId);
    }

}
