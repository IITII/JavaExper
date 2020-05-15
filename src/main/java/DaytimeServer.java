import java.io.*;
import java.net.*;
import java.util.*;

public class DaytimeServer {
    private static class Worker extends Thread {

        private final Socket socket;

        public Worker(Socket server) {
            this.socket = server;
        }

        @Override
        public void run() {
            try {
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                writeTime(out);
                out.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(2007);
            while (true) {
                Socket s = ss.accept();
//                DataOutputStream out = new DataOutputStream(
//                        s.getOutputStream());
//                writeTime(out);
//                out.close();
                new Worker(s).start();
                System.out.println("new client");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeTime(DataOutputStream out) throws IOException {
        Calendar current = Calendar.getInstance();
        out.writeInt(current.get(Calendar.YEAR));
        out.writeByte(current.get(Calendar.MONTH));
        out.writeByte(current.get(Calendar.DAY_OF_MONTH));
        out.writeByte(current.get(Calendar.HOUR_OF_DAY));
        out.writeByte(current.get(Calendar.MINUTE));
        out.writeByte(current.get(Calendar.SECOND));
    }
}