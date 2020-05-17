package lab.lab2;

/**
 * @author IITII
 */
public class FourthProblem {

    private static final int COUNT = 30;

    public static void main(String[] args) throws Exception {
        Thread[] threads = new Thread[COUNT];
        final Counter counter = new Counter();
        for (int i = 0; i < COUNT; i++) {
            threads[i] = new Thread(counter::decrement);
            threads[i].start();
        }
        for (int i = 0; i < COUNT; i++) {
            threads[i].join();
        }
    }
}

class Counter {
    private int c = 30;

    public void decrement() {
        if (c > 0) {
            c--;
        }
        System.out.println(Thread.currentThread() + "=" + c);
    }
}
