package com.guCoding.carrotMarket.controller;

import com.guCoding.carrotMarket.config.auth.LoginUser;
import com.guCoding.carrotMarket.dto.ResponseDto;
import com.guCoding.carrotMarket.dto.stuff.StuffReqDto;
import com.guCoding.carrotMarket.dto.stuff.StuffReqDto.StuffSaveReqDto;
import com.guCoding.carrotMarket.dto.stuff.StuffRespDto;
import com.guCoding.carrotMarket.dto.stuff.StuffRespDto.StuffSaveRespDto;
import com.guCoding.carrotMarket.service.StuffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class StuffController {

    private final StuffService stuffService;

    @PostMapping("/api/s/stuff")
    public ResponseEntity<?> saveStuff(@RequestBody @Valid StuffSaveReqDto stuffSaveReqDto, @AuthenticationPrincipal LoginUser loginUser){
        StuffSaveRespDto stuffSaveRespDto = stuffService.내물건팔기(loginUser.getUser().getId(), stuffSaveReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "내 물건 팔기 등록 완료", stuffSaveRespDto), HttpStatus.CREATED);
    }
}
