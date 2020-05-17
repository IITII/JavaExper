package work.online.socket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author IITII
 */
public class Server {
}


class ThreadClientSocket implements Runnable {

    private static final String HOST = "127.0.0.1";

    private static final int PORT = 2333;

    private Socket client;

    public ThreadClientSocket() {
        client = new Socket();
        try {
            client.connect(new InetSocketAddress(HOST, PORT), 500);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            OutputStream out = client.getOutputStream();
            int counter = 0;
            while(true) {
                System.out.println("Thread-→" + Thread.currentThread().getName());
                out.write("Heart Beat !".getBytes());
                System.out.println(client + ": " + (++counter));
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

class MulClientSocketDemo {


    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        int counter = 4;
        while ((counter--) > 0) {
            new Thread(new ThreadClientSocket()).start();
            ;
        }
        // client.setTcpNoDelay(true);
    }

}