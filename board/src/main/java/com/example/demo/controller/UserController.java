package com.example.demo.controller;

import com.example.demo.DTO.UserRequest;
import com.example.demo.core.security.CustomUserDetails;
import com.example.demo.core.security.JwtTokenProvider;
import com.example.demo.core.utils.ApiUtils;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<Object> join(@RequestBody @Valid UserRequest.JoinDto joinDto, Error error){
        userService.join(joinDto);

        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PostMapping("/check")
    public ResponseEntity<Object> check(@RequestBody @Valid UserRequest.JoinDto joinDto, Error error){
        userService.checkEmail(joinDto.getEmail());

        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PostMapping("/oauth")
    public ResponseEntity<Object> connect(@RequestBody @Valid UserRequest.JoinDto joinDto, Error error){
        String jwt = userService.connect(joinDto);

        return ResponseEntity.ok().header(JwtTokenProvider.HEADER, jwt)
                .body(ApiUtils.success(null));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid UserRequest.JoinDto joinDto, HttpServletRequest req, Error error){
        userService.login(joinDto, req.getSession());
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest req, Error error){
        return userService.logout(req.getSession());
    }

    @GetMapping("/users")
    public ResponseEntity<Object> printUsers(){
        userService.findAll();

        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PostMapping("/user_id")
    public ResponseEntity<ApiUtils.ApiResult<Long>> getCurrentUser(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        if (customUserDetails.getUser() == null){
            return ResponseEntity.ok(ApiUtils.error("현재 로그인된 user가 없습니다.", HttpStatus.UNAUTHORIZED));
        }
        //User user = userService.getUserById(customUserDetails.getUser().getId());
        // user.output();
        return ResponseEntity.ok(ApiUtils.success(customUserDetails.getUser().getId()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Object> tokenRefresh(@AuthenticationPrincipal CustomUserDetails customUserDetails, HttpServletRequest req){
        if (customUserDetails.getUser() == null){
            return ResponseEntity.ok(ApiUtils.error("현재 로그인된 user가 없습니다.", HttpStatus.UNAUTHORIZED));
        }
        userService.refresh(customUserDetails.getUser().getId(), req.getSession());
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @GetMapping("/send_user_id")
    public ResponseEntity<ApiUtils.ApiResult<Long>> getCurrnetUserId(HttpServletRequest req){
        return ResponseEntity.ok(ApiUtils.success(userService.getCurrnetUserId(req.getSession())));
    }
}
