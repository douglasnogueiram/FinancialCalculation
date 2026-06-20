package model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class IrrCalculator {

    public static BigDecimal calculate(BigDecimal initialRateEstimate, LoanRepaymentSchedule repaymentSchedule) {

        int maxIterations   = 100;
        BigDecimal accuracy = new BigDecimal(1E-20);
        MathContext RM      = new MathContext(20, RoundingMode.HALF_UP);

        BigDecimal x0 = initialRateEstimate;
        BigDecimal x1;

        LocalDate    startDate      = repaymentSchedule.getPaymentDueDate()[0];
        int          installmentCount = repaymentSchedule.getInstallmentReference().length;
        BigDecimal[] paymentAmounts = repaymentSchedule.getPaymentAmount();

        // Pre-compute day differences once — values never change between Newton-Raphson iterations
        int[] diffDaysArray = new int[installmentCount];
        for (int k = 1; k < installmentCount; k++) {
            diffDaysArray[k] = (int) ChronoUnit.DAYS.between(startDate, repaymentSchedule.getPaymentDueDate()[k]);
        }

        int i = 0;
        while (i < maxIterations) {

            BigDecimal onePlusX0 = BigDecimal.ONE.add(x0);

            // k=0: (1+x0)^0 = 1, contributes paymentAmounts[0] to fValue and 0 to fDerivative
            BigDecimal fValue      = paymentAmounts[0];
            BigDecimal fDerivative = BigDecimal.ZERO;

            for (int k = 1; k < installmentCount; k++) {
                int        diffDays     = diffDaysArray[k];
                BigDecimal potency      = onePlusX0.pow(diffDays, RM);
                BigDecimal paymentAmount = paymentAmounts[k];

                fValue      = fValue.add(paymentAmount.divide(potency, RM));
                fDerivative = fDerivative.add(
                    new BigDecimal(-diffDays).multiply(paymentAmount.divide(potency.multiply(onePlusX0, RM), RM)));
            }

            // Newton-Raphson step
            x1 = x0.subtract(fValue.divide(fDerivative, RM));

            if (x1.subtract(x0).abs().compareTo(accuracy) <= 0) {
                return x1.setScale(20, RoundingMode.HALF_UP);
            }

            x0 = x1;
            ++i;
        }

        // Maximum number of iterations exceeded — convergence failure
        return BigDecimal.valueOf(-1);
    }
}
