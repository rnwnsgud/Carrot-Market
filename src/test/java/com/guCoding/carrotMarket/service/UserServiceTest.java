package com.guCoding.carrotMarket.service;

import com.guCoding.carrotMarket.config.dummy.DummyObject;
import com.guCoding.carrotMarket.domain.user.User;
import com.guCoding.carrotMarket.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static com.guCoding.carrotMarket.dto.user.UserReqDto.*;
import static com.guCoding.carrotMarket.dto.user.UserRespDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest extends DummyObject {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void 회원가입_test() throws Exception {
        //given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setPhoneNumber("01012345678");
        joinReqDto.setNickname("ssar");
        joinReqDto.setPassword("1234");

        // stub 1
        when(userRepository.findByPhoneNumber(any())).thenReturn(Optional.empty());

        // stub 2
        when(userRepository.findByIdentifier(any())).thenReturn(Optional.empty());

        // stub 3
        User ssar = newUser(1L, "01012345678", "ssar");
        when(userRepository.save(any())).thenReturn(ssar);

        //when
        JoinRespDto joinRespDto = userService.회원가입(joinReqDto);
        System.out.println("테스트 : " + joinRespDto.getIdentifier());

        //then
        assertThat(joinRespDto.getId()).isEqualTo(1L);
        assertThat(joinRespDto.getIdentifier().length()).isEqualTo(11);

    }
}