package z;

import java.io.*;
import java.net.*;
import java.util.*;

public class Report extends Thread {

    private final int busLineNumber;

    private final int busStationNumber;

    private final UUID uuid = UUID.randomUUID();

    public Report(int busLineNumber, int busStationNumber) {
        this.busLineNumber = busLineNumber;
        this.busStationNumber = busStationNumber;
    }

    public void report(int busStationId) throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeLong(uuid.getMostSignificantBits());
        dataOutputStream.writeLong(uuid.getLeastSignificantBits());
        dataOutputStream.writeInt(busLineNumber);
        dataOutputStream.writeInt(busStationId);
        dataOutputStream.flush();
        DatagramPacket packet = new DatagramPacket(outputStream.toByteArray(), outputStream.size());
        packet.setAddress(InetAddress.getLocalHost());
        packet.setPort(2333);
        DatagramSocket socket = new DatagramSocket();
        socket.send(packet);
    }

    @Override
    public void run() {
        try {
            for (;;) {
                for (int i = 0; i < busStationNumber; i++) {
                    report(i);
                    sleep(1000);
                }
                for (int i = busStationNumber - 2; i > 0; i--) {
                    report(i);
                    sleep(1000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Timer().schedule(new TimerTask() {

            private int busNumber = 3;

            @Override
            public void run() {
                if (busNumber <= 0) {
                    cancel();
                }
                else {
                    busNumber--;
                }
                new Report(233, 27).start();
                new Report(1, Arrays.asList("南路，大岗，生米，九龙湖南，市民中心，鹰潭街，国博，西路南".split("，")).size()).start();
                new Report(2, Arrays.asList("龙岗，国体中心，卧龙山，岭北，前湖大道，学府大道东，翠苑路".split("，")).size()).start();
                new Report(3, Arrays.asList("地铁大厦，卫东，绿茵路，庐山南大道，珠江路，长江路，孔目湖，双港".split("，")).size()).start();
            }
        }, 0, 2000);
    }
}
