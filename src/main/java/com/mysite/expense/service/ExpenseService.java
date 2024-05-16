package com.mysite.expense.service;

import com.mysite.expense.dto.ExpenseDTO;
import com.mysite.expense.entity.Expense;
import com.mysite.expense.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
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

    // Entity => DTO 변환
    private ExpenseDTO mapToDTO(Expense expense) {
        ExpenseDTO expenseDTO = modelMapper.map(expense, ExpenseDTO.class);
        return expenseDTO;
    }

    // 모든 비용 DTO 리스트를 리턴
    public List<ExpenseDTO> getAllExpenses() {
        List<Expense> list = expRepo.findAll(); // Entity 리스트
        List<ExpenseDTO> listDTO = list.stream()
                .map((expense) -> mapToDTO(expense))
                .collect(Collectors.toList());
        return listDTO;
    }
}
