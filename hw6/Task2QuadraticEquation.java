package semestr1.hw6;

public class Task2QuadraticEquation {
    private static class QuadraticEquation {
        private double a;
        private double b;
        private double c;

        public void setCoefficients(double a, double b, double c) {
            if (a == 0) {
                System.out.println("Увага: коефіцієнт 'a' не може дорівнювати 0!");
                this.a = 1.0;
            } else {
                this.a = a;
            }
            this.b = b;
            this.c = c;
        }

        public double getDiscriminant() {
            return b * b - 4 * a * c;
        }

        public double getRoot1() {
            double discriminant = getDiscriminant();
            if (discriminant < 0) {
                return Double.NaN;
            }
            return (-b + Math.sqrt(discriminant)) / (2 * a);
        }

        public double getRoot2() {
            double discriminant = getDiscriminant();
            if (discriminant < 0) {
                return Double.NaN;
            }
            return (-b - Math.sqrt(discriminant)) / (2 * a);
        }

        public void printSolution() {
            System.out.printf("Рівняння: %.1f*x^2 + (%.1f)*x + %.1f = 0%n", a, b, c);

            double discriminant = getDiscriminant();
            System.out.printf("Дискримінант D = %.2f%n", discriminant);

            if (discriminant > 0) {
                System.out.println("Рівняння має 2 дійсні корені:");
                System.out.printf("  x1 = %.2f%n", getRoot1());
                System.out.printf("  x2 = %.2f%n", getRoot2());
            } else if (discriminant == 0) {
                System.out.println("Рівняння має один дійсний корінь:");
                System.out.printf("  x = %.2f%n", getRoot1());
            } else {
                System.out.println("Рівняння не має дійсних коренів (D < 0)");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== РОЗВ'ЯЗУВАЧ КВАДРАТНИХ РІВНЯНЬ ===");

        QuadraticEquation equation = new QuadraticEquation();

        System.out.println("Тест 1 (Два корені):");
        equation.setCoefficients(1.0, -5.0, 6.0);
        equation.printSolution();
        System.out.println("--------------------------------------");

        System.out.println("Тест 2 (Один корінь):");
        equation.setCoefficients(1.0, -4.0, 4.0);
        equation.printSolution();
        System.out.println("--------------------------------------");

        System.out.println("Тест 3 (Немає коренів):");
        equation.setCoefficients(1.0, 2.0, 5.0);
        equation.printSolution();
        System.out.println("======================================");
    }
}
