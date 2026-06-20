package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InterestAccrualRecord {

    private int        accrualDaySequence;
    private LocalDate  accruedDate;
    private BigDecimal accruedInterestAmount;
    private BigDecimal amortizationAmount;
    private BigDecimal outstandingBalance;

    public InterestAccrualRecord() {}

    public InterestAccrualRecord(int accrualDaySequence, LocalDate accruedDate,
            BigDecimal accruedInterestAmount, BigDecimal amortizationAmount, BigDecimal outstandingBalance) {
        this.accrualDaySequence   = accrualDaySequence;
        this.accruedDate          = accruedDate;
        this.accruedInterestAmount = accruedInterestAmount;
        this.amortizationAmount   = amortizationAmount;
        this.outstandingBalance   = outstandingBalance;
    }

    public int getAccrualDaySequence() { return accrualDaySequence; }
    public void setAccrualDaySequence(int accrualDaySequence) { this.accrualDaySequence = accrualDaySequence; }

    public LocalDate getAccruedDate() { return accruedDate; }
    public void setAccruedDate(LocalDate accruedDate) { this.accruedDate = accruedDate; }

    public BigDecimal getAccruedInterestAmount() { return accruedInterestAmount; }
    public void setAccruedInterestAmount(BigDecimal accruedInterestAmount) { this.accruedInterestAmount = accruedInterestAmount; }

    public BigDecimal getAmortizationAmount() { return amortizationAmount; }
    public void setAmortizationAmount(BigDecimal amortizationAmount) { this.amortizationAmount = amortizationAmount; }

    public BigDecimal getOutstandingBalance() { return outstandingBalance; }
    public void setOutstandingBalance(BigDecimal outstandingBalance) { this.outstandingBalance = outstandingBalance; }
}
