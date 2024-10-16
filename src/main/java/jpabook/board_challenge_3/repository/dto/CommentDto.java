package jpabook.board_challenge_3.repository.dto;

import jpabook.board_challenge_3.domain.Comment;
import jpabook.board_challenge_3.domain.Post;
import jpabook.board_challenge_3.domain.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {

    private Long id;
    private String userNickname;
    private Post post;
    private String content;
    private LocalDateTime regDate;
    private LocalDateTime lastUpdated;

//    private PostDto postDto;  // 댓글이 달린 게시글

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.userNickname = comment.getUser().getNickname();
        this.post = comment.getPost();
        this.content = comment.getContent();
        this.regDate = comment.getRegDate();
        this.lastUpdated = comment.getLastUpdated();
    }

}
