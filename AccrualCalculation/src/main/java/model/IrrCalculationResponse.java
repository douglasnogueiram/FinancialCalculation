package model;

import java.math.BigDecimal;

public class IrrCalculationResponse {

    private BigDecimal effectiveInterestRate;

    public IrrCalculationResponse(BigDecimal effectiveInterestRate) {
        this.effectiveInterestRate = effectiveInterestRate;
    }

    public BigDecimal getEffectiveInterestRate() { return effectiveInterestRate; }
    public void setEffectiveInterestRate(BigDecimal effectiveInterestRate) { this.effectiveInterestRate = effectiveInterestRate; }
}
