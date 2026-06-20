package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InterestAccrualRequest {

    private BigDecimal            dailyInterestRate;
    private LocalDate             accrualDate;
    private AccrualBasis          accrualBasis;
    private LoanRepaymentSchedule repaymentSchedule;

    public BigDecimal getDailyInterestRate() { return dailyInterestRate; }
    public void setDailyInterestRate(BigDecimal dailyInterestRate) { this.dailyInterestRate = dailyInterestRate; }

    public LocalDate getAccrualDate() { return accrualDate; }
    public void setAccrualDate(LocalDate accrualDate) { this.accrualDate = accrualDate; }

    public AccrualBasis getAccrualBasis() { return accrualBasis; }
    public void setAccrualBasis(AccrualBasis accrualBasis) { this.accrualBasis = accrualBasis; }

    public LoanRepaymentSchedule getRepaymentSchedule() { return repaymentSchedule; }
    public void setRepaymentSchedule(LoanRepaymentSchedule repaymentSchedule) { this.repaymentSchedule = repaymentSchedule; }
}
