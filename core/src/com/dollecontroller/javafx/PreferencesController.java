package com.dollecontroller.javafx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.dollecontroller.DolleApp;
import com.dollecontroller.actuators.ActuatorConstructor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.util.HashMap;

public class PreferencesController {

	public static final HashMap<String, String> preferences = new HashMap<>();

	@FXML
	public ChoiceBox<String> configDropDown;

	@FXML
	public void initialize() {

		FileHandle[] files = Gdx.files.local("dollecontroller/").list(); // these are all the save files
		for (FileHandle file : files) {

			if (file.toString().endsWith(".dollecontroller")) {

				String name = file.toString().replace(".dollecontroller", "").replace("dollecontroller/", "");
				configDropDown.getItems().add(name);

				if (name.equals(DolleApp.configName))
					configDropDown.getSelectionModel().select(name);

			}
		}

	}

	@FXML
	public void onConfigSelect(ActionEvent actionEvent) {

		String selected = configDropDown.getValue();

		DolleApp.configName = selected;
		ActuatorConstructor.loadConfig(selected);

		preferences.put("lastConfig", selected);
	}

	@FXML
	public void close() {
		((Stage) configDropDown.getScene().getWindow()).close();
		DolleApp.dialogs--;
	}

}
