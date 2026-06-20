package controller;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import model.IrrCalculationRequest;
import model.IrrCalculationResponse;
import model.IrrCalculator;
import model.LoanRepaymentSchedule;

@RestController
public class IrrController {

    @PostMapping(value = "/irr", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IrrCalculationResponse> calculateIrr(@RequestBody IrrCalculationRequest request) {
        BigDecimal            initialRateEstimate = request.getInitialRateEstimate();
        LoanRepaymentSchedule repaymentSchedule   = request.getRepaymentSchedule();

        BigDecimal effectiveRate = IrrCalculator.calculate(initialRateEstimate, repaymentSchedule);

        if (effectiveRate.compareTo(BigDecimal.valueOf(-1)) == 0) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(new IrrCalculationResponse(effectiveRate), HttpStatus.OK);
    }
}
