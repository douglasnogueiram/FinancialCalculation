package controller;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import model.AccrualBasis;
import model.InterestAccrualRequest;
import model.LoanRepaymentSchedule;

@RestController
public class InterestAccrualController {

    @PostMapping(value = "/interest-accrual/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public String calculateSummary(@RequestBody InterestAccrualRequest request) {
        BigDecimal            dailyInterestRate  = request.getDailyInterestRate();
        LocalDate             accrualDate        = request.getAccrualDate();
        AccrualBasis          accrualBasis       = request.getAccrualBasis();
        LoanRepaymentSchedule repaymentSchedule  = request.getRepaymentSchedule();

        InterestAccrualCalculation calculation = new InterestAccrualCalculation(
                dailyInterestRate, repaymentSchedule, accrualDate, accrualBasis);

        return calculation.calculateSummaryResult();
    }

    @PostMapping(value = "/interest-accrual/detailed", produces = MediaType.APPLICATION_JSON_VALUE)
    public String calculateDetailed(@RequestBody InterestAccrualRequest request) {
        BigDecimal            dailyInterestRate  = request.getDailyInterestRate();
        LocalDate             accrualDate        = request.getAccrualDate();
        AccrualBasis          accrualBasis       = request.getAccrualBasis();
        LoanRepaymentSchedule repaymentSchedule  = request.getRepaymentSchedule();

        InterestAccrualCalculation calculation = new InterestAccrualCalculation(
                dailyInterestRate, repaymentSchedule, accrualDate, accrualBasis);

        return calculation.calculateDetailedResult();
    }
}
