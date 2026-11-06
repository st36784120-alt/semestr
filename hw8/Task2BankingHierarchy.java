import java.util.Locale;

class Acc {
    private String iban, name;
    protected double bal;

    public Acc(String iban, String name, double bal) {
        this.iban = iban;
        this.name = name;
        this.bal = Math.max(bal, 0.0);
    }

    public void deposit(double sum) {
        if (sum > 0) bal += sum;
    }

    public boolean withdraw(double sum) {
        if (sum > 0 && sum <= bal) {
            bal -= sum;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "Рахунок[%s | Власник: %s | Баланс: %.2f грн]", iban, name, bal);
    }
}

class Save extends Acc {
    private double rate, min;

    public Save(String iban, String name, double bal, double rate, double min) {
        super(iban, name, bal);
        this.rate = rate;
        this.min = min;
    }

    @Override
    public boolean withdraw(double sum) {
        if (bal - sum >= min) return super.withdraw(sum);
        System.out.printf(Locale.US, "ВІДМОВА (порушення незнижуваного залишку %.2f грн)\n", min);
        return false;
    }

    public void addPercent() { deposit(bal * (rate / 100.0)); }

    @Override
    public String toString() {
        return String.format(Locale.US, "SavingsAccount{%s, Ставка: %.1f%%, Мін.залишок: %.2f грн}",
                super.toString(), rate, min);
    }
}

class Cred extends Acc {
    private double lim, fee;

    public Cred(String iban, String name, double bal, double lim, double fee) {
        super(iban, name, bal);
        this.lim = lim;
        this.fee = fee;
    }

    @Override
    public boolean withdraw(double sum) {
        if (bal >= sum) return super.withdraw(sum);

        double debt = sum - Math.max(0.0, bal);
        double com = debt * (fee / 100.0); // комиссия

        if (bal - sum - com >= -lim) {
            bal -= (sum + com);
            return true;
        }
        return false;
    }

    public double getDebt() { return bal < 0 ? Math.abs(bal) : 0.0; }

    @Override
    public String toString() {
        return String.format(Locale.US, "CreditAccount{%s, Ліміт: %.2f грн, Борг: %.2f грн}",
                super.toString(), lim, getDebt());
    }
}

public class Task2BankingHierarchy {
    public static void main(String[] args) {
        System.out.println("================ БАНКІВСЬКА ІЄРАРХІЯ РАХУНКІВ ================");

        System.out.println("1. Ощадний рахунок (початковий стан):");
        Save save = new Save("UA101", "Олена Коваль", 5000.0, 12.0, 1000.0);
        System.out.println(save);

        System.out.print("-> Спроба зняти 4500 грн: ");
        save.withdraw(4500);

        System.out.println("-> Нарахування річних відсотків (+12%)...");
        save.addPercent();
        System.out.println(save);

        System.out.println("\n2. Кредитний рахунок (початковий стан):");
        Cred cred = new Cred("UA202", "Максим Бондар", 2000.0, 10000.0, 2.0);
        System.out.println(cred);

        System.out.print("-> Покупка на 5000 грн (3000 грн у кредит з комісією 2% = 60 грн)... ");
        if (cred.withdraw(5000)) System.out.println("УСПІШНО!");
        System.out.println(cred);

        System.out.println("==============================================================");
    }
}