package hw11;

import java.util.ArrayList;

public class Task2ArrayListCapacityAndDynamicGrowth {

    static class CustomArrayCapacityEmulator<E> {

        private static final int DEFAULT_CAPACITY = 10;
        private Object[] elementData;
        private int size = 0;
        private int resizeCount = 0;
        private int totalElementsCopied = 0;

        public CustomArrayCapacityEmulator() {
            this.elementData = new Object[DEFAULT_CAPACITY];
        }

        private void ensureCapacityInternal() {
            if (size == elementData.length) {
                int oldCapacity = elementData.length;
                int newCapacity = oldCapacity + (oldCapacity >> 1);
                Object[] newBuffer = new Object[newCapacity];
                System.arraycopy(elementData, 0, newBuffer, 0, size);
                elementData = newBuffer;
                resizeCount++;
                totalElementsCopied += size;
                System.out.println("  ⚡ [GROW #" + resizeCount + "] Переповнення при додаванні " + (size + 1)
                        + "-го елемента! Ємність: " + oldCapacity + " -> " + newCapacity
                        + " (System.arraycopy скопіював " + size + " ел.)");
            }
        }

        private void rangeCheck(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
        }

        private void rangeCheckForAdd(int index) {
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
        }

        public void add(E element) {
            ensureCapacityInternal();
            elementData[size++] = element;
        }

        public void add(int index, E element) {
            rangeCheckForAdd(index);
            ensureCapacityInternal();
            int numMoved = size - index;
            if (numMoved > 0) {
                System.arraycopy(elementData, index, elementData, index + 1, numMoved);
            }
            elementData[index] = element;
            size++;
        }

        public E remove(int index) {
            rangeCheck(index);
            @SuppressWarnings("unchecked")
            E oldValue = (E) elementData[index];
            int numMoved = size - index - 1;
            if (numMoved > 0) {
                System.arraycopy(elementData, index + 1, elementData, index, numMoved);
            }
            elementData[--size] = null;
            return oldValue;
        }

        public E get(int index) {
            rangeCheck(index);
            @SuppressWarnings("unchecked")
            E value = (E) elementData[index];
            return value;
        }

        public E set(int index, E element) {
            rangeCheck(index);
            @SuppressWarnings("unchecked")
            E oldValue = (E) elementData[index];
            elementData[index] = element;
            return oldValue;
        }

        public int size() {
            return size;
        }

        public int getCapacity() {
            return elementData.length;
        }

        public int getResizeCount() {
            return resizeCount;
        }

        public int getTotalElementsCopied() {
            return totalElementsCopied;
        }

        public void trimToSize() {
            if (size < elementData.length) {
                int oldCapacity = elementData.length;
                Object[] trimmedBuffer = new Object[size];
                System.arraycopy(elementData, 0, trimmedBuffer, 0, size);
                elementData = trimmedBuffer;
                System.out.println("  -> Викликано trimToSize(): буфер Object[" + oldCapacity
                        + "] стиснуто рівно до Object[" + size + "]!");
            }
        }
    }

    public static void main(String[] args) {
            System.out.println("================ ЕМУЛЯТОР ВНУТРІШНЬОЇ БУДОВИ ARRAYLIST (ПІД КАПОТОМ) ================");

            var emulator = new CustomArrayCapacityEmulator<String>();

            var raw = new ArrayList<>();
            raw.add("StringValue");
            raw.add(100);
            System.out.println("[ПЕРЕВІРКА ВИВЕДЕННЯ ТИПІВ]: var raw = new ArrayList<>() втрачає тип до Object (елементи: "
                    + raw.get(0).getClass().getSimpleName() + " та " + raw.get(1).getClass().getSimpleName() + ")");

            System.out.println("[ІНІЦІАЛІЗАЦІЯ]: Створено CustomArrayCapacityEmulator<String> | size = "
                    + emulator.size() + " | capacity = " + emulator.getCapacity());
            System.out.println();

            System.out.println("--- Додавання 35 елементів у список ---");
            for (int i = 1; i <= 35; i++) {
                emulator.add("Block-" + i);
            }
            int freeSlots = emulator.getCapacity() - emulator.size();
            System.out.println("Стан після додавання 35 елементів: size = " + emulator.size()
                    + " | capacity = " + emulator.getCapacity()
                    + " (Вільний резерв: " + freeSlots + " комірок null)");
            System.out.println();

            System.out.println("--- Тестування вставки add(0, 'HEADER') та видалення remove(5) ---");
            int shiftedRight = emulator.size();
            emulator.add(0, "HEADER");
            System.out.println("  -> Вставлено 'HEADER' на індекс 0 (зсунуто праворуч " + shiftedRight + " елементів через System.arraycopy)");

            int shiftedLeft = emulator.size() - 5 - 1;
            String removedElement = emulator.remove(5);
            System.out.println("  -> Видалено елемент '" + removedElement + "' з індексу 5 (зсунуто ліворуч "
                    + shiftedLeft + " елементів, хвіст занулено для GC)");

            System.out.println("  Поточний стан: size = " + emulator.size()
                    + " | capacity = " + emulator.getCapacity()
                    + " | Перший: '" + emulator.get(0) + "'"
                    + " | Останній: '" + emulator.get(emulator.size() - 1) + "'");
            System.out.println();

            System.out.println("--- Оптимізація пам'яті trimToSize() ---");
            emulator.trimToSize();
            System.out.println("  Фінальна телеметрія: size = " + emulator.size()
                    + " | capacity = " + emulator.getCapacity()
                    + " | Кількість розширень: " + emulator.getResizeCount()
                    + " | Усього скопійовано при рості: " + emulator.getTotalElementsCopied());
            System.out.println("=====================================================================================");
    }
}
