package semestr1.hw3;

import java.util.Scanner;
public class hw2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== ТАРИФІКАТОР СЛУЖБИ ДОСТАВКИ ===");

        System.out.print("Введіть вагу відправлення (кг): ");
        double weight = scanner.nextDouble();

        System.out.print("Введіть відстань транспортування (км): ");
        int distance = scanner.nextInt();

        System.out.print("Оберіть пункт призначення (1 - Відділення, 2 - Поштомат, 3 - Кур'єр): ");
        int deliveryType = scanner.nextInt();

        scanner.nextLine();

        System.out.print("Чи є у вас карта Premium? (так/ні): ");
        String premiumInput = scanner.nextLine();

        if (weight <= 0 || weight > 50.0) {
            System.out.println("\nПомилка: Вага відправлення повинна бути більше 0 та не перевищувати 50.0 кг!");
            scanner.close();
            return;
        }

        if (distance <= 0) {
            System.out.println("\nПомилка: Відстань транспортування повинна бути більше 0 км!");
            scanner.close();
            return;
        }

        String deliveryTypeName = "";
        double baseTariff = switch (deliveryType) {
            case 1 -> {
                deliveryTypeName = "Відділення";
                yield 50.0;
            }
            case 2 -> {
                if (weight > 15.0) {
                    System.out.println("\nПомилка: Поштомат не приймає габаритні посилки вагою понад 15.0 кг!");
                    yield -1.0;
                }
                deliveryTypeName = "Поштомат";
                yield 60.0;
            }
            case 3 -> {
                deliveryTypeName = "Кур'єр";
                yield 100.0;
            }
            default -> {
                System.out.println("\nПомилка: Обрано неіснуючий тип доставки!");
                yield -1.0;
            }
        };

        if (baseTariff == -1.0) {
            scanner.close();
            return;
        }

        double distanceFee;
        if (distance <= 50) {
            distanceFee = 0.0;
        } else if (distance <= 200) {
            distanceFee = 35.0;
        } else {
            distanceFee = 80.0;
        }

        boolean isPremium = premiumInput.equalsIgnoreCase("так");
        double discountRate = isPremium ? 0.20 : 0.0;

        double sumBeforeDiscount = baseTariff + distanceFee;
        double totalAmount = sumBeforeDiscount * (1.0 - discountRate);

        System.out.println("\n---------------- НАКЛАДНА ДОСТАВКИ ----------------");
        System.out.println("Тип доставки:        " + deliveryTypeName + " (базовий тариф: " + baseTariff + " грн)");
        System.out.println("Доплата за відстань: " + distanceFee + " грн (" + distance + " км)");
        System.out.println("Сума до знижки:      " + sumBeforeDiscount + " грн");
        System.out.println("Статус клієнта:      " + (isPremium ? "Premium (-20%)" : "Звичайний (0%)"));
        System.out.println("---------------------------------------------------");
        System.out.println("РАЗОМ ДО СПЛАТИ:     " + totalAmount + " грн");
        System.out.println("===================================================");

        scanner.close();
    }
}