package com.guCoding.carrotMarket.controller;

import com.guCoding.carrotMarket.config.auth.LoginUser;
import com.guCoding.carrotMarket.domain.user.TownEnum;
import com.guCoding.carrotMarket.dto.ResponseDto;
import com.guCoding.carrotMarket.dto.stuff.StuffReqDto;
import com.guCoding.carrotMarket.dto.stuff.StuffReqDto.StuffInquireReqDto;
import com.guCoding.carrotMarket.dto.stuff.StuffReqDto.StuffSaveReqDto;
import com.guCoding.carrotMarket.dto.stuff.StuffRespDto;
import com.guCoding.carrotMarket.dto.stuff.StuffRespDto.StuffInquireRespDto;
import com.guCoding.carrotMarket.dto.stuff.StuffRespDto.StuffSaveRespDto;
import com.guCoding.carrotMarket.service.StuffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/s")
@RequiredArgsConstructor
@RestController
public class StuffController {

    private final StuffService stuffService;

    @PostMapping("/stuff")
    public ResponseEntity<?> saveStuff(@RequestBody @Valid StuffSaveReqDto stuffSaveReqDto, @AuthenticationPrincipal LoginUser loginUser){
        StuffSaveRespDto stuffSaveRespDto = stuffService.내물건등록(loginUser.getUser().getId(), stuffSaveReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "내 물건 팔기 등록 완료", stuffSaveRespDto), HttpStatus.CREATED);
    }

    @GetMapping("/stuff/{townEnum}")
    public ResponseEntity<?> inquireStuff(@PathVariable TownEnum townEnum){
        StuffInquireRespDto stuffInquireRespdto = stuffService.물건지역조회(townEnum);
        return new ResponseEntity<>(new ResponseDto<>(1, "물건 지역 조회 완료", stuffInquireRespdto), HttpStatus.OK);
    }
}
