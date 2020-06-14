package c;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author IITII
 */
public class Bus {
    private final static String SERVER_ADDR = "127.0.0.1";
    private final static int PORT = 23333;
    public final static String[] busLine = {
            "南路，大岗，生米，九龙湖南，市民中心，鹰潭街，国博，西路南",
            "龙岗，国体中心，卧龙山，岭北，前湖大道，学府大道东，翠苑路",
            "地铁大厦，卫东，绿茵路，庐山南大道，珠江路，长江路，孔目湖，双港"
    };

    public static void main(String[] args) {
        ThreadPoolExecutor factory = new ThreadPoolExecutor(6,
                10,
                5,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                (ThreadFactory) Thread::new);
        for (int i = 0; i < busLine.length; i++) {
            factory.execute(new BusTask(SERVER_ADDR, PORT, i, busLine[i].split("，").length, "小神龙-" + i, true));
        }
        int i = 0;
        factory.execute(new BusTask(SERVER_ADDR, PORT, i, busLine[i].split("，").length, "大神龙-" + i, false));
        factory.shutdown();
    }
}
