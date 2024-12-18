import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class AsyncArrayProcess {
    public static void main(String[] args) {
        CompletableFuture<int[]> futureArrayCreate =
                CompletableFuture
                        .runAsync(()-> System.out.println("START"))
                        .supplyAsync(() -> {
            Random random = new Random();
            int size = 10;

            long startTime = System.nanoTime();

            int[] array = new int[size];
            for (int i = 0; i < size; i++) {
                array[i] = random.nextInt(101); // [0, 100]
            }

            long endTime = System.nanoTime();
            System.out.println("\nМасив згенеровано: " + Arrays.toString(array));
            System.out.println("Час затрачений на генерацію: " + (endTime - startTime) / 1_000_000.0 + " мс");
            return array;
        });

        futureArrayCreate.thenApplyAsync(array -> {
            long startTime = System.nanoTime();

            int[] modifiedArray = new int[array.length];
            for (int i = 0; i < array.length; i++) {
                modifiedArray[i] = array[i] + 10;
            }

            long endTime = System.nanoTime();
            System.out.println("\nМасив після додавання +10: " + Arrays.toString(modifiedArray));
            System.out.println("Час потрачений на модифікацію (+10): " + (endTime - startTime) / 1_000_000.0 + " мс");
            return modifiedArray;
        }).thenApplyAsync(array -> {
            long startTime = System.nanoTime();

            double[] dividedArray = new double[array.length];
            for (int i = 0; i < array.length; i++) {
                dividedArray[i] = array[i] / 2.0;
            }

            long endTime = System.nanoTime();
            System.out.println("\nМасив після ділення на 2: " + Arrays.toString(dividedArray));
            System.out.println("Час використаний на модифікацію (ділення на 2): " + (endTime - startTime) / 1_000_000.0 + " мс");
            return dividedArray;

        }).thenAcceptAsync(array -> {
            long startTime = System.nanoTime();
            System.out.println("\nРезультат ділення: " + Arrays.toString(array));
            long endTime = System.nanoTime();
            System.out.println("Час виведення результату ділення: " + (endTime - startTime) / 1_000_000.0 + " мс");
        });

        futureArrayCreate.join();
    }
}