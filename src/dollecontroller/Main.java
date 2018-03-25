package dollecontroller;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Scanner;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
		primaryStage.setTitle("Dolle controller !!");
		primaryStage.setScene(new Scene(root, 700, 600));
		primaryStage.show();



		new Thread(() -> {
			Robot robot;
			try {
				robot = new Robot();
			} catch (AWTException e) {
				e.printStackTrace();
				return;
			}

//			long prevTime = System.currentTimeMillis();
//			while (true) {
//				long time = System.currentTimeMillis();
//
//				if (time - prevTime > 100) {
////					robot.mouseMove((int) (Math.random() * 1000), (int) (Math.random() * 1000));
//					robot.keyPress(87);
//					robot.delay(100);
//					robot.keyRelease(87);
//					prevTime = time;
//				}
//			}

			SerialPort ports[] = SerialPort.getCommPorts();
			SerialPort port = ports[0];

			if (!port.openPort()) {
				System.out.println("poep");
				return;
			}

			port.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);

			Scanner data = new Scanner(port.getInputStream());

			int x, y;
			x = y = 0;

			loop:
			while (true) {

				if (data.hasNextLine()) {

					String line = data.nextLine();

					System.out.println(line);

					if (line.length() < 3)
						continue;

					int value = 0;

					try {
						value = Integer.parseInt(line.substring(2));
					} catch (Exception e) {
						continue loop;
					}

					System.out.println(value);

					switch (line.charAt(0)) {

						case 'x':
							x = 1024 - value;
							break;

						case 'y':
							y = value;
							break;

						case 'k':
							if (value == 5) {
								robot.mousePress(InputEvent.BUTTON1_MASK);
								robot.delay(20);
								robot.mouseRelease(InputEvent.BUTTON1_MASK);
							} else
								robot.keyPress(value == 6 ? 87 : (value == 3 ? 65 : (value == 2 ? 83 : 68)));
							break;

						case 'r':
							for (int i : new int[]{87, 65, 83, 68})
								robot.keyRelease(i);
							break;


					}
				}

				Point pointer = MouseInfo.getPointerInfo().getLocation();

				System.out.println(pointer);

				robot.mouseMove((int) pointer.getX() + (x - 512) / 100, (int) pointer.getY() + (y - 512) / 100);
			}


		}).start();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
