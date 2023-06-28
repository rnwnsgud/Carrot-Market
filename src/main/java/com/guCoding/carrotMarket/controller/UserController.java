package com.guCoding.carrotMarket.controller;

import com.guCoding.carrotMarket.dto.ResponseDto;
import com.guCoding.carrotMarket.dto.user.UserReqDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

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
    public ResponseEntity<?> checkAuth(){

        return new ResponseEntity<>(new ResponseDto<>(1, "인증성공", null), HttpStatus.OK);
    }
}
