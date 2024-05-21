package com.mysite.expense.repository;

import com.mysite.expense.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // select * from tbl_users where email = ?
    Optional<User> findByEmail(String email);
}
