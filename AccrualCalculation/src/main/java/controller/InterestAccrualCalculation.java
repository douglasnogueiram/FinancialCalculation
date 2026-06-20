package controller;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import model.AccrualBasis;
import model.InterestAccrualRecord;
import model.LoanRepaymentSchedule;

public class InterestAccrualCalculation {

    private final BigDecimal            dailyInterestRate;
    private final LoanRepaymentSchedule repaymentSchedule;
    private final LocalDate             accrualDate;
    private final AccrualBasis          accrualBasis;
    private List<InterestAccrualRecord> accrualRecords = new ArrayList<>();

    private static final MathContext RM = new MathContext(20, RoundingMode.HALF_UP);

    public InterestAccrualCalculation(BigDecimal dailyInterestRate, LoanRepaymentSchedule repaymentSchedule,
            LocalDate accrualDate, AccrualBasis accrualBasis) {
        this.dailyInterestRate = dailyInterestRate;
        this.repaymentSchedule = repaymentSchedule;
        this.accrualDate       = accrualDate;
        this.accrualBasis      = accrualBasis;
    }

    public void calculate() {
        long startTime = System.currentTimeMillis();

        Map<LocalDate, BigDecimal> repayments = IntStream
                .range(1, repaymentSchedule.getInstallmentReference().length).boxed()
                .collect(Collectors.toMap(
                        i -> repaymentSchedule.getPaymentDueDate()[i],
                        i -> repaymentSchedule.getPaymentAmount()[i]));

        LocalDate startDate = repaymentSchedule.getPaymentDueDate()[0];
        LocalDate endDate   = repaymentSchedule.getPaymentDueDate()[repaymentSchedule.getInstallmentReference().length - 1];

        validateAccrualDate(startDate, endDate);

        if (accrualBasis == AccrualBasis.INTERMEDIATE_DATE) {
            endDate = accrualDate;
        }

        long diffDays = ChronoUnit.DAYS.between(startDate, endDate);
        System.out.println("Day iterations: " + diffDays);

        BigDecimal previousBalance = repaymentSchedule.getPaymentAmount()[0];
        accrualRecords.add(new InterestAccrualRecord(0, startDate, BigDecimal.ZERO, BigDecimal.ZERO, previousBalance));

        for (int i = 1; i <= diffDays; i++) {
            LocalDate  currentDate           = startDate.plusDays(i);
            BigDecimal accruedInterestAmount = previousBalance.multiply(dailyInterestRate, RM);
            BigDecimal balance               = previousBalance.add(accruedInterestAmount, RM);
            BigDecimal amortizationAmount    = repayments.getOrDefault(currentDate, BigDecimal.ZERO);
            balance = balance.subtract(amortizationAmount, RM);

            accrualRecords.add(new InterestAccrualRecord(i, currentDate, accruedInterestAmount, amortizationAmount, balance));
            previousBalance = balance;
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Execution time (ms): " + (endTime - startTime));
    }

    public String calculateSummaryResult() {
        Map<LocalDate, BigDecimal> repayments = IntStream
                .range(1, repaymentSchedule.getInstallmentReference().length).boxed()
                .collect(Collectors.toMap(
                        i -> repaymentSchedule.getPaymentDueDate()[i],
                        i -> repaymentSchedule.getPaymentAmount()[i]));

        LocalDate startDate = repaymentSchedule.getPaymentDueDate()[0];
        LocalDate endDate   = repaymentSchedule.getPaymentDueDate()[repaymentSchedule.getInstallmentReference().length - 1];

        validateAccrualDate(startDate, endDate);

        if (accrualBasis == AccrualBasis.INTERMEDIATE_DATE) {
            endDate = accrualDate;
        }

        long diffDays = ChronoUnit.DAYS.between(startDate, endDate);

        // Opening record (day 0)
        InterestAccrualRecord firstRecord = new InterestAccrualRecord(
                0, startDate, BigDecimal.ZERO, BigDecimal.ZERO, repaymentSchedule.getPaymentAmount()[0]);

        // Iterate maintaining only previousBalance — avoids storing all daily records in memory
        BigDecimal previousBalance      = repaymentSchedule.getPaymentAmount()[0];
        BigDecimal accruedInterestAmount = BigDecimal.ZERO;
        BigDecimal amortizationAmount   = BigDecimal.ZERO;
        BigDecimal balance              = previousBalance;

        for (int i = 1; i <= diffDays; i++) {
            LocalDate currentDate = startDate.plusDays(i);
            accruedInterestAmount = previousBalance.multiply(dailyInterestRate, RM);
            balance               = previousBalance.add(accruedInterestAmount, RM);
            amortizationAmount    = repayments.getOrDefault(currentDate, BigDecimal.ZERO);
            balance               = balance.subtract(amortizationAmount, RM);
            previousBalance       = balance;
        }

        // Closing record (last day)
        InterestAccrualRecord lastRecord = new InterestAccrualRecord(
                (int) diffDays, endDate, accruedInterestAmount, amortizationAmount, balance);

        List<InterestAccrualRecord> result = new ArrayList<>();
        result.add(firstRecord);
        result.add(lastRecord);

        return JSONGenerator.generateAccrualJSON(result);
    }

    public String calculateDetailedResult() {
        calculate();
        return JSONGenerator.generateAccrualJSON(accrualRecords);
    }

    private void validateAccrualDate(LocalDate startDate, LocalDate endDate) {
        if (accrualDate.isBefore(startDate) || accrualDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Accrual date must be within the loan contract period.");
        }
    }
}
