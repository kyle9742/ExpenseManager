package com.mysite.expense.repository;

import com.mysite.expense.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Optional<Expense> findByExpenseId(String expenseId);
    //검색메소드 WHERE name LIKE %keyword%
    List<Expense> findByNameContaining(String keyword);
}
