import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author IITII
 */
public class Client extends Application {
    private final static int MAX_INPUT = 50;
    private final static int PORT = 23333;

    @Override
    public void start(Stage primaryStage) {
        //判断是否已经清除提示信息
        AtomicReference<Boolean> uncleaned = new AtomicReference<>(true);
        Pane root = new Pane();
        root.setPrefSize(600, 400);
        root.setPadding(new Insets(5, 5, 5, 5));
        primaryStage.setTitle("web-作业3-TCP");
        //输入框
        HBox inputArea = new HBox();
        inputArea.setPrefWidth(root.getPrefWidth());
        inputArea.setPadding(new Insets(5, 5, 5, 5));
        inputArea.prefWidthProperty().bind(primaryStage.widthProperty());
        TextField textField = new TextField();
        textField.maxWidthProperty().bind(primaryStage.widthProperty());
        textField.setPromptText("以空格分隔数字");
        Button button = new Button("计算");
        button.setMinSize(55, 25);
        textField.setPrefWidth(530);
        //显示框
        TextArea outputArea = new TextArea();
        outputArea.setPadding(new Insets(5, 5, 5, 5));
        outputArea.prefWidthProperty().bind(primaryStage.widthProperty());
        outputArea.setPrefHeight(350);
        outputArea.setText("请输入数据以计算...\n");
        outputArea.setEditable(false);
        //事件监听
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss:");
        button.setOnMouseClicked(event -> {
            try {
                String inputText = textField.getText().trim();
                if ("".equals(inputText.replace(" ", ""))) {
                    textField.setTooltip(new Tooltip("不能为空"));
                    if (uncleaned.get()) {
                        outputArea.clear();
                        uncleaned.set(false);
                    }
                    outputArea.appendText(simpleDateFormat.format(new Date()) + " 输入框不能为空\n");
                    return;
                }
                String[] inputArray = inputText.split("\\s+");
                if (inputArray.length > MAX_INPUT) {
                    outputArea.setText(simpleDateFormat.format(new Date())
                            + " 输入数据过多，请重新输入"
                            + "\n");
                    return;
                }
                Socket socket = new Socket("localhost", PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                output.writeInt(inputArray.length);
                for (String i : inputArray) {
                    output.writeDouble(Double.parseDouble(i));
                }
                output.flush();
                outputArea.appendText(simpleDateFormat.format(new Date())
                        + "总计 " + inputArray.length
                        + " 个数，总和为: " + String.format("%.2f", input.readDouble())
                        + ", 平均数为: " + String.format("%.2f", input.readDouble())
                        + ", 方差为: " + String.format("%.2f", input.readDouble())
                        + "\n"
                );
            } catch (NumberFormatException e) {
                outputArea.clear();
                outputArea.appendText(simpleDateFormat.format(new Date())
                        + "数据输入出错，请检查"
                        + "\n");
            } catch (ConnectException e) {
                if (uncleaned.get()) {
                    outputArea.clear();
                    uncleaned.set(false);
                }
                outputArea.appendText(simpleDateFormat.format(new Date())
                        + "服务器连接失败，请检查"
                        + "\n");
            } catch (IOException e) {
                if (uncleaned.get()) {
                    outputArea.clear();
                    uncleaned.set(false);
                }
                outputArea.appendText(simpleDateFormat.format(new Date())
                        + "数据解析失败，请检查"
                        + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        //设置布局
        inputArea.getChildren().addAll(textField, button);
        root.getChildren().addAll(new VBox(inputArea, outputArea));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
