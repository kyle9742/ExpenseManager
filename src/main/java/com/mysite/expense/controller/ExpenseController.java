package com.mysite.expense.controller;

import com.mysite.expense.dto.ExpenseDTO;
import com.mysite.expense.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ExpenseController {

    // 테스트 리스트
    private static List<ExpenseDTO> list = new ArrayList<>();

    private final ExpenseService expService;

    @GetMapping("/expenses")
    public String showExpenseList(Model model) {
        model.addAttribute("expenses", expService.getAllExpenses());
        return "expenses-list";
    }
}
