package com.mysite.expense.service;

import com.mysite.expense.dto.ExpenseDTO;
import com.mysite.expense.dto.ExpenseFilterDTO;
import com.mysite.expense.entity.Expense;
import com.mysite.expense.repository.ExpenseRepository;
import com.mysite.expense.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expRepo;
    private final ModelMapper modelMapper;

    //엔티티 => DTO 변환 (다른 메서드에서만 사용 변환용)
    private ExpenseDTO mapToDTO(Expense expense) {
        ExpenseDTO expenseDTO = modelMapper.map(expense, ExpenseDTO.class);
        expenseDTO.setDateString(DateTimeUtil.convertDateToString(expenseDTO.getDate()));
        return expenseDTO;
    }
    // DTO => Entity 변환
    private Expense mapToEntity(ExpenseDTO expenseDTO) throws ParseException {
        Expense expense = modelMapper.map(expenseDTO, Expense.class);
        // expenseId 유니크 문자열 입력 (자바 유틸 UUID 사용)
        if(expenseDTO.getId() == null) {
            expense.setExpenseId(UUID.randomUUID().toString());
        }
        // 문자열날짜 => 날짜 변환
        expense.setDate(DateTimeUtil.convertStringToDate(expenseDTO.getDateString()));
        return expense;
    }


    //모든 비용 DTO 리스트를 가져오는 서비스
    public List<ExpenseDTO> getAllExpenses() {
        List<Expense> list = expRepo.findAll(); //엔티티 리스트
        List<ExpenseDTO> listDTO = list.stream()
                .map((expense) -> mapToDTO(expense))
                .collect(Collectors.toList());
        return listDTO;
    }

    public ExpenseDTO saveExpense(ExpenseDTO expenseDTO) throws ParseException {
        //1. DTO => Entity
        Expense expense = mapToEntity(expenseDTO);
        //2. DB에 저장 (id 있을경우에는 업데이트)
        expense = expRepo.save(expense);
        //3. Entity => DTO
        return mapToDTO(expense);
    }

    //삭제 서비스
    public void deleteExpense(String expenseId) {
        Expense expense = getExpenseById(expenseId);
        expRepo.delete(expense); //삭제하기
    }

    //리팩토링
    private Expense getExpenseById(String expenseId) {
        return expRepo.findByExpenseId(expenseId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 아이템을 찾을수 없습니다."));
    }

    //expenseId로 수정할 expense를 찾아 DTO 로 리턴
    public ExpenseDTO getExpense(String expenseId) {
        Expense expense = getExpenseById(expenseId);
        ExpenseDTO expenseDTO  = mapToDTO(expense);
        //날짜를 => Form 입력시 날짜는 2024-5-06
        expenseDTO.setDateString(DateTimeUtil.convertDateToInput(expense.getDate()));
        return expenseDTO;
    }

    //키워드 검색 결과 리스트
    public List<ExpenseDTO> getFilterExpenses(ExpenseFilterDTO expenseFilterDTO) throws ParseException {
        String keyword = expenseFilterDTO.getKeyword();
        String sortBy = expenseFilterDTO.getSortBy();
        String startString = expenseFilterDTO.getStartDate();
        String endString = expenseFilterDTO.getEndDate();
        //시작일과 종료일을 sql 날짜로 변환 (만약 없으면 최초일부터 현재까지 적용)
        Date startDay = !startString.isEmpty() ? DateTimeUtil.convertStringToDate(startString) : new Date(0);
        Date endDay = !endString.isEmpty() ? DateTimeUtil.convertStringToDate(endString) : new Date(System.currentTimeMillis());

        List<Expense> list = expRepo.findByNameContainingAndDateBetween(keyword, startDay, endDay);
        List<ExpenseDTO> filterlist = list.stream()
                .map((exp) -> mapToDTO(exp))
                .collect(Collectors.toList());

        // 정렬방법에 따라서 정렬하기
        if(sortBy.equals("date")) {
            filterlist.sort(((o1, o2) -> o2.getDate().compareTo(o1.getDate())));
        } else {
            filterlist.sort(((o1, o2) -> o2.getAmount().compareTo(o1.getAmount())));
        }
        return filterlist;
    }

    public Long totalExpenses(List<ExpenseDTO> list) {
        Long sum = list.stream()
                .map(x -> x.getAmount())
                .reduce(0L, Long::sum);
        return sum;
    }
}
