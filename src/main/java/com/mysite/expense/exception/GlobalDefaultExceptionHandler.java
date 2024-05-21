package com.mysite.expense.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// 모든 컨트롤러에 적용됨
@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    // (예외종류) 발생시 메소드 실행
    @ExceptionHandler(ExpenseNotFoundException.class)
    public String handleExpenseNotFoundException(ExpenseNotFoundException ex, Model model) {
        model.addAttribute("notfound", true);
        model.addAttribute("message", ex.getMessage());
        return "response";
    }

    // 그외의 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest request, Exception ex, Model model) {
        model.addAttribute("serverError", true);
        model.addAttribute("message", ex.getMessage());
        return "response";
    }
}
