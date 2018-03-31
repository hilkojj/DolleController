package com.dollecontroller.javafx;

import com.dollecontroller.actuators.ActuatorConstructor;
import com.dollecontroller.input.Input;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class EditDialogController {

	@FXML
	public AnchorPane settingsPane;
	@FXML
	public ChoiceBox<ActuatorConstructor> dropDown;

	private ActuatorConstructor.Settings settings;
	private Input input;

	public void init(Input input) {

		this.input = input;

		for (ActuatorConstructor a : ActuatorConstructor.values()) {

			for (Input i : a.inputs) {
				if (i == input) {
					addToDropDown(a);
					break;
				}
			}
		}
	}

	private void addToDropDown(ActuatorConstructor a) {
		dropDown.getItems().add(a);

		if (input.actuatorConstructor == a)
			dropDown.getSelectionModel().select(a);
	}

	@FXML
	public void apply() {
		input.actuatorConstructor = dropDown.getValue();
		input.constructorArgs = settings.getArgs();
		input.actuator = input.actuatorConstructor.constructor.construct(input.constructorArgs);

		close();
	}

	@FXML
	public void close() {
		((Stage) dropDown.getScene().getWindow()).close();
	}

	@FXML
	public void onSelect(ActionEvent actionEvent) {

		ActuatorConstructor a = dropDown.getValue();
		settings = a.settingsConstructor.construct();
		settingsPane.getChildren().clear();
		settings.show(settingsPane);

		if (a == input.actuatorConstructor)
			settings.setSettingsFromArgs(input.constructorArgs);

	}

}
