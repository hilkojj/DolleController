package com.dollecontroller.actuators;

import com.dollecontroller.input.Input;

public class MouseActuator extends Actuator {

	private float sensitivity;
	private boolean invertY;

	public MouseActuator(float sensitivity, boolean invertY) {
		this.sensitivity = sensitivity;
		this.invertY = invertY;
	}

	@Override
	public void update(Input i) {

	}

	@Override
	public String toString() {
		return "Muis" + (invertY ? " (Y-as omkeren)" : "");
	}
}
