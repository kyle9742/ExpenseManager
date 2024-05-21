package com.mysite.expense.repository;

import com.mysite.expense.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Optional<Expense> findByExpenseId(String expenseId);

    //검색메소드 WHERE name LIKE %keyword% AND date = 시작일 between date = 종료일 AND user_id = ?
    List<Expense> findByNameContainingAndDateBetweenAndUserId(String keyword, Date start, Date end, Long id);

    // 로그인 유저의 비용만 검색 select * from tbl_expenses where user_id = ?
//    List<Expense> findByUserId(Date start, Date end, Long id);
    // 로그인 유저의 이번달 비용만 검색 select * from tbl_expenses where date = 시작일 between date = 종료일 AND user_id = ?
    List<Expense> findByDateBetweenAndUserId(Date start, Date end, Long id);
}
