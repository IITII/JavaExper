import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author IITII
 */
public class RandomCoin {
    /**
     * 结果储存
     */
    volatile static double[][] state = new double[3][3];
    /**
     * 投币次数
     */
    final static int BOUND = 10;
    /**
     * 指定正面
     */
    final static Boolean FRONT = true;

    static class Task implements Runnable {
        private final int count;

        public Task(int count) {
            this.count = count;
        }

        @Override
        public void run() {
            synchronized (this) {
                int front_total = 0;
                int total = new Random().nextInt(BOUND);
                for (int i = 0; i < total; i++) {
                    if (new Random().nextBoolean() == FRONT) {
                        front_total++;
                    }
                }
                state[count][0] = front_total;
                state[count][1] = total;
                state[count][2] = total == 0 ? 0 : (float) front_total / total;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor factory = new ThreadPoolExecutor(5,
                10,
                5,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                (ThreadFactory) Thread::new);
        for (int i = 0; i < state.length; i++) {
            factory.execute(new Task(i));
        }
        factory.shutdown();
        factory.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        for (int i = 0; i < state.length; i++) {
            System.out.format("线程%d：\n 正面次数：%d\n 总次数：%d\n 概率：%.2f\n",
                    i + 1,
                    (int) state[i][0],
                    (int) state[i][1],
                    state[i][2]
            );
        }
    }
}
