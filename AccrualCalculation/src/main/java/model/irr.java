package model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;



public class irr {
	
    
    public static BigDecimal irrCalculation(BigDecimal guess, FinancialFlow financialFlow) {
        
    	//long initialTime = System.currentTimeMillis();
    	
    	int maxIterationCount = 100;
        BigDecimal absoluteAccuracy = new BigDecimal(1E-20);

        BigDecimal x0 = guess;
        BigDecimal x1;
        MathContext RM = new MathContext(20, RoundingMode.HALF_UP);
        
		LocalDate startDate = financialFlow.getInstallmentDueDate()[0];
		
		int quantityOfInstallments = financialFlow.getIdInstallment().length;

        int i = 0;
        while (i < maxIterationCount) {
        	//System.out.println("Iteração" + i);

            // the value of the function (NPV) and its derivate can be calculated in the same loop
            BigDecimal fValue = BigDecimal.ZERO;
            BigDecimal fDerivative = BigDecimal.ZERO;
            
            BigDecimal onePlusX0 = BigDecimal.ONE.add(x0);
             
            for (int k = 0; k < quantityOfInstallments; k++) {
            	
        		
            	LocalDate currentDate = financialFlow.getInstallmentDueDate()[k];
            	long diffDays = ChronoUnit.DAYS.between(startDate, currentDate);
            	int diffDaysInt = Math.toIntExact(diffDays);
            	
            	BigDecimal potency = onePlusX0.pow(diffDaysInt, RM);
            	BigDecimal potencyMultiply = potency.multiply(onePlusX0, RM);
            	//BigDecimal potencyMultiply = onePlusX0.pow(diffDaysInt + 1, RM);
            	
            	BigDecimal installmentValue = financialFlow.getInstallmentValue()[k];
            
            	
                fValue = fValue.add(installmentValue.divide(potency,RM));               
                fDerivative = fDerivative.add(new BigDecimal(-diffDaysInt).multiply(installmentValue.divide(potencyMultiply,RM)));
            }

            // the essense of the Newton-Raphson Method
            x1 = x0.subtract(fValue.divide(fDerivative,RM));
            
            //System.out.println("X1: " + x1);
            //System.out.println("X0: " + x0);
            
            //System.out.println(x1.subtract(x0).abs().compareTo(absoluteAccuracy));

            if (x1.subtract(x0).abs().compareTo(absoluteAccuracy) <= 0) {
            	//System.out.println("Número de iterações BigDecimal: " + i);
            	//Long endTime = System.currentTimeMillis();
                //System.out.println("Tempo de execução (ms): " + (endTime - initialTime));
                return x1.setScale(20, RoundingMode.HALF_UP);
            }

            x0 = x1;
            ++i;
        }
        
        // maximum number of iterations is exceeded
        return BigDecimal.valueOf(-1);

    }
    

}
