package lab.lab2;

import java.util.concurrent.*;

/**
 * @author IITII
 */
public class SecondProblem {
    private final static int LOOP = 10;
    private final static int COUNT = 3;

    static class Task {
        public void run() {
            for (int i = 1; i <= LOOP; i++) {
                System.out.print(i == LOOP ? i + "\n" : i + " ");
            }
        }
    }

    static class First extends Thread {
        @Override
        public void run() {
            new Task().run();
        }
    }

    static class Second implements Runnable {
        @Override
        public void run() {
            new Task().run();
        }
    }

    static class Third {
        public static void main() {
            ThreadPoolExecutor factory = new ThreadPoolExecutor(1,
                    10,
                    5,
                    TimeUnit.SECONDS,
                    new LinkedBlockingDeque<>(),
                    new ThreadFactory() {
                        @Override
                        public Thread newThread(Runnable r) {
                            return new Thread(r);
                        }
                    });
            for (int i = 1; i <= COUNT; i++) {
                factory.execute(new Runnable() {
                    @Override
                    public void run() {
                        new Task().run();
                    }
                });
            }
            factory.shutdown();
        }
    }

    public static void main(String[] args) throws Exception {
        // 第一种方法：继承 Thread 实现 run 方法
        System.out.println("方法一: 继承 Thread 实现 run 方法");
        First[] firsts = new First[3];
        for (int i = 0; i < COUNT; i++) {
            firsts[i] = new First();
            firsts[i].start();
        }
        for (int i = 0; i < COUNT; i++) {
            firsts[i].join();
        }

        // 第二种方式：Thread(Runnable)
        System.out.println("方法二: Thread(Runnable)");
        Thread[] threads = new Thread[3];
        for (int i = 0; i < COUNT; i++) {
            threads[i] = new Thread(new Second());
            threads[i].start();
        }
        for (int i = 0; i < COUNT; i++) {
            threads[i].join();
        }

        //第三种方式：ThreadFactory
        System.out.println("方法三: ThreadFactory");
        Third.main();
    }
}
