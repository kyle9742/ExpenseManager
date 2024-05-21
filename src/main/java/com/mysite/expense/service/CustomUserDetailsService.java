package com.mysite.expense.service;

import com.mysite.expense.entity.User;
import com.mysite.expense.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository uRepo;

    // 시큐리티가 로그인하기 위한 메서드 완성시켜야함
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = uRepo.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(email + "해당 이메일의 유저가 없습니다."));

        // 시큐리티의 유저는 권한도 필요
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>() // 권한(유저롤, 관리자롤 등등)
        );
    }
}
