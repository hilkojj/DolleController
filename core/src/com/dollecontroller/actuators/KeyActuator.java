package com.dollecontroller.actuators;

import com.dollecontroller.input.Input;
import javafx.scene.input.KeyCode;

public class KeyActuator extends Actuator {

	private String name;
	private final int[] keyCodes;
	private boolean prevPressed;

	public KeyActuator(int... keyCodes) {

		this.keyCodes = keyCodes;

		name = "";

		for (int keyInt : keyCodes)
			for (KeyCode k : KeyCode.values())
				if (k.ordinal() == keyInt)
					name += (name.length() == 0 ? "" : " + ") + k.getName();


	}

	@Override
	public void update(Input i, long deltaTime) {

		boolean pressed = digitalRead(i);

		if (pressed && !prevPressed)
			for (int keyCode : keyCodes)
				robot.keyPress(keyCode);

		if (!pressed && prevPressed)
			for (int keyCode : keyCodes)
				robot.keyRelease(keyCode);

		prevPressed = pressed;
	}

	@Override
	public String toString() {
		return name;
	}

}
