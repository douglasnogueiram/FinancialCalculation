package controller;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import model.AccrualCalculationMethod;
import model.AccrualRequest;
import model.FinancialFlow;

@RestController
public class AccrualCalculationController {

    @PostMapping(value = "/accrual/simple", produces = MediaType.APPLICATION_JSON_VALUE)
    public String calculateAccrualSimple(@RequestBody AccrualRequest request) {
        // Pega os dados do corpo da requisição (JSON) e faz o cálculo
        BigDecimal dailyInterestRate = request.getDailyInterestRate();
        LocalDate accrualDate = request.getAccrualDate();
        AccrualCalculationMethod method = request.getMethod();
        FinancialFlow financialFlow = request.getFinancialFlow();

        // Cria uma instância de AccrualCalculation com os dados fornecidos
        AccrualCalculation accrualCalculation = new AccrualCalculation(dailyInterestRate, financialFlow, accrualDate, method);
        
        // Retorna o resultado do cálculo (em formato JSON)
        return accrualCalculation.calculateAccrualSimplifiedResult();
    }
    
    @PostMapping(value = "/accrual/detailed", produces = MediaType.APPLICATION_JSON_VALUE)
    public String calculateAccrualDetailed(@RequestBody AccrualRequest request) {
        // Pega os dados do corpo da requisição (JSON) e faz o cálculo
        BigDecimal dailyInterestRate = request.getDailyInterestRate();
        LocalDate accrualDate = request.getAccrualDate();
        AccrualCalculationMethod method = request.getMethod();
        FinancialFlow financialFlow = request.getFinancialFlow();

        // Cria uma instância de AccrualCalculation com os dados fornecidos
        AccrualCalculation accrualCalculation = new AccrualCalculation(dailyInterestRate, financialFlow, accrualDate, method);
        
        // Retorna o resultado do cálculo (em formato JSON)
        return accrualCalculation.calculateAccrualDetaileddResult();
    }
    
    
 
}
