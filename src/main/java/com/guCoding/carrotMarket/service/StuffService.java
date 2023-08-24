package com.guCoding.carrotMarket.service;

import com.guCoding.carrotMarket.domain.like.Like;
import com.guCoding.carrotMarket.domain.stuff.Stuff;
import com.guCoding.carrotMarket.domain.stuff.StuffRepository;
import com.guCoding.carrotMarket.domain.user.TownEnum;
import com.guCoding.carrotMarket.domain.user.User;
import com.guCoding.carrotMarket.domain.user.UserRepository;
import com.guCoding.carrotMarket.dto.stuff.StuffReqDto;
import com.guCoding.carrotMarket.dto.stuff.StuffReqDto.StuffEditReqDto;
import com.guCoding.carrotMarket.dto.stuff.StuffReqDto.StuffInquireReqDto;
import com.guCoding.carrotMarket.dto.stuff.StuffReqDto.StuffSaveReqDto;
import com.guCoding.carrotMarket.dto.stuff.StuffRespDto;
import com.guCoding.carrotMarket.dto.stuff.StuffRespDto.StuffDetailRespDto;
import com.guCoding.carrotMarket.dto.stuff.StuffRespDto.StuffEditRespDto;
import com.guCoding.carrotMarket.dto.stuff.StuffRespDto.StuffInquireRespDto;
import com.guCoding.carrotMarket.dto.stuff.StuffRespDto.StuffSaveRespDto;
import com.guCoding.carrotMarket.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class StuffService {

    private final StuffRepository stuffRepository;
    private final UserRepository userRepository;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public StuffSaveRespDto 내물건등록(StuffSaveReqDto stuffSaveReqDto, Long userId) {
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

    public StuffDetailRespDto 물건상세조회(Long stuffId, Long userId) {
        User userPS = userRepository.findById(userId).orElseThrow(() -> new CustomApiException("해당 id 에 해당하는 유저가 없습니다."));
        Stuff stuffPS = stuffRepository.findById(stuffId).orElseThrow(() -> new CustomApiException("해당 id 에 해당하는 물건이 없습니다."));
        // 좋아요 추가에 따른 기능 수정
        List<Like> likes = stuffPS.getLikes();
        likes.forEach(like -> {
            if (like.getUser().getId() == userId) {
                stuffPS.settingLikeState(true);
            } else stuffPS.settingLikeState(false);
        });
        stuffPS.settingLikeCount(stuffPS.getLikes().size());

        return new StuffDetailRespDto(stuffPS, userPS);
    }

    // 가격 수정만 있다고 가정함
    public StuffEditRespDto 물건수정(StuffEditReqDto stuffEditReqDto, Long stuffId, Long userId) {
        Stuff stuffPS = stuffRepository.findById(stuffId).orElseThrow(() -> new CustomApiException("해당 id 에 해당하는 물건이 없습니다."));
        // 본인 소유 물건인지 확인
        stuffPS.checkOwner(userId);
        int beforePrice = stuffPS.getPrice();
        stuffPS.changePrice(stuffEditReqDto.getPrice());
        if (stuffEditReqDto.getPrice() < beforePrice) {
            // 좋아요 사람에게 알림
            List<Like> likes = stuffPS.getLikes();
            likes.forEach(like -> log.debug("가격이 내려갔습니다. " + like.getUser().getNickname() +"님"));
        }
        return new StuffEditRespDto(stuffPS);
    }

}
