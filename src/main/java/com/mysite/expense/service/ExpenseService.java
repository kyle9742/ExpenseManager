package com.mysite.expense.service;

import com.mysite.expense.dto.ExpenseDTO;
import com.mysite.expense.entity.Expense;
import com.mysite.expense.repository.ExpenseRepository;
import com.mysite.expense.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    
    private final ExpenseRepository expRepo;
    private final ModelMapper modelMapper;

    // 모든 비용 리스트를 찾는 서비스
    public List<Expense> findAllExpenses() {
        return expRepo.findAll();
    }

    // Entity => DTO 변환 (다른 메소드에서만 사용 변환용)
    private ExpenseDTO mapToDTO(Expense expense) {
        ExpenseDTO expenseDTO = modelMapper.map(expense, ExpenseDTO.class);
        expenseDTO.setDateString(DateTimeUtil.convertDateToString(expenseDTO.getDate()));
        return expenseDTO;
    }

    // DTO => Entity
    private Expense mapToEntity(ExpenseDTO expenseDTO) throws ParseException {
        Expense expense = modelMapper.map(expenseDTO, Expense.class);
        // expenseId 유니크 문자열 입력 (자바 유틸 UUID 사용)
        expense.setExpenseId(UUID.randomUUID().toString());
        // 문자열 날짜 => 날짜
        expense.setDate(DateTimeUtil.convertStringToDate(expenseDTO.getDateString()));
        return expense;
    }

    // 모든 비용 DTO 리스트를 가져오는 서비스
    public List<ExpenseDTO> getAllExpenses() {
        List<Expense> list = expRepo.findAll(); // Entity 리스트
        List<ExpenseDTO> listDTO = list.stream()
                .map((expense) -> mapToDTO(expense))
                .collect(Collectors.toList());
        return listDTO;
    }

    public ExpenseDTO saveExpense(ExpenseDTO expenseDTO) throws ParseException {
        // 1. DTO => Entity
        Expense expense = mapToEntity(expenseDTO);
        // 2. DB 에 저장
        expense = expRepo.save(expense);
        // 3. Entity => DTO
        return mapToDTO(expense);
    }
}
