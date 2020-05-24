import java.io.*;
import java.net.*;
import java.util.*;

/**
 * @author IITII
 */
public class Client {
    private final static String SERVER_ADDRESS = "127.0.0.1";
    private final static int PORT = 23333;

    public static void main(String[] args) {
        try {
            new Timer().schedule(new TimerTask() {
                final UUID uuid = UUID.randomUUID();
                final Random random = new Random();
                final DatagramSocket socket = new DatagramSocket();
                int time = 0;

                @Override
                public void run() {
                    if (time > Integer.MAX_VALUE - 1) {
                        System.out.print("运行累了，休息一下\n");
                        System.exit(0);
                    }
                    try {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        DataOutputStream output = new DataOutputStream(byteArrayOutputStream);
                        long[] data = {
                                //高64位
                                uuid.getMostSignificantBits(),
                                //低64位
                                uuid.getLeastSignificantBits(),
                                //时间
                                (long) time,
                                //随机温度
                                (long) random.nextInt(40)
                        };
                        for (long i : data) {
                            output.writeLong(i);
                        }
                        socket.send(new DatagramPacket(byteArrayOutputStream.toByteArray(),
                                byteArrayOutputStream.size(),
                                InetAddress.getByName(SERVER_ADDRESS),
                                PORT));
                        System.out.println("UUID: " + uuid.toString() + " Time: " + data[2] + " Temperature: " + data[3]);
                        time++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

