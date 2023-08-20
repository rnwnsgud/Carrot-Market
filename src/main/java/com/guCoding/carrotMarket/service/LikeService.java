package com.guCoding.carrotMarket.service;

import com.guCoding.carrotMarket.domain.like.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class LikeService {

    private final LikeRepository likeRepository;

    public void 좋아요추가(Long stuffId, Long userId) {
        // 연관관계를 두개나 들고있어서 엔티티를 select 해서 넣는 방법보다는 쿼리를 작성한다.
        likeRepository.mLike(stuffId, userId);
    }

    public void 좋아요취소(Long stuffId, Long userId) {
        likeRepository.munLike(stuffId, userId);
    }

    // 상품의 좋아요개수 얻어내는 메서드

}
