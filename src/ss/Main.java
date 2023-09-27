package ss;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;


public class Main {
    public static ArrayBlockingQueue queueA = new ArrayBlockingQueue(100);
    public static ArrayBlockingQueue queueB = new ArrayBlockingQueue<>(100);
    public static ArrayBlockingQueue queueC = new ArrayBlockingQueue<>(100);
    ;

    public static void main(String[] args) throws InterruptedException {
        Thread textGenerator = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                String text = generateText("abc", 100_000);
                try {
                    queueA.put(text);
                    queueB.put(text);
                    queueC.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
            }
        });
        textGenerator.start();

        Thread a = new Thread(() -> {
            char letter = 'a';
            int maxA = findMaxCharCount(queueA, letter);
            System.out.println("Максимальное количество символов " +"\'"+ letter+"\'" + "\fв тексте: "  + maxA);
        });
        a.start();

        Thread b = new Thread(() -> {
            char letter = 'b';
            int maxB = findMaxCharCount(queueB, letter);
            System.out.println("Максимальное количество символов " +"\'"+ letter+"\'" + "\fв тексте: " + maxB);
        });
        b.start();

        Thread c = new Thread(() -> {
            char letter = 'c';
            int maxC = findMaxCharCount( queueC, letter);
            System.out.println("Максимальное количество символов " +"\'"+ letter+"\'" + "\fв тексте: " + maxC);
        });
        c.start();

        a.join();
        b.join();
        c.join();
    }

    private static int findMaxCharCount(ArrayBlockingQueue queue, char letter) {
        int count = 0;
        int max = 0;
        String text;
        try {
            for (int i = 0; i < 10_000; i++) {
                text = (String) queue.take();
                for (char c : text.toCharArray()) {
                    if (c == letter) count++;
                }
                if (count > max) max = count;
                count = 0;
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + "was in interrupted");
            return -1;
        }
        return max;
    }


    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}