package lab.lab2;

import java.util.Scanner;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author IITII
 */
public class FirstProblem {
    static class Task implements Runnable {

        private final int id;

        public Task(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                System.out.println("Thread " + id + " started");
                System.out.flush();
                Thread.sleep(1000);
                System.out.println("Thread " + id + " stopped");
                System.out.flush();
            } catch (Exception ignored) {
            }
        }
    }

    public static void main(String[] args) {
        System.out.print("请输入要生成的线程数量：");
        try {
            ThreadPoolExecutor factory = new ThreadPoolExecutor(1,
                    1000,
                    5,
                    TimeUnit.SECONDS,
                    new LinkedBlockingDeque<>(),
                    new ThreadFactory() {
                        @Override
                        public Thread newThread(Runnable r) {
                            return new Thread(r);
                        }
                    });
            int n = new Scanner(System.in).nextInt();
            for (int i = 0; i < n; i++) {
                final int tmp = i;
                factory.execute(new Runnable() {
                    @Override
                    public void run() {
                        new Task(tmp).run();
                    }
                });
            }
            factory.shutdown();
        } catch (Exception ignored) {
        }

    }
}
