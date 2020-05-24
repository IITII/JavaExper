import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author IITII
 */
public class Server extends Thread {
    private final static int PORT = 23333;
    private final DataInputStream input;

    private final DataOutputStream output;

    public Server(Socket socket) throws IOException {
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        while (true) {
            try {
                double[] numbers = new double[input.readInt()];
                // 0: 总和，1：平均，2：方差
                double[] res = new double[3];
                for (int i = 0; i < numbers.length; i++) {
                    numbers[i] = input.readDouble();
                    res[0] += numbers[i];
                }
                res[1] = res[0] / numbers.length;
                for (double i : numbers) {
                    double t = i - res[1];
                    res[2] += t * t;
                }
                res[2] /= numbers.length;
                for (double i : res) {
                    output.writeDouble(i);
                }
                output.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        while (true) {
            new Server(serverSocket.accept()).start();
        }
    }
}
