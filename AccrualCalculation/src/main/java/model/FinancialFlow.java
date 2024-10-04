package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FinancialFlow {
	

private int[] idInstallment;
private LocalDate[] installmentDueDate;
private BigDecimal[] installmentValue;





// Construtor padrão
public FinancialFlow() {
}


public FinancialFlow(int[] idInstallment, LocalDate[] installmentDueDate, BigDecimal[] installmentValue) {
	super();
	this.idInstallment = idInstallment;
	this.installmentDueDate = installmentDueDate;
	this.installmentValue = installmentValue;

}




public int[] getIdInstallment() {
	return idInstallment;
}
public void setIdInstallment(int[] idInstallment) {
	this.idInstallment = idInstallment;
}
public LocalDate[] getInstallmentDueDate() {
	return installmentDueDate;
}
public void setInstallmentDueDate(LocalDate[] installmentDueDate) {
	this.installmentDueDate = installmentDueDate;
}
public BigDecimal[] getInstallmentValue() {
	return installmentValue;
}
public void setInstallmentValue(BigDecimal[] installmentValue) {
	this.installmentValue = installmentValue;
}





}
