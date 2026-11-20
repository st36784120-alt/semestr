package hw10;

import java.util.Locale;

public class Task2SmartPaymentAndAuditGateway {
    public static void main(String[] args) {
        AbstractPaymentTransaction[] ledger = {
                new PremiumCardPayment("TX-101", 20000.0, "4149 **** **** 8821", 0.03),
                new CryptoUsdtPayment("TX-102", 8500.0, "0x9f8a...c4", 18),
                new TaxTreasuryPayment("TX-103", 5000.0, "10101010")
        };

        auditLedger(ledger);
    }

    public static void auditLedger(AbstractPaymentTransaction[] ledger) {
        double totalTurnover = 0.0;
        double totalFees = 0.0;
        double totalCashback = 0.0;

        System.out.println("================ ПРОЦЕСИНГОВИЙ ЦЕНТР FINTECH (POLYMORPHISM & INTERFACES) ================");
        System.out.println("[1. ПРОВЕДЕННЯ ПАКЕТУ ТРАНЗАКЦІЙ (final process())]:");
        for (AbstractPaymentTransaction tx : ledger) {
            if (tx.process()) {
                double fee = tx.calculateFee();
                totalTurnover += tx.getBaseAmountUah();
                totalFees += fee;
                System.out.printf(Locale.US, "  ✅ [%s | %s] Сума: %.2f грн | Комісія: %.2f грн | Всього: %.2f грн%n",
                        tx.getTxId(), tx.getPaymentChannel(), tx.getBaseAmountUah(), fee, tx.getTotalCharged());
            }
        }

        System.out.println("\n[2. АВТОМАТИЧНИЙ АУДИТ МОЖЛИВОСТЕЙ (Pattern Matching instanceof)]:");
        boolean refundPerformed = false;
        for (AbstractPaymentTransaction tx : ledger) {
            if (!tx.isProcessed()) {
                continue;
            }
            if (tx instanceof CashbackCapable cb) {
                double rawCashback = tx.getBaseAmountUah() * cb.getCashbackRate();
                double actualCashback = cb.calculateCashback(tx.getBaseAmountUah());
                totalCashback += actualCashback;
                int percent = (int) Math.round(cb.getCashbackRate() * 100);
                if (rawCashback > CashbackCapable.MAX_CASHBACK_UAH) {
                    System.out.printf(Locale.US, "  💰 [hw10.CashbackCapable] %s: розрахунковий %d%% = %.0f грн -> нараховано %.2f грн (Ліміт MAX_CASHBACK_UAH!)%n",
                            tx.getTxId(), percent, rawCashback, actualCashback);
                } else {
                    System.out.printf(Locale.US, "  💰 [hw10.CashbackCapable] %s: нараховано %d%% крипто-бонусу = %.2f грн%n",
                            tx.getTxId(), percent, actualCashback);
                }
            }
            if (tx instanceof BlockchainVerifiable bv) {
                System.out.printf(Locale.US, "  ⛓️ [hw10.BlockchainVerifiable] %s: Hash=%s, Підтверджень=%d (Finalized: %b)%n",
                        tx.getTxId(), bv.getTxHash(), bv.getConfirmations(), bv.isFinalized());
            }
            if (tx instanceof Refundable ref && !refundPerformed) {
                boolean refunded = ref.requestRefund("Подвійне списання");
                refundPerformed = true;
                System.out.printf(Locale.US, "  🔄 [hw10.Refundable] Спроба повернення %s ('Подвійне списання'): %s%n",
                        tx.getTxId(), refunded ? "УСПІШНО ПОВЕРНЕНО!" : "ВІДХИЛЕНО");
            }
        }

        System.out.println("\n[ПІДСУМКОВИЙ ЗВІТ ШЛЮЗУ]:");
        System.out.printf(Locale.US, "  Сумарний базовий оборот:  %.2f грн%n", totalTurnover);
        System.out.printf(Locale.US, "  Сумарна комісія шлюзу:      %.2f грн%n", totalFees);
        System.out.printf(Locale.US, "  Сумарний виплачений кешбек: %.2f грн%n", totalCashback);
        System.out.println("=========================================================================================");
    }
}

interface CashbackCapable {
    double MAX_CASHBACK_UAH = 500.0;

    double getCashbackRate();

    default double calculateCashback(double baseAmount) {
        return Math.min(MAX_CASHBACK_UAH, baseAmount * getCashbackRate());
    }
}

interface Refundable {
    boolean requestRefund(String reason);

    boolean isRefunded();
}

interface BlockchainVerifiable {
    String getTxHash();

    int getConfirmations();

    default boolean isFinalized() {
        return getConfirmations() >= 12;
    }
}

abstract class AbstractPaymentTransaction {
    private final String txId;
    private final double baseAmountUah;
    private boolean processed;

    public AbstractPaymentTransaction(String txId, double baseAmountUah) {
        this.txId = txId;
        this.baseAmountUah = baseAmountUah;
        this.processed = false;
    }

    public String getTxId() {
        return txId;
    }

    public double getBaseAmountUah() {
        return baseAmountUah;
    }

    public boolean isProcessed() {
        return processed;
    }

    public abstract double calculateFee();

    public abstract String getPaymentChannel();

    public final boolean process() {
        if (baseAmountUah <= 0.0 || processed) {
            return false;
        }
        processed = true;
        return true;
    }

    public final double getTotalCharged() {
        return baseAmountUah + calculateFee();
    }
}

class PremiumCardPayment extends AbstractPaymentTransaction implements CashbackCapable, Refundable {
    private final String maskedPan;
    private final double cashbackRate;
    private boolean refunded;

    public PremiumCardPayment(String txId, double baseAmountUah, String maskedPan, double cashbackRate) {
        super(txId, baseAmountUah);
        this.maskedPan = maskedPan;
        this.cashbackRate = cashbackRate;
        this.refunded = false;
    }

    public String getMaskedPan() {
        return maskedPan;
    }

    @Override
    public double calculateFee() {
        return getBaseAmountUah() * 0.01;
    }

    @Override
    public String getPaymentChannel() {
        return "Visa Infinite";
    }

    @Override
    public double getCashbackRate() {
        return cashbackRate;
    }

    @Override
    public boolean requestRefund(String reason) {
        if (isProcessed() && !refunded) {
            refunded = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean isRefunded() {
        return refunded;
    }
}

class CryptoUsdtPayment extends AbstractPaymentTransaction implements CashbackCapable, BlockchainVerifiable {
    private final String txHash;
    private final int confirmations;

    public CryptoUsdtPayment(String txId, double baseAmountUah, String txHash, int confirmations) {
        super(txId, baseAmountUah);
        this.txHash = txHash;
        this.confirmations = confirmations;
    }

    @Override
    public double calculateFee() {
        return 40.0;
    }

    @Override
    public String getPaymentChannel() {
        return "USDT TRC-20";
    }

    @Override
    public double getCashbackRate() {
        return 0.01;
    }

    @Override
    public String getTxHash() {
        return txHash;
    }

    @Override
    public int getConfirmations() {
        return confirmations;
    }
}

final class TaxTreasuryPayment extends AbstractPaymentTransaction implements Refundable {
    private final String taxCode;
    private boolean refunded;

    public TaxTreasuryPayment(String txId, double baseAmountUah, String taxCode) {
        super(txId, baseAmountUah);
        this.taxCode = taxCode;
        this.refunded = false;
    }

    public String getTaxCode() {
        return taxCode;
    }

    @Override
    public double calculateFee() {
        return 0.0;
    }

    @Override
    public String getPaymentChannel() {
        return "ДержКазначейство";
    }

    @Override
    public boolean requestRefund(String reason) {
        if (isProcessed() && !refunded) {
            refunded = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean isRefunded() {
        return refunded;
    }
}