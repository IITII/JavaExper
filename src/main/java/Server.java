import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author IITII
 */
public class Server extends Application {
    private final static int PORT = 23333;

    private static long[] getData(DatagramSocket socket) {
        try {
            // 客户端 UUID 高 64 位 + 客户端 UUID 低 64 位 + 64 位时间值 + 64 位温度值
            byte[] buffer = new byte[8 + 8 + 8 + 8];
            long[] data = new long[4];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            if (packet.getLength() != buffer.length) {
                return null;
            }
            // 获取温度数据
            DataInputStream input = new DataInputStream(new ByteArrayInputStream(buffer, 0, packet.getLength()));
            for (int i = 0; i < data.length; i++) {
                data[i] = input.readLong();
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class Task implements Runnable {
        private final LineChart<Number, Number> lineChart;
        private final Label statusLabel;

        public Task(LineChart<Number, Number> lineChart, Label statusLabel) {
            this.lineChart = lineChart;
            this.statusLabel = statusLabel;
        }

        @Override
        public void run() {
            try {
                int currentClientNumber = 0;
                HashMap<UUID, XYChart.Series<Number, Number>> seriesLines = new HashMap<>();
                HashMap<Integer, XYChart.Data<Number, Number>> timeAverageDataMap = new HashMap<>();
                HashMap<Integer, Integer> timeClientNumbersMap = new HashMap<>();
                HashMap<Integer, Integer> timeSumTemperatureMap = new HashMap<>();
                DatagramSocket socket = new DatagramSocket(PORT);
                XYChart.Series<Number, Number> averageSeries = new XYChart.Series<>();
                averageSeries.setName("平均温度");
                for (; ; ) {
                    long[] data = getData(socket);
                    if (data == null) {
                        return;
                    }
                    UUID uuid = new UUID(data[0], data[1]);
                    final int time = (int) data[2];
                    final int temperature = (int) data[3];
                    timeClientNumbersMap.put(time, timeClientNumbersMap.getOrDefault(time, 0) + 1);
                    timeSumTemperatureMap.put(time, timeSumTemperatureMap.getOrDefault(time, 0) + temperature);
                    XYChart.Series<Number, Number> series = seriesLines.get(uuid);
                    if (series == null) {
                        series = new XYChart.Series<>();
                        seriesLines.put(uuid, series);
                        currentClientNumber++;
                        series.setName("客户端 " + uuid.toString());
                        XYChart.Series<Number, Number> finalSeries = series;
                        Platform.runLater(() -> {
                            lineChart.getData().add(finalSeries);
                            if (seriesLines.size() == 1) {
                                lineChart.getData().add(averageSeries);
                            }
                        });
                    }
                    XYChart.Series<Number, Number> finalSeries = series;
                    Platform.runLater(() -> {
                        double average = timeSumTemperatureMap.getOrDefault(time, 0);
                        if (average != 0) {
                            average /= timeClientNumbersMap.getOrDefault(time, 1);
                            finalSeries.getData().add(new XYChart.Data<>(time, temperature));
                            XYChart.Data<Number, Number> averageData = timeAverageDataMap.get(time);
                            if (averageData == null) {
                                averageData = new XYChart.Data<>();
                                averageData.setXValue(time);
                                timeAverageDataMap.put(time, averageData);
                                averageSeries.getData().add(averageData);
                            }
                            averageData.setYValue(average);
                        }
                    });
                }
            } catch (Exception e) {
                Platform.runLater(() -> statusLabel.setText(e.getLocalizedMessage()));
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        NumberAxis xAxis = new NumberAxis(), yAxis = new NumberAxis();
        xAxis.setLabel("时间 (s)");
        yAxis.setLabel("温度 (°C)");
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("温度曲线图");
        // 参数设置
        xAxis.setAutoRanging(true);
        // 动画设置，画面异常可关闭
        lineChart.setAnimated(true);
        // 关键点数值
        lineChart.setCreateSymbols(true);

        Label statusLabel = new Label();
        VBox root = new VBox();
        root.getChildren().addAll(lineChart, new Pane(statusLabel));

        ThreadPoolExecutor factory = new ThreadPoolExecutor(1,
                10,
                5,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                (ThreadFactory) Thread::new);
        factory.execute(new Task(lineChart, statusLabel));
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("web-实验4-udp");
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.show();
        factory.shutdown();
    }
}
