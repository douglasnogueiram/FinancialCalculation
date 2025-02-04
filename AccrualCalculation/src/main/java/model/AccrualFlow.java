package model;

import java.math.BigDecimal;
import java.time.LocalDate;


public class AccrualFlow {

	private int idAccruedDay;
	private LocalDate accruedDay;
	private BigDecimal dailyInterestAmount;
	private BigDecimal dailyAmmortizationAmount;
	private BigDecimal dailyBalance;
	
	
	
	
	
	public int getIdAccruedDay() {
		return idAccruedDay;
	}
	public void setIdAccruedDay(int idAccruedDay) {
		this.idAccruedDay = idAccruedDay;
	}
	public LocalDate getAccruedDay() {
		return accruedDay;
	}
	public void setAccruedDay(LocalDate accruedDay) {
		this.accruedDay = accruedDay;
	}
	public BigDecimal getDailyInterestAmount() {
		return dailyInterestAmount;
	}
	public void setDailyInterestAmount(BigDecimal dailyInterestAmount) {
		this.dailyInterestAmount = dailyInterestAmount;
	}
	public BigDecimal getDailyAmmortizationAmount() {
		return dailyAmmortizationAmount;
	}
	public void setDailyAmmortizationAmount(BigDecimal dailyAmmortizationAmount) {
		this.dailyAmmortizationAmount = dailyAmmortizationAmount;
	}
	public BigDecimal getDailyBalance() {
		return dailyBalance;
	}
	public void setDailyBalance(BigDecimal dailyBalance) {
		this.dailyBalance = dailyBalance;
	}
	
	
	
	
}
