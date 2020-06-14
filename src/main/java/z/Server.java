import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    private final int busQueryServicePort;

    private final int busReportServicePort;

    private final ConcurrentHashMap<UUID, Integer> uuidBusLineNumberMap = new ConcurrentHashMap<UUID, Integer>();

    private final ConcurrentHashMap<UUID, Integer> uuidBusStationIdMap = new ConcurrentHashMap<UUID, Integer>();

    private final static HashMap<Integer, List<String>> busStationIdNameMap = new HashMap<Integer, List<String>>();

    static {
        busStationIdNameMap.put(233, Arrays.asList(
                "高铁西客站东枢纽", "龙兴大街集嘉坊路口", "九龙大道龙兴大街口", "九龙大道北口", "省行政中心", "省交通厅", "岭北五路口",
                "岭北三路西口（招呼站）", "南航路口", "前湖大道口", "朝阳大桥丰和口", "朝阳大桥红谷口", "灌婴路北口", "南昌就业大厦",
                "朝阳洲南路", "桃苑街西口", "桃苑小区", "站前西路口（招呼站）", "绳金塔", "公交保育院", "老福山北", "八一大道永叔路口",
                "八一广场南", "口腔医院", "人民公园北门", "福州路东口", "福州路口"));
        busStationIdNameMap.put(1, Arrays.asList("南路，大岗，生米，九龙湖南，市民中心，鹰潭街，国博，西路南".split("，")));
        busStationIdNameMap.put(2, Arrays.asList("龙岗，国体中心，卧龙山，岭北，前湖大道，学府大道东，翠苑路".split("，")));
        busStationIdNameMap.put(3, Arrays.asList("地铁大厦，卫东，绿茵路，庐山南大道，珠江路，长江路，孔目湖，双港".split("，")));
    }

    private final Thread busQueryService = new Thread() {
        @Override
        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(busReportServicePort);
                for (; ; ) {
                    final Socket socket = serverSocket.accept();
                    socket.setKeepAlive(true);
                    new Thread(new Runnable() {
                        public void run() {
                            int retryTime = 3;
                            for (; ; ) {
                                try {
                                    if (retryTime <= 0) {
                                        break;
                                    }
                                    Scanner scanner = new Scanner(socket.getInputStream());
                                    PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                                    if (scanner.hasNextInt()) {
                                        int operation = scanner.nextInt();
                                        switch (operation) {
                                            /// 公交线路查询
                                            case 1: {
                                                try {
                                                    int busLineNumber = scanner.nextInt();
                                                    for (String busStationName : busStationIdNameMap.get(busLineNumber)) {
                                                        printWriter.println(busStationName);
                                                    }
                                                } catch (Exception e) {
                                                    printWriter.println();
                                                }
                                                break;
                                            }
                                            /// 公交行驶查询
                                            case 2: {
                                                int busLineNumber = scanner.nextInt();
                                                try {
                                                    for (Map.Entry<UUID, Integer> entry : uuidBusLineNumberMap.entrySet()) {
                                                        UUID uuid = entry.getKey();
                                                        if (busLineNumber == entry.getValue()) {
                                                            int busStationId = uuidBusStationIdMap.get(uuid);
                                                            printWriter.println(busStationId);
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    printWriter.println();
                                                }
                                                break;
                                            }
                                        }
                                        printWriter.println();
                                        printWriter.flush();
                                    }
                                    else {
                                        retryTime--;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    break;
                                }
                            }
                        }
                    }).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private final Thread busReportService = new Thread() {
        @Override
        public void run() {
            try {
                DatagramSocket socket = new DatagramSocket(busReportServicePort);
                /// UUID 高64位 + UUID 低 64 位 + 32 位公交车路线号 + 公交站 ID
                byte[] buffer = new byte[8 + 8 + 4 + 4];
                for (; ; ) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    if (packet.getLength() == buffer.length) {
                        DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(buffer));
                        long uuidMostSigBits = inputStream.readLong();
                        long uuidLeastSigBits = inputStream.readLong();
                        UUID uuid = new UUID(uuidMostSigBits, uuidLeastSigBits);
                        int busLineNumber = inputStream.readInt();
                        int busStationId = inputStream.readInt();
                        uuidBusLineNumberMap.put(uuid, busLineNumber);
                        uuidBusStationIdMap.put(uuid, busStationId);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public Server() {
        busQueryServicePort = busReportServicePort = 2333;
    }

    public Server(int busQueryServicePort, int busReportServicePort) {
        this.busQueryServicePort = busQueryServicePort;
        this.busReportServicePort = busReportServicePort;
    }

    public void start() {
        busQueryService.start();
        busReportService.start();
    }

    public void stop() {
        busQueryService.interrupt();
        busReportService.interrupt();
    }

    public int getBusQueryServicePort() {
        return busQueryServicePort;
    }

    public int getBusReportServicePort() {
        return busReportServicePort;
    }

    public static void main(String[] args) {
        new Server().start();
    }
}
