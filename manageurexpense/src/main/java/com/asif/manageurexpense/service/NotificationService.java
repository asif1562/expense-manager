package com.asif.manageurexpense.service;

import com.asif.manageurexpense.dto.ExpenseDto;
import com.asif.manageurexpense.entity.ExpenseEntity;
import com.asif.manageurexpense.entity.ProfileEntity;
import com.asif.manageurexpense.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final ExpenseService expenseService;
    @Value("${manager.expense.frontend.url}")
    private String frontendUrl;


    //@Scheduled(cron = "0 * * * * *", zone = "IST")
   @Scheduled(cron = "0 0 22 * * *", zone = "IST")
    public void sendDailyIncomeExpenseReminder() {

        log.info("Job started: sendDailyIncomeExpenseReminder()");

        List<ProfileEntity> profiles = profileRepository.findAll();

        for (ProfileEntity profile : profiles) {

            String body = "Hi " + profile.getFullName() + ",\n\n"
                    + "This is a friendly reminder to add your income and expenses for today.\n\n"
                    + "Click the link below to open Money Manager:\n"
                    + frontendUrl + "\n\n"
                    + "Keep your finances updated!\n\n"
                    + "Regards,\n"
                    + "Money Manager Team";

            emailService.sendEmail(
                    profile.getEmail(),
                    "Daily reminder: Add your income and expenses",
                    body);
        }
        log.info("Job completed: sendDailyIncomeExpenseReminder()");
    }
    //@Scheduled(cron = "0 0 23 * * *", zone = "IST")
//    @Scheduled(cron = "0 * * * * *", zone = "IST")
//    public void sendDailyExpenseSummery(){
//       log.info("Job started : sendDailyExpenseSummery()");
//        List<ProfileEntity> profiles = profileRepository.findAll();
//        for (ProfileEntity profile : profiles){
//            List<ExpenseDto> todayExpense  =expenseService.getExpenseForUserOnDate(profile.getId(),LocalDate.now());
//            if(!todayExpense.isEmpty()){
//                StringBuilder table = new StringBuilder();
//                table.append("<table style='border-collapse;width:100%;'>");
//                table.append("<tr style='background-color:#f2f2f2;'>");
//
//                table.append("<th style='border:1px solid #ddd;padding:8px;'>Name</th>");
//                table.append("<th style='border:1px solid #ddd;padding:8px;'>Amount</th>");
//                table.append("<th style='border:1px solid #ddd;padding:8px;'>Category</th>");
//                table.append("<th style='border:1px solid #ddd;padding:8px;'>Date</th>");
//
//                table.append("</tr>");
//
//                int i =1;
//                for (ExpenseDto expenseDto : todayExpense){
//
//                }
//            }
//        }
//    }

    @Scheduled(cron = "0 0 21 * * *", zone = "IST")
    public void sendDailyExpenseSummary() {

        log.info("Job started: sendDailyExpenseSummary()");

        List<ProfileEntity> profiles = profileRepository.findAll();

        for (ProfileEntity profile : profiles) {

            List<ExpenseDto> todaysExpenses =
                    expenseService.getExpenseForUserOnDate(
                            profile.getId(),
                            LocalDate.now()
                    );

            log.info("User: {}, Today's expenses: {}",
                    profile.getEmail(),
                    todaysExpenses.size());

            if (todaysExpenses.isEmpty()) {
                continue;
            }

            StringBuilder table = new StringBuilder();

            // Table start
            table.append("<table style='border-collapse:collapse;width:100%;'>");

            // Table header
            table.append("<tr style='background-color:#f2f2f2;'>")
                    .append("<th style='border:1px solid #ddd;padding:8px;'>S.No</th>")
                    .append("<th style='border:1px solid #ddd;padding:8px;'>Name</th>")
                    .append("<th style='border:1px solid #ddd;padding:8px;'>Amount</th>")
                    .append("<th style='border:1px solid #ddd;padding:8px;'>Category</th>")
                    .append("</tr>");

            // Expense rows
            int count = 1;

            for (ExpenseDto expense : todaysExpenses) {

                String category = expense.getCategoryId() != null
                        ? expense.getCategoryName()
                        : "N/A";

                table.append("<tr>")
                        .append("<td style='border:1px solid #ddd;padding:8px;'>")
                        .append(count++)
                        .append("</td>")

                        .append("<td style='border:1px solid #ddd;padding:8px;'>")
                        .append(expense.getName())
                        .append("</td>")

                        .append("<td style='border:1px solid #ddd;padding:8px;'>")
                        .append(expense.getAmount())
                        .append("</td>")

                        .append("<td style='border:1px solid #ddd;padding:8px;'>")
                        .append(category)
                        .append("</td>")

                        .append("</tr>");
            }

            // Table end
            table.append("</table>");

            String body =
                    "Hi " + profile.getFullName() + ",<br/><br/>" +
                            "Here is a summary of your expenses for today:<br/><br/>" +
                            table +
                            "<br/><br/>" +
                            "Best Regards,<br/>" +
                            "Money Manager Team.";

            log.info("Sending expense summary email to {}", profile.getEmail());
            emailService.sendEmail(profile.getEmail(), "Summary of your today's expenses", body);
        }

        log.info("Job finished: sendDailyExpenseSummary()");
    }

}
