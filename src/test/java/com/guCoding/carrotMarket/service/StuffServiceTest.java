package com.guCoding.carrotMarket.service;

import com.guCoding.carrotMarket.config.dummy.DummyObject;
import com.guCoding.carrotMarket.domain.like.Like;
import com.guCoding.carrotMarket.domain.stuff.Stuff;
import com.guCoding.carrotMarket.domain.stuff.StuffRepository;
import com.guCoding.carrotMarket.domain.user.User;
import com.guCoding.carrotMarket.domain.user.UserRepository;
import com.guCoding.carrotMarket.dto.stuff.StuffReqDto;
import com.guCoding.carrotMarket.dto.stuff.StuffReqDto.StuffSaveReqDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.guCoding.carrotMarket.dto.stuff.StuffReqDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StuffServiceTest extends DummyObject {

    @InjectMocks
    private StuffService stuffService;

    @Mock
    private StuffRepository stuffRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void 물건좋아요성공_test() throws Exception {
        //given : ssar로 로그인, 본인의 물건을 조회할 때
        User ssar = newUser(1L, "01012345678", "ssar");
        User cos = newUser(2L, "01012345678", "cos");
        Stuff umbrella = newMockStuff(3L, "우산", 2000, "투명색이에요.", false, ssar);
        Like like = newMockLike(4L, umbrella, ssar);
        Like like2 = newMockLike(5L, umbrella, cos);
        List<Like> likeList = new ArrayList<>();
        likeList.add(like);
        likeList.add(like2);
        //when
        if (like.getUser().getId() == ssar.getId()) {
            umbrella.settingLikeState(true);
        }

        umbrella.settingLikeCount(likeList.size());

        //then
        assertThat(umbrella.isLikeState()).isEqualTo(true);
        assertThat(umbrella.getLikeCount()).isEqualTo(2);
    }

    @Test
    public void 물건좋아요실패_test() throws Exception {
        //given : cos로 로그인, ssar의 물건을 조회할 때
        User ssar = newUser(1L, "01012345678", "ssar");
        User cos = newUser(2L, "01012345678", "cos");
        Stuff umbrella = newMockStuff(3L, "우산", 2000, "투명색이에요.", false, ssar);
        Like like = newMockLike(4L, umbrella, ssar);
        Like like2 = newMockLike(5L, umbrella, cos);
        List<Like> likeList = new ArrayList<>();
        likeList.add(like);
        likeList.add(like2);
        //when
        if (like.getUser().getId() == cos.getId()) {
            umbrella.settingLikeState(true);
        } else {
            umbrella.settingLikeState(false);
        }
        umbrella.settingLikeCount(likeList.size());

        //then
        assertThat(umbrella.isLikeState()).isEqualTo(false);

    }

    @Test
    public void 물건수정_test() throws Exception {
        //given
        Long userId = 1L;
        Long userId2 = 10L;
        int beforePrice = 2000;
        User ssar = newUser(userId, "01012345678", "ssar");
        User cos = newUser(userId2, "01012345678", "cos");
        Stuff umbrella = newMockStuff(2L, "우산", beforePrice, "투명색이에요.", false, ssar);

        Like like = newMockLike(4L, umbrella, ssar);
        Like like2 = newMockLike(5L, umbrella, cos);
        List<Like> likeList = new ArrayList<>();
        likeList.add(like);
        likeList.add(like2);

        StuffEditReqDto stuffEditReqDto = new StuffEditReqDto();
        stuffEditReqDto.setPrice(1500);
        umbrella.changePrice(stuffEditReqDto.getPrice());
        //when
        umbrella.checkOwner(1L);
        List<String> nicknames = new ArrayList<>();
        if (umbrella.getPrice() < beforePrice) {
            likeList.forEach(l -> nicknames.add(l.getUser().getNickname()));
        }
        //then
        assertThat(umbrella.getPrice()).isEqualTo(1500);
        assertTrue(nicknames.contains("ssar"));
    }


}