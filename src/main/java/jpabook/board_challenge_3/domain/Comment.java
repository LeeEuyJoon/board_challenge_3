package jpabook.board_challenge_3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor
@ToString
public class Comment {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Post post;

    private String content;
//    private Integer like;

    @Column(name = "reg_date")
    private LocalDateTime regDate;
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    public Comment(User user, Post post, String content) {
        this.user = user;
        this.post = post;
        this.content = content;
        this.regDate = LocalDateTime.now();
    }

    // 테스트용
    public Comment(Long id, User user, Post post, String content) {
        this.id = id;
        this.user = user;
        this.post = post;
        this.content = content;
        this.regDate = LocalDateTime.now();
    }

    public void updateLastUpdated() {
        this.lastUpdated = LocalDateTime.now();
    }

    // 업데이트 메서드
    public void updateComment(String content) {
        this.content = content;
        this.lastUpdated = LocalDateTime.now();
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
