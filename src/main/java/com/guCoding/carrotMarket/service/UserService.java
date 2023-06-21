package com.guCoding.carrotMarket.service;

import com.guCoding.carrotMarket.domain.user.User;
import com.guCoding.carrotMarket.domain.user.UserRepository;
import com.guCoding.carrotMarket.dto.user.UserReqDto;
import com.guCoding.carrotMarket.dto.user.UserReqDto.JoinReqDto;
import com.guCoding.carrotMarket.dto.user.UserRespDto;
import com.guCoding.carrotMarket.dto.user.UserRespDto.JoinRespDto;
import com.guCoding.carrotMarket.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinRespDto 회원가입(JoinReqDto joinReqDto) {

        boolean isPresent = true;
        JoinRespDto joinRespDto = null;
        while (isPresent) {
            Random random = new Random();
            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            int length = 6;  // 조합된 문자열의 길이

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                int index = random.nextInt(characters.length());
                sb.append(characters.charAt(index));
            }
            String randomString = sb.toString();
            String identifier = joinReqDto.getNickname() + "#" + randomString;
            Optional<User> userPhoneOP = userRepository.findByPhoneNumber(joinReqDto.getPhoneNumber());
            if (userPhoneOP.isPresent()) {
                throw new CustomApiException("이미 등록된 전화번호 입니다.");
            }
            Optional<User> userIdentityOP = userRepository.findByIdentifier(identifier); //닉네임중복되면서 태그값 달리할 수 없어서
            if (!userIdentityOP.isPresent()) {
                isPresent = false;
                User userPS = userRepository.save(joinReqDto.toEntity(bCryptPasswordEncoder, identifier));
                joinRespDto = new JoinRespDto(userPS);
            }
        }
        return joinRespDto;

    }
}
