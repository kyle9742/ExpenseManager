package com.mysite.expense;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ExpenseApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpenseApplication.class, args);
    }

    //사용할 모델매퍼 객체를 빈 등록
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
