package semestr1.hw4;

import java.util.Scanner;
public class hw4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n;

        do {
            System.out.print("Введіть верхню межу N (>= 2): ");
            n = scanner.nextInt();
        } while (n < 2);

        int primeCount = 0;
        long primeSum = 0;
        int totalNumbers = n - 1;

        System.out.println("\nЗнайдені прості числа у проміжку від 2 до " + n + ":");

        for (int i = 2; i <= n; i++) {
            boolean isPrime = true;

            for (int j = 2; (long) j * j <= i; j++) {
                if (i % j == 0) {
                    isPrime = false;
                    break;
                }
            }

            if (isPrime) {
                if (primeCount > 0) {
                    System.out.print(", ");
                }
                System.out.print(i);
                primeCount++;
                primeSum += i;
            }
        }

        double percentage = (double) primeCount / totalNumbers * 100;

        System.out.println("\n\n--- СТАТИСТИКА ---");
        System.out.println("Усього чисел у діапазоні:    " + totalNumbers);
        System.out.println("Знайдено простих чисел:      " + primeCount + " шт.");
        System.out.println("Сума всіх простих чисел:     " + primeSum);
        System.out.printf("Частка простих чисел:        %.2f%%%n", percentage);
        System.out.println("==============================");

        scanner.close();
    }
}
