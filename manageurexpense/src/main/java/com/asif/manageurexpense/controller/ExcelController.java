//package com.asif.manageurexpense.controller;
//
//import com.asif.manageurexpense.service.ExcelService;
//import com.asif.manageurexpense.service.IncomeService;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//
//@RestController
//@RequestMapping("/excel")
//@RequiredArgsConstructor
//public class ExcelController {
//
//    private final ExcelService excelService;
//    private final IncomeService incomeService;
//
//    @GetMapping("/download/income")
//    public void downloadIncomeExcel(
//            HttpServletResponse response
//    ) throws IOException {
//
//        response.setContentType(
//                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
//        );
//
//        response.setHeader(
//                "Content-Disposition",
//                "attachment; filename=income.xlsx"
//        );
//
//        excelService.writeIncomesToExcel(
//                response.getOutputStream(),
//                incomeService.getCurrentMonthIncomesForCurrentUser()
//        );
//    }
//}

package com.asif.manageurexpense.controller;

import com.asif.manageurexpense.service.ExcelService;
import com.asif.manageurexpense.service.ExpenseService;
import com.asif.manageurexpense.service.IncomeService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/excel")
@RequiredArgsConstructor
public class ExcelController {

    private final ExcelService excelService;
    private final IncomeService incomeService;
    private final ExpenseService expenseService;


    // =========================
    // DOWNLOAD INCOME EXCEL
    // =========================

    @GetMapping("/download/income")
    public void downloadIncomeExcel(
            HttpServletResponse response
    ) throws IOException {

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        );

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=income.xlsx"
        );

        excelService.writeIncomesToExcel(
                response.getOutputStream(),
                incomeService.getCurrentMonthIncomesForCurrentUser()
        );
    }


    // =========================
    // DOWNLOAD EXPENSE EXCEL
    // =========================

    @GetMapping("/download/expense")
    public void downloadExpenseExcel(
            HttpServletResponse response
    ) throws IOException {

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        );

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=expense.xlsx"
        );

        excelService.writeExpensesToExcel(
                response.getOutputStream(),
                expenseService.getCurrentMonthExpensesForCurrentUser()
        );
    }
}
