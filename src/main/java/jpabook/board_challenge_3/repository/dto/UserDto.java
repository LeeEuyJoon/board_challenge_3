package jpabook.board_challenge_3.repository.dto;

import jpabook.board_challenge_3.domain.Address;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {

    private String id;
    private String pwd;
    private String nickname;
    private Address address;
    private LocalDateTime regDate;
    private LocalDateTime lastUpdated;

}
