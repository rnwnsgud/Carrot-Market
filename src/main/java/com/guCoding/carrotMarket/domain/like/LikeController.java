package com.guCoding.carrotMarket.domain.like;

import com.guCoding.carrotMarket.config.auth.LoginUser;
import com.guCoding.carrotMarket.dto.ResponseDto;
import com.guCoding.carrotMarket.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/s")
@RequiredArgsConstructor
@RestController
public class LikeController {

    private final LikeService likeService;
    
    @PostMapping("/like/{stuffId}")
    public ResponseEntity<?> like(@PathVariable Long stuffId, @AuthenticationPrincipal LoginUser loginUser){
        likeService.좋아요추가(stuffId, loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "좋아요 성공", null), HttpStatus.CREATED);
    }

    @DeleteMapping("/like/{stuffId}")
    public ResponseEntity<?> unLike(@PathVariable Long stuffId, @AuthenticationPrincipal LoginUser loginUser){
        likeService.좋아요취소(stuffId, loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "좋아요 취소 성공", null), HttpStatus.CREATED);
    }
}
