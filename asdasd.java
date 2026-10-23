package semestr1;

import java.util.Scanner;
public class asdasd {
    static void main(String... args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введіть перше число: ");
        int a = scanner.nextInt();

        System.out.println("Введіть друге число: ");
        int b = scanner.nextInt();

        System.out.println("Сума: " + (a + b));
        System.out.println("Різниця: " + (a - b));
        System.out.println("Добуток: " + (a * b));
        System.out.println("Остача від ділення: " + (a % b));

        scanner.close();
    }
}