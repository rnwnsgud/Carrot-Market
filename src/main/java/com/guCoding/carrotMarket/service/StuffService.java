package com.guCoding.carrotMarket.service;

import com.guCoding.carrotMarket.domain.stuff.Stuff;
import com.guCoding.carrotMarket.domain.stuff.StuffRepository;
import com.guCoding.carrotMarket.domain.user.TownEnum;
import com.guCoding.carrotMarket.domain.user.User;
import com.guCoding.carrotMarket.domain.user.UserRepository;
import com.guCoding.carrotMarket.dto.stuff.StuffReqDto;
import com.guCoding.carrotMarket.dto.stuff.StuffReqDto.StuffInquireReqDto;
import com.guCoding.carrotMarket.dto.stuff.StuffReqDto.StuffSaveReqDto;
import com.guCoding.carrotMarket.dto.stuff.StuffRespDto;
import com.guCoding.carrotMarket.dto.stuff.StuffRespDto.StuffDetailRespDto;
import com.guCoding.carrotMarket.dto.stuff.StuffRespDto.StuffInquireRespDto;
import com.guCoding.carrotMarket.dto.stuff.StuffRespDto.StuffSaveRespDto;
import com.guCoding.carrotMarket.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class StuffService {

    private final StuffRepository stuffRepository;
    private final UserRepository userRepository;

    public StuffSaveRespDto 내물건등록(Long userId, StuffSaveReqDto stuffSaveReqDto) {
        User userPS = userRepository.findById(userId).orElseThrow(() -> new CustomApiException("해당 id 에 해당하는 유저가 없습니다."));
        Stuff stuff = stuffSaveReqDto.toEntity(userPS);
        Stuff stuffPS = stuffRepository.save(stuff);
        return new StuffSaveRespDto(stuffPS, stuffSaveReqDto.getTownEnum());

    }

    @Transactional(readOnly = true)
    public StuffInquireRespDto 물건지역조회(TownEnum townEnum) {
        List<Stuff> stuffList = stuffRepository.findByTownEnum(townEnum);
        return new StuffInquireRespDto(stuffList);
    }

    @Transactional(readOnly = true)
    public StuffDetailRespDto 물건상세조회(Long stuffId, Long userId) {
        User userPS = userRepository.findById(userId).orElseThrow(() -> new CustomApiException("해당 id 에 해당하는 유저가 없습니다."));
        Stuff stuffPS = stuffRepository.findById(stuffId).orElseThrow(() -> new CustomApiException("해당 id 에 해당하는 물건이 없습니다."));
        return new StuffDetailRespDto(stuffPS, userPS);
    }
}
