package controller;


import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


import model.AccrualCalculationMethod;
import model.AccrualFlow;
import model.FinancialFlow;

public class AccrualCalculation {

	private final BigDecimal dailyInterestRate;
	private final FinancialFlow financialFlow;
	private final LocalDate accrualDate;
	private Enum<?> accrualCalculationMethod;
	private List<AccrualFlow> calculatedAccrual = new ArrayList<AccrualFlow>();

	private static final MathContext RM = new MathContext(20, RoundingMode.HALF_UP);

	public AccrualCalculation(BigDecimal dailyInterestRate, FinancialFlow financialFlow, LocalDate accrualDate,
			AccrualCalculationMethod accrualCalculationMethod) {
		this.dailyInterestRate = dailyInterestRate;
		this.financialFlow = financialFlow;
		this.accrualDate = accrualDate;
		this.accrualCalculationMethod = accrualCalculationMethod;
	}

	
	
	
	public void calculate() {


		long tempoInicial = System.currentTimeMillis();

		//0 - Gerar um hashmap para as datas/valores de pagamentos existentes

		Map<LocalDate, BigDecimal> repayments = IntStream.range(1, financialFlow.getIdInstallment().length).boxed()
				.collect(Collectors.toMap(i -> financialFlow.getInstallmentDueDate()[i],
						i -> financialFlow.getInstallmentValue()[i]));

	
		
		
		//1 - Calcular a diferença entre datas para saber o tamanho das iterações

		LocalDate startDate = financialFlow.getInstallmentDueDate()[0];
		LocalDate endDate = financialFlow.getInstallmentDueDate()[financialFlow.getIdInstallment().length - 1];

		if (accrualDate.isBefore(startDate) || accrualDate.isAfter(endDate)) {
			throw new IllegalArgumentException("A data de cálculo deve estar dentro do período do contrato.");
		}

		if (accrualCalculationMethod == AccrualCalculationMethod.UNTIL_INTERMEDIATE_DATE) {
			endDate = accrualDate;
		}

		long diffDays = ChronoUnit.DAYS.between(startDate, endDate);
		System.out.println(diffDays);



		// 2 - Iteração pela quantidade de dias
		for (int i = 0; i <= diffDays; i++) {

			AccrualFlow accrualFlow = new AccrualFlow();
			LocalDate currentDate = startDate.plusDays(i);

			BigDecimal balance, dailyInterestAmount, amortization;

			//O item 0 é o valor presente
			if (i == 0) {

				balance = financialFlow.getInstallmentValue()[0];
				dailyInterestAmount = BigDecimal.ZERO;
				amortization = BigDecimal.ZERO;

				accrualFlow.setIdAccruedDay(i);
				accrualFlow.setAccruedDay(currentDate);
				accrualFlow.setDailyInterestAmount(dailyInterestAmount);
				accrualFlow.setDailyAmmortizationAmount(amortization);
				accrualFlow.setDailyBalance(balance);

			} else {

				dailyInterestAmount = calculatedAccrual.get(i - 1).getDailyBalance().multiply(dailyInterestRate, RM);

				balance = calculatedAccrual.get(i - 1).getDailyBalance().add(dailyInterestAmount, RM);
				;

				// verificar se existe valor para amortizar para a data informada
				amortization = repayments.getOrDefault(currentDate, BigDecimal.ZERO);
				balance = balance.subtract(amortization, RM);

				accrualFlow.setIdAccruedDay(i);
				accrualFlow.setAccruedDay(currentDate);
				accrualFlow.setDailyInterestAmount(dailyInterestAmount);
				accrualFlow.setDailyAmmortizationAmount(amortization);
				accrualFlow.setDailyBalance(balance);

			}
			;

			calculatedAccrual.add(accrualFlow);

		}

		Long tempoFinal = System.currentTimeMillis();
		System.out.println("Tempo de execução (ms): " + (tempoFinal - tempoInicial));

		
	}
	

	public String calculateAccrualSimplifiedResult() {
		
		calculate();
		
		List<AccrualFlow> finalCalculatedAccrual = new ArrayList<AccrualFlow>();

		finalCalculatedAccrual.add(calculatedAccrual.getFirst());
		finalCalculatedAccrual.add(calculatedAccrual.getLast());
		
		String jsonOutput = JSONGenerator.generateAccrualFlowJSON(finalCalculatedAccrual);
		return jsonOutput;
	}
	
	
	public String calculateAccrualDetaileddResult() {
		
		calculate();
		
		String jsonOutput = JSONGenerator.generateAccrualFlowJSON(calculatedAccrual);
		return jsonOutput;
	}
	


}
