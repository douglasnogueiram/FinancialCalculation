package model;

import java.math.BigDecimal;

public class IrrCalculationRequest {

    private BigDecimal           initialRateEstimate;
    private LoanRepaymentSchedule repaymentSchedule;

    public BigDecimal getInitialRateEstimate() { return initialRateEstimate; }
    public void setInitialRateEstimate(BigDecimal initialRateEstimate) { this.initialRateEstimate = initialRateEstimate; }

    public LoanRepaymentSchedule getRepaymentSchedule() { return repaymentSchedule; }
    public void setRepaymentSchedule(LoanRepaymentSchedule repaymentSchedule) { this.repaymentSchedule = repaymentSchedule; }
}
