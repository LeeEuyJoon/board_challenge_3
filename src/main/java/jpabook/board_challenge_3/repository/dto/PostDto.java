package jpabook.board_challenge_3.repository.dto;

import jpabook.board_challenge_3.domain.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDto {

    private Long id;
    private String writer;
    private String title;
    private String content;
    private Integer viewCnt;
    private LocalDateTime regDate;
    private LocalDateTime lastUpdated;

    public PostDto(Post post) {
        this.id = post.getId();
        this.writer = post.getWriter().getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.viewCnt = post.getViewCnt();
        this.regDate = post.getRegDate();
        this.lastUpdated = post.getLastUpdated();
    }


}
