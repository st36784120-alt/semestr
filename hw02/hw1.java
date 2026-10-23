package semestr1.hw02;

public class hw1 {
    public static void main(String[] args) {
        double usd = 250.0;
        final double RATE = 41.25;
        final double COMMISSION = 1.5;

        double uah = usd * RATE;
        double fee = uah * COMMISSION / 100;
        double net = uah - fee;
        long cash = Math.round(net);

        long usdCents = Math.round(usd * 100);
        long rateCents = Math.round(RATE * 100);
        long uahCents = Math.round(uah * 100);
        long feeCents = Math.round(fee * 100);
        long netCents = Math.round(net * 100);

        System.out.println("================ КВИТАНЦІЯ ОБМІНУ ================");
        System.out.println("Сума до обміну:             " + (usdCents / 100) + "." + ((usdCents % 100) / 10) + (usdCents % 10) + " USD");
        System.out.println("Курс валют:                 " + (rateCents / 100) + "." + ((rateCents % 100) / 10) + (rateCents % 10) + " UAH/USD");
        System.out.println("Нараховано до комісії:      " + (uahCents / 100) + "." + ((uahCents % 100) / 10) + (uahCents % 10) + " UAH");
        System.out.println("Комісія банку (" + COMMISSION + "%):       " + (feeCents / 100) + "." + ((feeCents % 100) / 10) + (feeCents % 10) + " UAH");
        System.out.println("--------------------------------------------------");
        System.out.println("Чиста сума до видачі:       " + (netCents / 100) + "." + ((netCents % 100) / 10) + (netCents % 10) + " UAH");
        System.out.println("До видачі готівкою:         " + cash + " UAH");
        System.out.println("==================================================");
    }
}
