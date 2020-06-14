package a;

import c.Bus;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * java 网络实验5
 * 公交车服务端
 * @author IITII
 */
public class Server {
    private final static int PORT = 23333;
    volatile ArrayList<String[]> busInfo = new ArrayList<>();

    private static Object getData(DatagramSocket socket) {
        try {
            // 客户端 UUID 高 64 位 + 客户端 UUID 低 64 位 + 64 位时间值 + 64 位温度值
            byte[] buffer = new byte[8 + 8 + 8 + 4 + 1];
            Object[] data = new Object[5];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            if (packet.getLength() != buffer.length) {
                return null;
            }
            // 获取温度数据
            DataInputStream input = new DataInputStream(new ByteArrayInputStream(buffer, 0, packet.getLength()));
            data[0] = input.readLong();
            data[1] = input.readLong();
            data[2] = input.readLong();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String[] strings = {
                "南路",
                "大岗",
                "生米",
                "九龙湖南",
                "市民中心",
                "鹰潭街",
                "国博",
                "西站南",
                "龙岗",
                "国体中心",
                "卧龙山",
                "前湖大道"
        };
        HashMap<UUID, Integer> map = new HashMap<>();

    }

    public String getBusData(DatagramSocket udpSocket) {
        try {
            // 公交路线 车牌号 当前站下标 下个站下标 方向
            //  4       4       4       4       4
            byte[] buffer = new byte[4 + 4 + 4 + 4 + 4];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            udpSocket.receive(packet);
            DataInputStream input = new DataInputStream(new ByteArrayInputStream(buffer, 0, packet.getLength()));
            return input.readUTF();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getBusInfo(String busLineNumber){
        return "";
    }
}
