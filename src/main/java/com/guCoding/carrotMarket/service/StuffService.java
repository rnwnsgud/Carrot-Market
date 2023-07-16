package com.guCoding.carrotMarket.service;

import com.guCoding.carrotMarket.domain.stuff.Stuff;
import com.guCoding.carrotMarket.domain.stuff.StuffRepository;
import com.guCoding.carrotMarket.domain.user.User;
import com.guCoding.carrotMarket.domain.user.UserRepository;
import com.guCoding.carrotMarket.dto.stuff.StuffReqDto.StuffSaveReqDto;
import com.guCoding.carrotMarket.dto.stuff.StuffRespDto.StuffSaveRespDto;
import com.guCoding.carrotMarket.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class StuffService {

    private final StuffRepository stuffRepository;
    private final UserRepository userRepository;

    public StuffSaveRespDto 내물건팔기(Long userId, StuffSaveReqDto stuffSaveReqDto) {
        User userPS = userRepository.findById(userId).orElseThrow(() -> new CustomApiException("해당 id 에 해당하는 유저가 없습니다."));
        Stuff stuff = stuffSaveReqDto.toEntity(userPS);
        Stuff stuffPS = stuffRepository.save(stuff);
        return new StuffSaveRespDto(stuffPS, stuffSaveReqDto.getTownEnum());


    }
}
