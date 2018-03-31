package com.dollecontroller.actuators;

import com.badlogic.gdx.Gdx;
import com.dollecontroller.input.Input;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

import static com.dollecontroller.input.Input.BUTTONS;
import static com.dollecontroller.input.Input.JOYSTICKS;

public enum ActuatorConstructor {

	MOUSE(
			"Muis bewegen",
			args -> new MouseActuator(
					Float.parseFloat(args[0]),
					Boolean.parseBoolean(args[1])
			),
			() -> {
				return new Settings() {

					@FXML
					public Slider slider;
					@FXML
					public CheckBox invertY;

					@Override
					public void show(AnchorPane settingsPane) {
						showFXMLFile("fxml/mouseSettings.fxml", settingsPane);
					}

					@Override
					public void setSettingsFromArgs(String[] args) {
						slider.adjustValue(Double.parseDouble(args[0]));
						invertY.setSelected(Boolean.parseBoolean(args[1]));
					}

					@Override
					public String[] getArgs() {
						return new String[] {
								"" + slider.getValue(),
								String.valueOf(invertY.isSelected())
						};
					}

				};
			},
			JOYSTICKS

	),
	KEY(
			"Toets indrukken",
			args -> null,
			() -> {
				return new Settings() {
				};
			},
			BUTTONS
	);

	public final Input[] inputs;
	public final String name;
	public final Constructor constructor;
	public final SettingsConstructor settingsConstructor;

	ActuatorConstructor(
			String name,
			Constructor constructor,
			SettingsConstructor settingsConstructor,
			Input... inputs
	) {
		this.name = name;
		this.inputs = inputs;
		this.constructor = constructor;
		this.settingsConstructor = settingsConstructor;
	}

	@Override
	public String toString() {
		return name;
	}

	@FunctionalInterface
	public interface Constructor {

		Actuator construct(String[] args);

	}

	@FunctionalInterface
	public interface SettingsConstructor {

		Settings construct();

	}

	public static class Settings {

		public void show(AnchorPane settingsPane) {}

		public String[] getArgs() {
			return new String[0];
		}

		public void setSettingsFromArgs(String[] args) {}

		void showFXMLFile(String path, AnchorPane settingsPane) {

			FXMLLoader loader = new FXMLLoader();
			loader.setController(this);

			try {
				Parent p = loader.load(Gdx.files.internal(path).read());
				settingsPane.getChildren().add(p);

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}
