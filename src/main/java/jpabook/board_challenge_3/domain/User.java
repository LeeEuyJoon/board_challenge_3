package jpabook.board_challenge_3.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jpabook.board_challenge_3.repository.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "`user`")
@Getter
@NoArgsConstructor
@ToString
public class User {

    @Id
    private String id;

    private String pwd;
    private String nickname;

    @Embedded
    private Address address = new Address();

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;


    public User(String id, String pwd, String nickname, Address address) {
        this.id = id;
        this.pwd = pwd;
        this.nickname = nickname;
        this.address = address;
        this.regDate = LocalDateTime.now();
    }



    // lastUpdated 값을 갱신하는 메서드
    public void updateLastUpdated() {
        this.lastUpdated = LocalDateTime.now();
    }
}
