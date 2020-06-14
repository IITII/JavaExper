import javafx.application.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client extends Application {

    public static class Record {

        private final StringProperty busNumber = new SimpleStringProperty();

        private final StringProperty busStationName = new SimpleStringProperty();

        public Record(String busStationName) {
            this.busStationName.setValue(busStationName);
        }

        public StringProperty busNumberProperty() {
            return busNumber;
        }

        public StringProperty busStationNameProperty() {
            return busStationName;
        }
    }

    @Override
    public void start(final Stage primaryStage) {
        final ObservableList<Record> data = FXCollections.observableArrayList();

        final TableColumn<Record, String> statusColumn = new TableColumn<Record, String>("该站公交车数量");
        statusColumn.setCellValueFactory(new PropertyValueFactory<Record, String>("busNumber"));

        final TableColumn<Record, String> busStationNameColumn = new TableColumn<Record, String>("公交站名");
        busStationNameColumn.setCellValueFactory(new PropertyValueFactory<Record, String>("busStationName"));

        final TableView<Record> tableView = new TableView<Record>();
        statusColumn.setSortable(false);
        busStationNameColumn.setSortable(false);
        tableView.getColumns().add(statusColumn);
        tableView.getColumns().add(busStationNameColumn);
        tableView.setItems(data);

        final TextField searchTextField = new TextField();
        searchTextField.setPromptText("请输入要查询的公交路线");
        searchTextField.setOnAction(new EventHandler<ActionEvent>() {
            private Thread thread;
            private Timer timer;
            public void handle(ActionEvent event) {
                try {
                    if (timer != null) {
                        timer.cancel();
                    }
                    if (thread != null) {
                        thread.interrupt();
                    }
                    final int busLineNumber = Integer.parseInt(searchTextField.getCharacters().toString());
                    thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                Socket socket = new Socket(InetAddress.getLocalHost(), 2333);
                                final Scanner scanner = new Scanner(socket.getInputStream());
                                final PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                                printWriter.println(1);
                                printWriter.println(busLineNumber);
                                printWriter.flush();
                                final ArrayList<String> busStationNames = new ArrayList<String>();
                                while (scanner.hasNextLine()) {
                                    String busStationName = scanner.nextLine();
                                    if (busStationName.length() == 0) {
                                        break;
                                    }
                                    else {
                                        busStationNames.add(busStationName);
                                    }
                                }
                                Platform.runLater(new Runnable() {
                                    public void run() {
                                        data.clear();
                                        for (String busStationName : busStationNames) {
                                            data.add(new Record(busStationName));
                                        }
                                    }
                                });
                                timer = new Timer();
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        printWriter.println(2);
                                        printWriter.println(busLineNumber);
                                        printWriter.flush();
                                        final int[] busStationIdStatusMap = new int[busStationNames.size()];
                                        while (scanner.hasNextLine()) {
                                            String line = scanner.nextLine();
                                            if (line.length() == 0) {
                                                break;
                                            }
                                            busStationIdStatusMap[Integer.parseInt(line)]++;
                                        }
                                        Platform.runLater(new Runnable() {
                                            public void run() {
                                                for (int i = 0; i < busStationIdStatusMap.length; i++) {
                                                    Record record = data.get(i);
                                                    record.busNumberProperty().setValue(busStationIdStatusMap[i] == 0 ? "" : Integer.toString(busStationIdStatusMap[i]));
                                                    data.set(i, record);
                                                }
                                            }
                                        });
                                    }
                                }, 0, 1000);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();
                }
                catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });

        VBox root = new VBox(searchTextField, tableView);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
