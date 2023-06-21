package com.guCoding.carrotMarket.config.auth;

import com.guCoding.carrotMarket.domain.user.User;
import com.guCoding.carrotMarket.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        User userPS = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new InternalAuthenticationServiceException("인증 실패"));
        return new LoginUser(userPS);
    }
}
