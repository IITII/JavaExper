import java.text.NumberFormat;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author IITII
 */
public class Shop {
    /**
     * 商品数量
     */
    volatile static int shopItems = 0;
    /**
     * 最大循环次数
     */
    final static int COUNT = 20;
    /**
     * 进货和销售的次数
     */
    volatile static int totalCount = 0;
    /**
     * 最大等待时间
     */
    final static long WAIT_TIME = 2000;

    /**
     * 进货
     * 商品数目少于10时进货
     * 进货数目随机生成但不少于50
     */
    static class Buy implements Runnable {

        @Override
        public void run() {
            synchronized (this) {
                while (totalCount < COUNT) {
                    if (shopItems < 10) {
                        totalCount++;
                        int tmp = new Random().nextInt(20) + 50;
                        shopItems += tmp;
                        System.out.format("总次数：%d, 进货 %d 件, 库存：%d\n", totalCount, tmp, shopItems);
                    } else {
                        try {
                            this.notifyAll();
                            this.wait(WAIT_TIME);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * 卖出商品
     * 销售数目随机生成
     * 数目不大于商品数量
     * 2次销售之间的时间随机生成,但不大于2s
     */
    static class Sale implements Runnable {

        @Override
        public void run() {
            synchronized (this) {
                while (totalCount < COUNT) {
                    if (shopItems > 0) {
                        totalCount++;
                        Random r = new Random();
                        int tmp = r.nextInt(shopItems) + 1;
                        if (tmp > shopItems) {
                            System.out.println("假假");
                        }
                        shopItems -= tmp;
                        System.out.format("总次数：%d, 销售 %d 件, 库存：%d\n", totalCount, tmp, shopItems);
                        NumberFormat numberFormat = NumberFormat.getInstance();
                        numberFormat.setMaximumFractionDigits(2);
                        long sleepTime = (long) (Double.parseDouble(numberFormat.format(Math.random())) * 2 * 1000);
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        this.notifyAll();
                    } else {
                        try {
                            this.notifyAll();
                            this.wait(WAIT_TIME);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        ThreadPoolExecutor factory = new ThreadPoolExecutor(5,
                10,
                5,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                (ThreadFactory) Thread::new);
        factory.execute(new Sale());
        factory.execute(new Buy());
        factory.shutdown();
    }
}
