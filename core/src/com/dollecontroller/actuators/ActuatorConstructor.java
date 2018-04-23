package com.dollecontroller.actuators;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.dollecontroller.DolleApp;
import com.dollecontroller.input.Input;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

import static com.dollecontroller.input.Input.BUTTONS;
import static com.dollecontroller.input.Input.JOYSTICKS;

public enum ActuatorConstructor {

	JOYSTICK_MOUSE(
			"Muis bewegen",
			args -> new JoyStickMouseActuator(
					Float.parseFloat(args[0]),
					Boolean.parseBoolean(args[1])
			),
			() -> {
				return new Settings() {

					@FXML
					public Slider slider;
					@FXML
					public CheckBox invertY;

					@FXML
					public void resetSlider() {
						slider.adjustValue(1);
					}

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
	MOUSE_MOVE(
			"Beweeg muis",
			args -> new MouseMoveActuator(Integer.parseInt(args[0]), Float.parseFloat(args[1])),
			() -> {
				return new Settings() {

					@Override
					public void show(AnchorPane settingsPane) {
						showFXMLFile("fxml/mouseMoveSettings.fxml", settingsPane);
					}
				};
			},
			BUTTONS
	),
	KEY(
			"Toets indrukken",
			args -> new KeyActuator(Integer.parseInt(args[0])),
			() -> {
				return new Settings() {

					int keyCode;

					@FXML
					public Label keyLabel;

					@Override
					public void show(AnchorPane settingsPane) {
						showFXMLFile("fxml/keySettings.fxml", settingsPane);
						settingsPane.getScene().addEventFilter(KeyEvent.KEY_RELEASED, event -> {

							keyCode = event.getCode().impl_getCode();
							keyLabel.setText(event.getCode().getName());

						});
					}

					@Override
					public String[] getArgs() {
						return new String[]{"" + keyCode};
					}

					@Override
					public void setSettingsFromArgs(String[] args) {
						keyCode = Integer.parseInt(args[0]);
						for (KeyCode k : KeyCode.values())
							if (k.impl_getCode() == keyCode)
								keyLabel.setText(k.getName());
					}

				};
			},
			BUTTONS
	),
	SHORTCUT(
			"Toetsen combinati",
			args -> {
				int[] keyCodes = new int[args.length];

				for (int i = 0; i < args.length; i++)
					keyCodes[i] = Integer.parseInt(args[i]);

				return new KeyActuator(keyCodes);
			},
			() -> {
				return new Settings() {

					Array<Integer> keyCodes = new Array<>();

					@FXML
					public Label label;

					@Override
					public void show(AnchorPane settingsPane) {
						showFXMLFile("fxml/shortcutSettings.fxml", settingsPane);
						settingsPane.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {

							int keyCode = event.getCode().impl_getCode();
							addKeyCode(keyCode);

						});
					}

					@FXML
					public void reset() {
						keyCodes.clear();
						label.setText("...");
					}

					@Override
					public String[] getArgs() {
						String[] args = new String[keyCodes.size];
						for (int i = 0; i < args.length; i++)
							args[i] = String.valueOf(keyCodes.get(i));
						return args;
					}

					@Override
					public void setSettingsFromArgs(String[] args) {
						for (String arg : args)
							addKeyCode(Integer.parseInt(arg));
					}

					private void addKeyCode(int keyCode) {
						for (int k : keyCodes)
							if (k == keyCode)
								return;

						keyCodes.add(keyCode);

						String name = null;
						for (KeyCode k : KeyCode.values()) {
							if (k.impl_getCode() == keyCode) {
								name = k.getName();
								break;
							}
						}
						if (name == null)
							return;

						String s = (keyCodes.size == 1 ? "" : label.getText() + " + ") + name;
						label.setText(s);
					}

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

	public static void saveConfig() {

		FileHandle f = Gdx.files.local("dollecontroller/" + DolleApp.configName + ".dollecontroller");

		String s = "";

		for (Input i : Input.values()) {

			if (i.actuator == null)
				continue;

			s += i.name() + "->" + i.actuatorConstructor.name() + "(";

			boolean first = true;

			for (String arg : i.constructorArgs) {
				s += (!first ? ", " : "") + arg;
				first = false;
			}
			s += ")\n";

		}

		f.writeString(s, false);

	}

	public static void loadConfig(String name) {

		FileHandle f = Gdx.files.local("dollecontroller/" + name + ".dollecontroller");

		if (!f.exists())
			f = Gdx.files.internal("defaultConfig.dollecontroller");

		try {

			String[] lines = f.readString().split("\\r?\\n");

			for (String l : lines) {

				String[] keyAndValue = l.split("->");

				Input i = Input.valueOf(keyAndValue[0]);

				ActuatorConstructor a = ActuatorConstructor.valueOf(keyAndValue[1].split("\\(")[0]);
				String[] args = keyAndValue[1].split("\\(")[1].split("\\)")[0].split(", ");

				i.actuatorConstructor = a;
				i.constructorArgs = args;
				i.actuator = i.actuatorConstructor.constructor.construct(args);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		DolleApp.configName = name;
	}

}
