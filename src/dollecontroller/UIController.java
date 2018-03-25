package dollecontroller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UIController implements Runnable {

	@FXML
	public Label statusLabel;

	@FXML
	public void initialize() {

		new Thread(this).start();

	}

	@Override
	public void run() {

		Runnable updateStatus = () -> statusLabel.setText(Main.INPUT_PROCESSOR.statusInfo);

		while (Main.running) {

			Platform.runLater(updateStatus);

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

}
