package com.asif.manageurexpense.controller;

import com.asif.manageurexpense.dto.ExpenseDto;
import com.asif.manageurexpense.dto.FilterDto;
import com.asif.manageurexpense.dto.IncomeDto;
import com.asif.manageurexpense.service.ExpenseService;
import com.asif.manageurexpense.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filter")
public class FilterController  {

    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<?> filterTransactions(@RequestBody FilterDto filter){

        LocalDate startDate = filter.getStartDate() != null ? filter.getStartDate() : LocalDate.MIN;
        LocalDate endDate  = filter.getEndDate() != null ? filter.getEndDate() : LocalDate.now();
        String keyWord = filter.getKeyword() != null ? filter.getKeyword() : "";
        String sortField = filter.getSortField() != null ? filter.getSortField() : "date";
        Sort.Direction direction = "desc".equalsIgnoreCase(filter.getSortField()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction , sortField);
        if("income".equals(filter.getType())){
            List<IncomeDto> incomes =incomeService.filterIncomes(startDate,endDate,keyWord,sort);
            return ResponseEntity.ok(incomes);
        } else if ("expense".equals(filter.getType())) {
            List<ExpenseDto> expense = expenseService.filterExpenses(startDate,endDate,keyWord,sort);
            return ResponseEntity.ok(expense);
        } else {
            return ResponseEntity.badRequest().body("Invalid type. Must be 'income' or 'expense'");
        }
    }
}
