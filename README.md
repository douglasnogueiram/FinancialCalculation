# FinancialCalculation

REST API for loan financial calculations aligned with the [BIAN](https://bian.org) (Banking Industry Architecture Network) data dictionary. Implements two core algorithms from the **Installment Loan** service domain:

- **IRR (Internal Rate of Return)** — Newton-Raphson method with 20-decimal precision
- **Interest Accrual** — Daily accrual calculation for accrual-based accounting

---

## Prerequisites

| Tool | Version |
|------|---------|
| Java | 17+ |
| Maven | 3.6+ |
| Docker | 20+ (optional) |

---

## Running

### With Docker (recommended)

```bash
cd AccrualCalculation
docker build -t financial-calculation .
docker run -p 8088:8088 financial-calculation
```

### Locally

```bash
cd AccrualCalculation
mvn spring-boot:run
```

The service starts on **port 8088**.

---

## API Reference

### POST `/irr`

Computes the daily **Effective Interest Rate** for a loan repayment schedule using the Newton-Raphson algorithm.

**Request body:**

```json
{
  "initialRateEstimate": 0.0001,
  "repaymentSchedule": {
    "installmentReference": [0, 1, 2, 3, 4, 5, 6],
    "paymentDueDate": [
      "2024-09-01",
      "2024-10-01",
      "2024-11-01",
      "2024-12-01",
      "2025-01-01",
      "2025-02-01",
      "2025-03-01"
    ],
    "paymentAmount": [
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

| Field | Type | Description |
|-------|------|-------------|
| `initialRateEstimate` | decimal | Starting guess for the Newton-Raphson iteration |
| `repaymentSchedule.installmentReference` | int[] | Sequential installment identifiers |
| `repaymentSchedule.paymentDueDate` | date[] (yyyy-MM-dd) | Due date for each installment. Index [0] is the loan disbursement date |
| `repaymentSchedule.paymentAmount` | decimal[] | Payment amount per installment. Index [0] must be negative (loan principal outflow) |

**Response:**

```json
{
  "effectiveInterestRate": 0.00560298075229797650
}
```

> **Note:** Precision is set to 20 decimal places. If the algorithm does not converge within 100 iterations, HTTP 422 is returned.

---

### POST `/interest-accrual/summary`

Returns only the **opening** and **closing** interest accrual records for the requested period — efficient for balance sheet snapshots.

**Request body:**

```json
{
  "dailyInterestRate": 0.000660305482286683,
  "accrualDate": "2024-10-01",
  "accrualBasis": "END_OF_PERIOD",
  "repaymentSchedule": {
    "installmentReference": [0, 1, 2],
    "paymentDueDate": [
      "2024-09-01",
      "2024-10-01",
      "2024-11-01"
    ],
    "paymentAmount": [
      50000.0000000,
      2651.1922386,
      2651.1922386
    ]
  }
}
```

| Field | Type | Description |
|-------|------|-------------|
| `dailyInterestRate` | decimal (20 d.p.) | Daily interest rate used in the repayment schedule |
| `accrualDate` | date (yyyy-MM-dd) | Target date for the accrual calculation. Must fall within the schedule period |
| `accrualBasis` | enum | `END_OF_PERIOD` — iterates through the full schedule end date · `INTERMEDIATE_DATE` — stops at `accrualDate` |
| `repaymentSchedule` | object | Loan repayment schedule. Index [0] is the principal disbursement; subsequent entries are repayment installments |

**Response:**

```json
[
  {
    "accrualDaySequence": 0,
    "accruedDate": "2024-09-01",
    "accruedInterestAmount": 0,
    "amortizationAmount": 0,
    "outstandingBalance": 50000.0000000
  },
  {
    "accrualDaySequence": 61,
    "accruedDate": "2024-11-01",
    "accruedInterestAmount": 32.563482483414937102,
    "amortizationAmount": 2651.1922386,
    "outstandingBalance": 46697.155160511476827
  }
]
```

---

### POST `/interest-accrual/detailed`

Returns an **accrual record for every day** in the calculation period. Same request body as `/interest-accrual/summary`.

**Response:** array of `InterestAccrualRecord` for each calendar day from the disbursement date through the end of the period.

| Response field | BIAN term | Description |
|---|---|---|
| `accrualDaySequence` | Accrual Day Sequence | Days elapsed since disbursement date |
| `accruedDate` | Accrued Date | Calendar date of this record |
| `accruedInterestAmount` | Accrued Interest Amount | Interest accrued on this specific day |
| `amortizationAmount` | Amortization Amount | Principal repaid on this day (0 on non-payment days) |
| `outstandingBalance` | Outstanding Balance | Remaining principal balance after this day |

---

## BIAN Data Dictionary

This service aligns with the BIAN **Installment Loan** service domain. Key term mappings:

| BIAN Term | Field / Class |
|---|---|
| Loan Repayment Schedule | `repaymentSchedule` (`LoanRepaymentSchedule`) |
| Installment Reference | `installmentReference` |
| Payment Due Date | `paymentDueDate` |
| Payment Amount | `paymentAmount` |
| Effective Interest Rate | `effectiveInterestRate` |
| Interest Accrual Record | `InterestAccrualRecord` |
| Accrued Interest Amount | `accruedInterestAmount` |
| Amortization Amount | `amortizationAmount` |
| Outstanding Balance | `outstandingBalance` |
| Accrual Basis | `accrualBasis` (`AccrualBasis`) |

---

## Performance Benchmark

Run the bundled benchmark to measure execution time and memory allocation:

```bash
cd AccrualCalculation
JAVA_HOME=$(/usr/libexec/java_home) mvn compile exec:java
```

Results are saved to `benchmark_results.txt`.

---

## Algorithm Notes

### IRR — Newton-Raphson

The NPV function is computed over daily compounding:

```
NPV = Σ [ paymentAmount[k] / (1 + r)^diffDays[k] ]
```

where `diffDays[k]` is the number of calendar days between the disbursement date and installment `k`. Day differences are pre-computed once before the iteration loop to avoid redundant date arithmetic.

### Interest Accrual

Each day's balance follows:

```
accruedInterest[i]  = outstandingBalance[i-1] × dailyInterestRate
outstandingBalance[i] = outstandingBalance[i-1] + accruedInterest[i] − amortization[i]
```

The `/summary` endpoint computes this loop maintaining only a rolling balance variable (O(1) memory), while `/detailed` stores the full daily series.
