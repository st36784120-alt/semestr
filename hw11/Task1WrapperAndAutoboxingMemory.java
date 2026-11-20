package hw11;

import java.util.ArrayList;
import java.util.List;

public class Task1WrapperAndAutoboxingMemory {

    public static void inspectIntegerCache(int[] testValues) {
        System.out.println("[1. ЕКСПЕРИМЕНТ: INTEGER CACHE [-128..127] (== проти .equals())]:");
        for (int val : testValues) {
            Integer first = Integer.valueOf(val);
            Integer second = Integer.valueOf(val);
            boolean byRef = (first == second);
            boolean byVal = first.equals(second);
            String source = byRef ? "IntegerCache" : "Новий об'єкт у Heap!";
            System.out.printf("  Значення: %5d | (a == b): %-5b | a.equals(b): %-5b | Джерело: %s%n",
                    val, byRef, byVal, source);
        }
        System.out.println();
    }

    public static void demonstrateAutoboxingUnderTheHood() {
        System.out.println("[2. AUTOBOXING / UNBOXING ПІД КАПОТОМ ТА ЗАХИСТ ВІД NULL]:");

        Integer implicitInt = 42;
        Integer explicitInt = Integer.valueOf(42);
        int unboxedImplicit = implicitInt;
        int unboxedExplicit = explicitInt.intValue();
        System.out.println("  Явне пакування: Integer.valueOf(42) -> " + explicitInt
                + " | Розпакування: .intValue() -> " + unboxedExplicit);

        Double implicitDouble = 3.14;
        Double explicitDouble = Double.valueOf(3.14);
        double unboxedDouble = explicitDouble.doubleValue();
        System.out.println("  Double: Double.valueOf(" + implicitDouble + ") -> " + explicitDouble
                + " | .doubleValue() -> " + unboxedDouble);

        Boolean implicitBool = true;
        Boolean explicitBool = Boolean.valueOf(true);
        boolean unboxedBool = explicitBool.booleanValue();
        System.out.println("  Boolean: Boolean.valueOf(" + implicitBool + ") -> " + explicitBool
                + " | .booleanValue() -> " + unboxedBool);

        List<Integer> sensorReadings = new ArrayList<>();
        sensorReadings.add(45);
        sensorReadings.add(null);
        sensorReadings.add(-12);
        sensorReadings.add(88);
        sensorReadings.add(null);
        sensorReadings.add(102);

        System.out.println("  Вхідний список із датчиків (з null): " + sensorReadings);

        int sum = 0;
        for (int i = 0; i < sensorReadings.size(); i++) {
            Integer current = sensorReadings.get(i);
            if (current == null) {
                System.out.println("  ⚠️ Пропущено null-елемент на індексі " + i
                        + " (запобігання NullPointerException при intValue()!)");
                continue;
            }
            sum += current.intValue();
        }
        System.out.println("  Сума валідних розпакованих елементів: " + sum);
        System.out.println();
    }

    public static List<Integer> parseTelemetryStream(String[] rawPackets) {
        System.out.println("[3. ПАРСИНГ РЯДКІВ (Integer.parseInt) ТА ЕКСТРЕМУМИ (MAX_VALUE / MIN_VALUE)]:");
        List<Integer> validData = new ArrayList<>();

        if (rawPackets == null) {
            System.out.println("  ❌ Потік пакетів null");
            return validData;
        }

        for (String packet : rawPackets) {
            if (packet == null || packet.trim().isEmpty()) {
                System.out.println("  ❌ Порожній або null-пакет відхилено");
                continue;
            }
            try {
                int parsedValue = Integer.parseInt(packet.trim());
                validData.add(Integer.valueOf(parsedValue));
            } catch (NumberFormatException e) {
                System.out.println("  ❌ Пакет '" + packet + "' відхилено (NumberFormatException)");
            }
        }
        return validData;
    }

    public static void printExtremesAndStats(List<Integer> validData) {
        if (validData == null || validData.isEmpty()) {
            System.out.println("  Немає даних для обчислення статистики.");
            return;
        }

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        long sum = 0;
        boolean hasValues = false;

        System.out.println("  Початкові межі пошуку: min = " + min + " (MAX_VALUE), max = " + max + " (MIN_VALUE)");
        System.out.println("  Успішно розпізнаний список Integer: " + validData);

        for (int i = 0; i < validData.size(); i++) {
            Integer item = validData.get(i);
            if (item != null) {
                hasValues = true;
                int value = item.intValue();
                if (value < min) {
                    min = value;
                }
                if (value > max) {
                    max = value;
                }
                sum += value;
            }
        }

        if (!hasValues) {
            System.out.println("  Список не містить числових значень для обчислення статистики.");
            return;
        }

        System.out.println("  Знайдений мінімум: " + min + " | Знайдений максимум: " + max + " | Сума: " + sum);

        List<Integer> copyForIndexTest = new ArrayList<>(validData);
        Integer removedByIndex = copyForIndexTest.remove(0);
        System.out.println("  Демонстрація remove(0) (за індексом видалено перший елемент " + removedByIndex + "): " + copyForIndexTest);

        validData.remove(Integer.valueOf(-30));
        System.out.println("  Після remove(Integer.valueOf(-30)) (за значенням): " + validData);
    }

    public static void main(String[] args) {
        System.out.println("================ ДОСЛІДЖЕННЯ WRAPPER CLASSES ТА INTEGER CACHE ================");

        int[] testValues = {-128, -50, 0, 127, 128, 500, 2026};
        inspectIntegerCache(testValues);

        demonstrateAutoboxingUnderTheHood();

        String[] rawPackets = {"  145 ", "-30", "ERR_404", "980", "", null, "2147483647", "-85"};
        List<Integer> parsedList = parseTelemetryStream(rawPackets);
        printExtremesAndStats(parsedList);

        System.out.println("==============================================================================");
    }
}
