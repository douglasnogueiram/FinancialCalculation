package benchmark;

import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import controller.InterestAccrualCalculation;
import model.AccrualBasis;
import model.IrrCalculator;
import model.LoanRepaymentSchedule;

public class BenchmarkRunner {

    private static final int WARMUP   = 10;
    private static final int RUNS     = 30;
    private static final PrintStream REAL_OUT = System.out;

    static class IrrParams {
        final BigDecimal            initialRateEstimate;
        final LoanRepaymentSchedule repaymentSchedule;
        IrrParams(BigDecimal initialRateEstimate, LoanRepaymentSchedule repaymentSchedule) {
            this.initialRateEstimate = initialRateEstimate;
            this.repaymentSchedule   = repaymentSchedule;
        }
    }

    static class AccrualParams {
        final BigDecimal            dailyInterestRate;
        final LoanRepaymentSchedule repaymentSchedule;
        final LocalDate             accrualDate;
        AccrualParams(BigDecimal dailyInterestRate, LoanRepaymentSchedule repaymentSchedule, LocalDate accrualDate) {
            this.dailyInterestRate = dailyInterestRate;
            this.repaymentSchedule = repaymentSchedule;
            this.accrualDate       = accrualDate;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        REAL_OUT.println("╔════════════════════════════════════════════════════════════╗");
        REAL_OUT.printf ("║         FinancialCalculation — Benchmark Suite             ║%n");
        REAL_OUT.printf ("║   warm-up: %-3d iterations  |  measurement: %-3d iterations  ║%n", WARMUP, RUNS);
        REAL_OUT.println("╚════════════════════════════════════════════════════════════╝");
        REAL_OUT.println();

        runIrr    ("IRR — Small    (6 installments / 6 months)",        irrSmall());
        runIrr    ("IRR — Large    (120 installments / 10 years)",      irrLarge());
        runAccrual("Accrual Summary  — Short   (2 months / ~60 days)",  accrualShort(), false);
        runAccrual("Accrual Summary  — Long    (10 years / ~3650 days)", accrualLong(),  false);
        runAccrual("Accrual Detailed — Long    (10 years / ~3650 days)", accrualLong(),  true);
    }

    // ─── Runners ──────────────────────────────────────────────────────────────

    static void runIrr(String label, IrrParams p) throws InterruptedException {
        suppressOutput();

        for (int i = 0; i < WARMUP; i++)
            IrrCalculator.calculate(p.initialRateEstimate, p.repaymentSchedule);

        long[] times   = new long[RUNS];
        long memAccum  = 0;

        for (int i = 0; i < RUNS; i++) {
            long m0 = usedMemory();
            long t0 = System.nanoTime();
            IrrCalculator.calculate(p.initialRateEstimate, p.repaymentSchedule);
            times[i]  = System.nanoTime() - t0;
            memAccum += usedMemory() - m0;
        }

        restoreOutput();
        printStats(label, times, memAccum / RUNS);
    }

    static void runAccrual(String label, AccrualParams p, boolean detailed) throws InterruptedException {
        suppressOutput();

        for (int i = 0; i < WARMUP; i++) {
            InterestAccrualCalculation c = new InterestAccrualCalculation(
                p.dailyInterestRate, p.repaymentSchedule, p.accrualDate, AccrualBasis.END_OF_PERIOD);
            if (detailed) c.calculateDetailedResult(); else c.calculateSummaryResult();
        }

        long[] times  = new long[RUNS];
        long memAccum = 0;

        for (int i = 0; i < RUNS; i++) {
            InterestAccrualCalculation c = new InterestAccrualCalculation(
                p.dailyInterestRate, p.repaymentSchedule, p.accrualDate, AccrualBasis.END_OF_PERIOD);
            long m0 = usedMemory();
            long t0 = System.nanoTime();
            if (detailed) c.calculateDetailedResult(); else c.calculateSummaryResult();
            times[i]  = System.nanoTime() - t0;
            memAccum += usedMemory() - m0;
        }

        restoreOutput();
        printStats(label, times, memAccum / RUNS);
    }

    // ─── Test data ────────────────────────────────────────────────────────────

    static IrrParams irrSmall() {
        int[] ref     = {0, 1, 2, 3, 4, 5, 6};
        LocalDate[] d = {
            LocalDate.of(2024,  9, 1), LocalDate.of(2024, 10, 1),
            LocalDate.of(2024, 11, 1), LocalDate.of(2024, 12, 1),
            LocalDate.of(2025,  1, 1), LocalDate.of(2025,  2, 1),
            LocalDate.of(2025,  3, 1)
        };
        BigDecimal[] v = {
            new BigDecimal("-5000.00000000000"),
            new BigDecimal("1447.95455883595"), new BigDecimal("1447.95455883595"),
            new BigDecimal("1447.95455883595"), new BigDecimal("1447.95455883595"),
            new BigDecimal("1447.95455883595"), new BigDecimal("1447.95455883595")
        };
        return new IrrParams(new BigDecimal("0.0001"), new LoanRepaymentSchedule(ref, d, v));
    }

    static IrrParams irrLarge() {
        int n = 121;
        int[]         ref   = new int[n];
        LocalDate[]   dates = new LocalDate[n];
        BigDecimal[]  vals  = new BigDecimal[n];

        LocalDate start = LocalDate.of(2015, 1, 1);
        ref[0]   = 0; dates[0] = start; vals[0] = new BigDecimal("-100000.00");
        for (int i = 1; i < n; i++) {
            ref[i]   = i;
            dates[i] = start.plusMonths(i);
            vals[i]  = new BigDecimal("1100.00");
        }
        return new IrrParams(new BigDecimal("0.0001"), new LoanRepaymentSchedule(ref, dates, vals));
    }

    static AccrualParams accrualShort() {
        int[] ref     = {0, 1, 2};
        LocalDate[] d = {
            LocalDate.of(2024,  9, 1),
            LocalDate.of(2024, 10, 1),
            LocalDate.of(2024, 11, 1)
        };
        BigDecimal[] v = {
            new BigDecimal("50000.0000000"),
            new BigDecimal("2651.1922386"),
            new BigDecimal("2651.1922386")
        };
        return new AccrualParams(
            new BigDecimal("0.000660305482286683"),
            new LoanRepaymentSchedule(ref, d, v),
            LocalDate.of(2024, 11, 1)
        );
    }

    static AccrualParams accrualLong() {
        int n = 121;
        int[]        ref   = new int[n];
        LocalDate[]  dates = new LocalDate[n];
        BigDecimal[] vals  = new BigDecimal[n];

        LocalDate start = LocalDate.of(2015, 1, 1);
        ref[0]   = 0; dates[0] = start; vals[0] = new BigDecimal("100000.00");
        for (int i = 1; i < n; i++) {
            ref[i]   = i;
            dates[i] = start.plusMonths(i);
            vals[i]  = new BigDecimal("1100.00");
        }
        return new AccrualParams(
            new BigDecimal("0.000660305482286683"),
            new LoanRepaymentSchedule(ref, dates, vals),
            LocalDate.of(2025, 1, 1)
        );
    }

    // ─── Metrics ──────────────────────────────────────────────────────────────

    static long usedMemory() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    static void printStats(String label, long[] nanos, long avgMemDelta) {
        Arrays.sort(nanos);
        long min = nanos[0];
        long max = nanos[RUNS - 1];
        long p95 = nanos[(int) (RUNS * 0.95)];
        long avg = 0;
        for (long t : nanos) avg += t;
        avg /= RUNS;

        String sep = "─".repeat(64);
        REAL_OUT.println(sep);
        REAL_OUT.println("  " + label);
        REAL_OUT.println(sep);
        REAL_OUT.printf ("  Time (µs):   min=%,7d   avg=%,7d   p95=%,7d   max=%,7d%n",
            min / 1_000, avg / 1_000, p95 / 1_000, max / 1_000);
        REAL_OUT.printf ("  Memory:      avg delta per call ≈ %s%n", formatBytes(avgMemDelta));
        REAL_OUT.println();
    }

    static String formatBytes(long bytes) {
        if (bytes < 0)
            return String.format("%d KB  ⚠ GC occurred during measurement (may be underreported)", bytes / 1024);
        if (bytes < 1024)        return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.2f MB", bytes / (1024.0 * 1024));
    }

    static void suppressOutput() {
        System.setOut(new PrintStream(OutputStream.nullOutputStream()));
    }

    static void restoreOutput() {
        System.setOut(REAL_OUT);
    }
}
