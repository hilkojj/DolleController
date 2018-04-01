package com.dollecontroller.actuators;

import com.dollecontroller.input.Input;
import javafx.scene.input.KeyCode;

public class KeyActuator extends Actuator {

	private String name;
	private final int[] keyCodes;

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

	}

	@Override
	public String toString() {
		return name;
	}
}
