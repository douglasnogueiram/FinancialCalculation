# FinancialCalculation
Java package with some examples for financial calculations - Internal return rate and daily interest accruals

## IRR Calculation based on the Newton-Raphson algorithm:

* It's necessary to inform a Financial flow, with the repayments/installments and dates
* A guess must be informed as an approximate interest rate to run the calculation
* Method: POST - http://localhost:8088/irr
* An example of API request is shown here:

```JSON
  {
    "guess": 0.0001,
    "financialFlow": {
        "idInstallment": [
            0,
            1,
            2,
            3,
            4,
            5,
            6
        ],
        "installmentDueDate": [
            "2024-09-01",
            "2024-10-01",
            "2024-11-01",
            "2024-12-01",
            "2025-01-01",
            "2025-02-01",
            "2025-03-01"
        ],
        "installmentValue": [
            -5000.00000000000,
            1447.95455883595,
            1447.95455883595,
            1447.95455883595,
            1447.95455883595,
            1447.95455883595,
            1447.95455883595
        ]
    }
} 
```

The response to this request is:

```JSON
{
    "interestRate": 0.00560298075229797650
}
```

## Daily interest accrual calculation

```JSON
{
  "dailyInterestRate": 0.000660305482286683,
  "accrualDate": "2024-10-01",
  "method": "UNTIL_END_OF_PERIOD",
  "financialFlow": {
    "idInstallment": [0,1,2],
    "installmentDueDate": [
        "2024-09-01",
        "2024-10-01",
        "2024-11-01"
        ],
    "installmentValue": [
        50000.0000000,
        2651.1922386,
        2651.1922386
        ]
  }
}
```

