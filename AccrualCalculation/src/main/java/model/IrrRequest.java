package model;

import java.math.BigDecimal;

public class IrrRequest {
	
	private BigDecimal guess;
	private FinancialFlow financialFlow;
	
	
	public BigDecimal getGuess() {
		return guess;
	}
	public void setGuess(BigDecimal guess) {
		this.guess = guess;
	}
	public FinancialFlow getFinancialFlow() {
		return financialFlow;
	}
	public void setFinancialFlow(FinancialFlow financialFlow) {
		this.financialFlow = financialFlow;
	}
	

}
