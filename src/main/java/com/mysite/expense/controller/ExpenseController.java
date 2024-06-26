package com.mysite.expense.controller;

import com.mysite.expense.dto.ExpenseDTO;
import com.mysite.expense.dto.ExpenseFilterDTO;
import com.mysite.expense.service.ExpenseService;
import com.mysite.expense.util.DateTimeUtil;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expService;

    @GetMapping("/expenses")
    public String showExpenseList(Model model) {
        List<ExpenseDTO> list = expService.getAllExpenses();
        model.addAttribute("expenses", list);
        model.addAttribute("filter",
                new ExpenseFilterDTO(DateTimeUtil.getCurrentMonthStartDate(), DateTimeUtil.getCurrentMonthEndDate()));
        Long total = expService.totalExpenses(list);
        model.addAttribute("total", total);
        return "expenses-list";
    }

    @GetMapping("/createExpense")
    public String createExpense(Model model) {
        model.addAttribute("expense", new ExpenseDTO());
        return "expense-form";
    }

    @PostMapping("/saveOrUpdateExpense")
    public String saveOrUpdateExpense(@Valid @ModelAttribute("expense") ExpenseDTO expenseDTO,
                                      BindingResult result) throws ParseException {
        System.out.println("입력한 expenseDTO = " + expenseDTO);
        if (result.hasErrors()) {
            return "expense-form";
        }
        expService.saveExpense(expenseDTO);
        return "redirect:/expenses";
    }

    //삭제하기
    @GetMapping("/deleteExpense")
    public String deleteExpense(@RequestParam("id") String expenseId) {
        System.out.println("삭제 번호: " + expenseId);
        expService.deleteExpense(expenseId);
        return "redirect:/expenses";
    }

    //수정페이지 보이기
    @GetMapping("/updateExpense")
    public String updateExpense(@RequestParam("id") String expenseId, Model model) {
        System.out.println("업데이트 아이템 : " + expenseId);
        model.addAttribute("expense", expService.getExpense(expenseId));
        return "expense-form";
    }
}
