package semestr1.hw3;

import java.util.Locale;
import java.util.Scanner;

public class hw1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
        System.out.println("=== КАЛЬКУЛЯТОР ПОЇЗДКИ ТА ПАЛЬНОГО ===");
        System.out.println("Введіть відстань поїздки (км): ");
        double a = scanner.nextDouble();
        System.out.println("Введіть середню витрату на 100 км (л):");
        double b = scanner.nextDouble();
        System.out.println("Оберіть тип пального (1 - А-95, 2 - Дизель, 3 - Газ):");
        int c = scanner.nextInt();
        if (a <= 0 || b <= 0) {
            System.out.println("Помилка: відстань та витрата пального повинні бути більше 0!");
            scanner.close();
            return;
        }
        double q = 0.0;
        String fuelName = "";
        switch (c) {
            case 1 -> {
                q = 56.50;
                fuelName = "Бензин А-95";
            }
            case 2 -> {
                q = 52.80;
                fuelName = "Дизель";
            }
            case 3 -> {
                q = 29.40;
                fuelName = ("Газ");
            }
            default -> {
                System.out.println("Помилка: невідомий тип пального!");
                scanner.close();
                return;

            }

        }
        double neededFuel = (a * b) / 100.0;
        double baseCost = neededFuel * q;
        double discountAmount = (baseCost >= 2000.0) ? baseCost * 0.05 : 0.0;
        double finalCost = (baseCost >= 2000.0) ? baseCost * 0.95 : baseCost;
        System.out.println("\n---------------- ЗВІТ ПОЇЗДКИ ----------------");
        System.out.println("Обране пальне:          " + fuelName + " (" + q + " грн/л)");
        System.out.println("Необхідно пального:     " + neededFuel + " л");
        System.out.println("Базова вартість:        " + baseCost + " грн");
        System.out.println("Знижка АЗС (5%):        " + discountAmount + " грн");
        System.out.println("----------------------------------------------");
        System.out.println("Підсумкова сума до сплати:   " + finalCost + " грн");
        System.out.println("==============================================");
        scanner.close();


    }
}
