package dollecontroller;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Random;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
		primaryStage.setTitle("Dolle controller !!");
		primaryStage.setScene(new Scene(root, 700, 600));
		primaryStage.show();

		SerialPort ports[] = SerialPort.getCommPorts();

		System.out.println(ports.length);

		new Thread(() -> {
			Robot robot;
			try {
				robot = new Robot();
			} catch (AWTException e) {
				e.printStackTrace();
				return;
			}

			long prevTime = System.currentTimeMillis();
			while (true) {
				long time = System.currentTimeMillis();

				if (time - prevTime > 100) {
					robot.mouseMove((int) (Math.random() * 1000), (int) (Math.random() * 1000));
					robot.keyPress(84);
					robot.delay(100);
					robot.keyRelease(84);
					prevTime = time;
				}
			}
		}).start();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
