package jpabook.board_challenge_3.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor
@AllArgsConstructor
//@ToString
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    private String title;
    private String content;

    @Column(name = "view_cnt")
    private Integer viewCnt;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public Post(User user, String title, String content) {
        this.writer = user;
        this.title = title;
        this.content = content;
        this.viewCnt = 0;
        this.regDate = LocalDateTime.now();
    }

    // 테스트용
    public Post(Long id, User user, String title, String content) {
        this.id = id;
        this.writer = user;
        this.title = title;
        this.content = content;
        this.viewCnt = 0;
        this.regDate = LocalDateTime.now();
    }

    public void updateLastUpdated() {
        this.lastUpdated = LocalDateTime.now();
    }

    public void increaseViewCount() {
        this.viewCnt++;
    }


    // 업데이트 로직
    public void updatePost(String title, String content) {
        this.title = title;
        this.content = content;
        this.lastUpdated = LocalDateTime.now(); // 수정 시간 업데이트
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setPost(this);
    }

}
