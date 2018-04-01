package com.dollecontroller.javafx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.dollecontroller.DolleApp;
import com.dollecontroller.actuators.ActuatorConstructor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PreferencesController {

	public static final HashMap<String, String> PREFERENCES = new HashMap<>();
	public static final String PATH = "dollecontroller/dolle.voorkeuren";

	public static void savePreferences() {

		String s = "";
		for (Map.Entry<String, String> entry : PREFERENCES.entrySet())
			s += entry.getKey() + "->" + entry.getValue() + "\n";

		FileHandle f = Gdx.files.local(PATH);
		f.writeString(s, false);
	}

	public static void loadPreferences() {

		try {

			FileHandle f = Gdx.files.local(PATH);

			if (!f.exists())
				return;

			String s = f.readString();

			String[] lines = s.split("\\r?\\n");

			for (String l : lines) {

				String[] keyAndValue = l.split("->");
				String value = "";
				for (int i = 1; i < keyAndValue.length; i++)
					value += keyAndValue[i];

				PREFERENCES.put(keyAndValue[0], value);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	public ChoiceBox<String> configDropDown;

	@FXML
	public void initialize() {

		boolean selected = false;

		FileHandle[] files = Gdx.files.local("dollecontroller/").list();
		for (FileHandle file : files) {

			if (file.toString().endsWith(".dollecontroller")) {

				String name = file.toString().replace(".dollecontroller", "").replace("dollecontroller/", "");
				configDropDown.getItems().add(name);

				if (name.equals(DolleApp.configName)) {
					configDropDown.getSelectionModel().select(name);
					selected = true;
				}

			}
		}

		if (configDropDown.getItems().size() == 0)
			configDropDown.getItems().add(DolleApp.DEFAULT_CONFIG_NAME);

		if (!selected)
			configDropDown.getSelectionModel().select(0);
	}

	@FXML
	public void onConfigSelect(ActionEvent actionEvent) {

		String selected = configDropDown.getValue();

		DolleApp.configName = selected;
		ActuatorConstructor.loadConfig(selected);

		PREFERENCES.put("lastConfig", selected);
		savePreferences();
	}

	@FXML
	public void close() {
		((Stage) configDropDown.getScene().getWindow()).close();
		DolleApp.dialogs--;
	}

	@FXML
	public void newConfig(MouseEvent mouseEvent) {

		close();

		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Niwe configuratie");
		dialog.setHeaderText("Maak een niwe configuratie");
		dialog.setContentText("Naam:");

		Optional<String> result = dialog.showAndWait();
		result.ifPresent(name -> {

			int maxLength = 30;

			boolean
					tooLong = name.length() > maxLength,
					tooShort = name.length() == 0;

			if (tooLong || tooShort) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Fout");
				alert.setHeaderText("(•̀o•́)ง");
				alert.setContentText(
						tooLong ?
								"Naam mag nit langer dan " + maxLength + " tekens."
								:
								"Naam mag nit leeg zijn"
				);
				alert.showAndWait().ifPresent(buttonType -> newConfig(null));
				return;
			}

			DolleApp.configName = name;
			PREFERENCES.put("lastConfig", name);
			savePreferences();
			ActuatorConstructor.saveConfig();

		});

	}

	public void deleteConfig(MouseEvent mouseEvent) {

		close();

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Verwijderen");
		alert.setHeaderText("ʕ•ᴥ•ʔ");
		alert.setContentText("hoi weet je het zeker??!!");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){

			try {

				FileHandle f = Gdx.files.local("dollecontroller/" + DolleApp.configName + ".dollecontroller");
				f.delete();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		DolleApp.ui.showPreferences();

	}

}
