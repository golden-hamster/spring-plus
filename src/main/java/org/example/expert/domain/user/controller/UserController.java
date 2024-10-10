package org.example.expert.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.request.UserUpdateRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping("/users/search")
    public ResponseEntity<UserResponse> searchUser(@RequestParam String nickname) {
        long startTime = System.currentTimeMillis();

        UserResponse userResponse = userService.searchUser(nickname);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        log.info("조회시간 = {} ms", duration);

        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/users")
    public void changePassword(@AuthenticationPrincipal AuthUser authUser, @RequestBody UserChangePasswordRequest userChangePasswordRequest) {
        userService.changePassword(authUser.getId(), userChangePasswordRequest);
    }

    @PutMapping("/users/me")
    public ResponseEntity<Void> updateMyInfo(@AuthenticationPrincipal AuthUser authUser, @RequestBody UserUpdateRequest userUpdateRequest) {
        userService.updateUser(authUser.getId(), userUpdateRequest);
        return ResponseEntity.noContent().build();
    }
}
