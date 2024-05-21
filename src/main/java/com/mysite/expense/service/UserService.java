package com.mysite.expense.service;

import com.mysite.expense.dto.UserDTO;
import com.mysite.expense.entity.User;
import com.mysite.expense.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    // 입력받은 userDTO 를 변환하여 DB 에 저장
    public void save(UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user = mapToEntity(userDTO);
        user.setUserId(UUID.randomUUID().toString()); // 중복되지않는 userID 생성
        userRepo.save(user);
    }

    // 로그인된 유저 정보 가져오기
    public User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loginUserEmail = auth.getName();
        return userRepo.findByEmail(loginUserEmail).orElseThrow(()->
                new UsernameNotFoundException("이메일을 찾을수 없습니다"));
    }

    // DTO => Entity
    private User mapToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
