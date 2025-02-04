package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AccrualRequest {
    private BigDecimal dailyInterestRate;
    private LocalDate accrualDate;
    private AccrualCalculationMethod method;
    private FinancialFlow financialFlow;  // Certifique-se de que FinancialFlow está bem definido

    // Getters e Setters
    public BigDecimal getDailyInterestRate() {
        return dailyInterestRate;
    }

    public void setDailyInterestRate(BigDecimal dailyInterestRate) {
        this.dailyInterestRate = dailyInterestRate;
    }

    public LocalDate getAccrualDate() {
        return accrualDate;
    }

    public void setAccrualDate(LocalDate accrualDate) {
        this.accrualDate = accrualDate;
    }

    public AccrualCalculationMethod getMethod() {
        return method;
    }

    public void setMethod(AccrualCalculationMethod method) {
        this.method = method;
    }

    public FinancialFlow getFinancialFlow() {
        return financialFlow;
    }

    public void setFinancialFlow(FinancialFlow financialFlow) {
        this.financialFlow = financialFlow;
    }
}

