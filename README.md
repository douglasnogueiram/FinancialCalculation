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

> [!NOTE]
> The current code was defined with a precision of 20 decimal places, which is directly responsible for the performance of the calculation. Most of the use cases demand this level of precision.

## Daily interest accrual calculation

Method to calculate the daily interest accrual for Loans. When you have an Accrual-based Accounting, income and expenses are recognized when accrued, not when the money is exchanged.

### Parameters:

| Parameter | Type | Description |
| --- | --- | --- |
| `dailyInterestRate` | numeric (with 20 decimal places) | Daily interest rate used in the finance flow |
| `accrualDate` | date (yyyy-mm-dd)| Desired date to get the accrual. It must be a date later than the first date of the finance flow. |
| `method` | Enum | `UNTIL_END_OF_PERIOD`: The calculation will last until the last date of the finance flow * `UNTIL_INTERMEDIATE_DATE`: The calculation process stops when the accrual date is reached | 
| `financialFlow` | Object | Finance flow, based on a list of dates and values that compose a typical loan. The item [0] is the amount of the loan and all others are the repayments. | 


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
The response depends on the chosen method:

* POST - http://localhost:8088/accrual/simple: only the result of the accrual for the desired date (end of period or accrual date) is shown:

```JSON
[
    {
        "idAccruedDay": 0,
        "accruedDay": "2024-09-01",
        "dailyInterestAmount": 0,
        "dailyAmmortizationAmount": 0,
        "dailyBalance": 50000.0000000
    },
    {
        "idAccruedDay": 61,
        "accruedDay": "2024-11-01",
        "dailyInterestAmount": 32.563482483414937102,
        "dailyAmmortizationAmount": 2651.1922386,
        "dailyBalance": 46697.155160511476827
    }
]
```

* POST - http://localhost:8088/accrual/detailed: all date are shown
