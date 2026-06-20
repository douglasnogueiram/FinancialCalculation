package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LoanRepaymentSchedule {

    private int[]        installmentReference;
    private LocalDate[]  paymentDueDate;
    private BigDecimal[] paymentAmount;

    public LoanRepaymentSchedule() {}

    public LoanRepaymentSchedule(int[] installmentReference, LocalDate[] paymentDueDate, BigDecimal[] paymentAmount) {
        this.installmentReference = installmentReference;
        this.paymentDueDate       = paymentDueDate;
        this.paymentAmount        = paymentAmount;
    }

    public int[] getInstallmentReference() { return installmentReference; }
    public void setInstallmentReference(int[] installmentReference) { this.installmentReference = installmentReference; }

    public LocalDate[] getPaymentDueDate() { return paymentDueDate; }
    public void setPaymentDueDate(LocalDate[] paymentDueDate) { this.paymentDueDate = paymentDueDate; }

    public BigDecimal[] getPaymentAmount() { return paymentAmount; }
    public void setPaymentAmount(BigDecimal[] paymentAmount) { this.paymentAmount = paymentAmount; }
}
