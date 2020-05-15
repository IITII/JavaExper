import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author IITII
 */
public class JavaFxClock extends Application {
    private static Text timeText;

    private static class Task extends TimerTask {
        @Override
        public void run() {
            timeText.setText(Client.getFormatDate());
        }
    }

    @Override
    public void start(Stage primaryStage) {
        timeText = new Text("Init");
        BorderPane pane = new BorderPane();
        pane.setPrefSize(500, 500);
        pane.setCenter(timeText);
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Java实验3");
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new Task(), 0, 1000);
        primaryStage.show();
    }
}