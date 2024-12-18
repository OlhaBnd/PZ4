import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncArrayCalculation  {
    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(2);
        long startTime = System.currentTimeMillis();


        CompletableFuture<List<Double>> generateNums = CompletableFuture.supplyAsync(() -> {
            System.out.println("Генерація чисел...");
            Random rand = new Random();
            List<Double> numbers = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                numbers.add(((int)(rand.nextFloat()*10.0 * 100) / 100.0));
            }
            System.out.println("Згенерований масив: " + numbers);
            return numbers;
        }, es);

        generateNums.thenApplyAsync(numbers -> {
            System.out.println("Обчислень...");
            double result = 1.0;
            for (int i = 1; i < numbers.size(); i++) {
                result *= (numbers.get(i) - numbers.get(i - 1));
            }
            return result;
        }, es).thenAcceptAsync(result -> System.out.println("Результат обчислень: " + result));

        generateNums.thenRunAsync(()->{
            System.out.println("END");
            es.shutdown();
            System.out.println("Час виконання: " + (System.currentTimeMillis() - startTime) + " мс");
        });

        generateNums.join();
    }
}