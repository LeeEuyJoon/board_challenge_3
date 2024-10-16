package jpabook.board_challenge_3.controller;

import jpabook.board_challenge_3.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping("/idCheck")
    public ResponseEntity<Boolean> idCheck(@RequestParam String id) {
        boolean isDuplicated = userService.validateDuplicateMemberId(id);
        return ResponseEntity.ok(isDuplicated);
    }

    @GetMapping("/nickCheck")
    public ResponseEntity<Boolean> nickCheck(@RequestParam String nickname) {
        boolean isDuplicated = userService.validateDuplicateMemberId(nickname);
        return ResponseEntity.ok(isDuplicated);
    }
}
