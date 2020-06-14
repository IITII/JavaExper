package c;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

/**
 * @author IITII
 */
public class BusTask implements Runnable {
    private final static int MAX_COUNT = 100;
    private final String serverAddr;
    private final int port;
    private final int busLine;
    private final int totalSites;
    private final String carNumber;
    private final Boolean direction;
    private int current = 0;

    /**
     * 公交车任务
     *
     * @param serverAddr 服务器IP
     * @param port       服务器端口
     * @param busLine    公交线路
     * @param totalSites 总站数
     * @param carNumber  车牌号
     * @param direction  行驶方向，true 为向右
     */
    public BusTask(String serverAddr, int port, int busLine, int totalSites, String carNumber, Boolean direction) {
        this.serverAddr = serverAddr;
        this.port = port;
        this.busLine = busLine;
        this.totalSites = totalSites;
        this.carNumber = carNumber;
        this.direction = direction;
    }

    @Override
    public void run() {
        int totalCount = 0;
        while (totalCount < MAX_COUNT) {
            totalCount++;
            try {
                DatagramSocket udpSocket = new DatagramSocket();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                // 公交路线 车牌号 当前站下标 下个站下标 方向
                //  4       4       4       4       4
                String s = busLine +
                        "#" + carNumber +
                        "#" + current +
                        "#" + getNextSite() +
                        "#" + direction;
                System.out.println(s);
                dataOutputStream.writeUTF(s);
                udpSocket.send(new DatagramPacket(
                        byteArrayOutputStream.toByteArray(),
                        byteArrayOutputStream.size(),
                        InetAddress.getByName(serverAddr),
                        port));
                current = getNextSite();
                //随机休眠
                Thread.sleep(new Random().nextInt(2000) + 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getNextSite() {
        int tmp = current;
        if (direction) {
            if (tmp + 1 <= totalSites) {
                tmp++;
            } else {
                tmp--;
            }
        } else {
            if (tmp - 1 >= 0) {
                tmp--;
            } else {
                tmp++;
            }
        }
        return tmp;
    }

}
