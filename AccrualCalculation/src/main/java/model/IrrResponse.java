package model;

import java.math.BigDecimal;

public class IrrResponse {

    private BigDecimal interestRate;

    public IrrResponse(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}