package com.guCoding.carrotMarket.controller;

import com.guCoding.carrotMarket.config.auth.LoginUser;
import com.guCoding.carrotMarket.dto.ResponseDto;
import com.guCoding.carrotMarket.dto.user.UserReqDto;
import com.guCoding.carrotMarket.dto.user.UserReqDto.EditReqDto;
import com.guCoding.carrotMarket.dto.user.UserReqDto.JoinReqDto;
import com.guCoding.carrotMarket.dto.user.UserReqDto.LoginReqDto;
import com.guCoding.carrotMarket.dto.user.UserRespDto;
import com.guCoding.carrotMarket.dto.user.UserRespDto.JoinRespDto;
import com.guCoding.carrotMarket.dto.user.UserRespDto.LoginRespDto;
import com.guCoding.carrotMarket.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @PostMapping("/api/users/join")
    public ResponseEntity<?> join(@RequestBody @Valid JoinReqDto joinReqDto, BindingResult bindingResult){
        JoinRespDto joinRespDto = userService.회원가입(joinReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 완료", joinRespDto), HttpStatus.CREATED);
    }

    @Operation(summary = "로그인", description = "바디에 {password, phoneNumber} 을 json 형식으로 보내주세요, 토큰은 Bearer을 포함해주세요")
    @PostMapping("/api/users/login")
    public ResponseEntity<?> login(@RequestBody LoginReqDto loginReqDto) {
        return null;
    }

    @GetMapping("/api/s/users")
    public ResponseEntity<?> checkAuth(@AuthenticationPrincipal LoginUser loginUser){
        log.debug("loginUser : " + loginUser);
        return new ResponseEntity<>(new ResponseDto<>(1, "인증성공", null), HttpStatus.OK);
    }

    @PutMapping("/api/s/users")
    public ResponseEntity<?> editUser(@RequestBody @Valid EditReqDto editReqDto, @AuthenticationPrincipal LoginUser loginUser){
        userService.회원정보수정(loginUser.getUser().getId(), editReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "회원정보수정 완료", null), HttpStatus.OK);
    }



}
