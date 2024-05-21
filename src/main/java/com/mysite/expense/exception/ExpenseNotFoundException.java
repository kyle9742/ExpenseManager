package com.mysite.expense.exception;

public class ExpenseNotFoundException extends RuntimeException {

    private String message;

    public ExpenseNotFoundException(String message) {
        this.message = message;
    }
}
