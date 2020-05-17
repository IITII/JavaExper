package work.online.socket;

import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class TcpTask implements Runnable {
    private static final int BUFFER_SIZE = 1024;
    private final Socket server;

    public TcpTask(Socket socket) {
        this.server = socket;
    }

    @Override
    public void run() {
        try {
            InputStream in = server.getInputStream();
            OutputStream out = server.getOutputStream();
            byte[] recData = null;
            while (true) {
                recData = new byte[BUFFER_SIZE];
                int r = in.read(recData);
                if (r > -1) {
                    String data = new String(recData);
                    if ("over".equals(data.trim())) {
                        server.close();
                        break;
                    }
                    System.out.println("Receive：" + data);
                    out.write("Send：".getBytes());
                    out.write(recData);
                } else {
                    System.out.println("Finish!");
                    server.close();
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/**
 * @author IITII
 */
class MyThreadFactory implements ThreadFactory {
    private String name = "";
    AtomicInteger atomicInteger = new AtomicInteger(1);

    public MyThreadFactory(@NotNull String name) {
        this.name = name + "-" + atomicInteger.getAndIncrement();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, name);
        if (thread.isDaemon()) {
            thread.setDaemon(true);
        }
        if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        System.out.println("Current Thread " + name);
        return thread;
    }
}

/**
 * @author IITII
 */
public class MultiTcp {
    private static final int PORT = 2333;
    private static int COUNT = 1;

    public static void main(String[] args) throws IOException {
        ThreadPoolExecutor factory = new ThreadPoolExecutor(1,
                10,
                5,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                new MyThreadFactory("JavaSocket"));
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Listen on " + PORT);
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.print(COUNT++);
            factory.execute(new TcpTask(socket));
        }
    }
}
