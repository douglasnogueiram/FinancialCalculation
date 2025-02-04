package controller;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import model.FinancialFlow;
import model.IrrRequest;
import model.IrrResponse;
import model.irr;

@RestController
public class IrrController {
	
	@PostMapping(value = "/irr", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<IrrResponse> calculateIrr(@RequestBody IrrRequest request) {
		
		BigDecimal guess = request.getGuess();
		FinancialFlow finacialFlow = request.getFinancialFlow();
		BigDecimal interestRate = irr.irrCalculation(guess, finacialFlow);
		
		String response = interestRate.toString();
		
        if (response == null || response.isEmpty()) {
            // Retorna 204 No Content
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
		
        return new ResponseEntity<>(new IrrResponse(interestRate), HttpStatus.OK);
		
	}


}
