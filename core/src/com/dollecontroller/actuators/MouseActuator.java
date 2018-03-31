package com.dollecontroller.actuators;

import com.dollecontroller.input.Input;

import java.awt.*;

public class MouseActuator extends Actuator {

	private float sensitivity, deltaX, deltaY;
	private boolean invertY;

	public MouseActuator(float sensitivity, boolean invertY) {
		this.sensitivity = sensitivity;
		this.invertY = invertY;
	}

	@Override
	public void update(Input i, long deltaTime) {

		try {

			String[] values = i.value.split(",");

			Point pointer = MouseInfo.getPointerInfo().getLocation();

			deltaX += convert(values[0], deltaTime);
			deltaY += convert(values[1], deltaTime) * (invertY ? -1 : 1);

			int
					intX = (int) deltaX,
					intY = (int) deltaY;

			deltaX -= intX;
			deltaY -= intY;

			robot.mouseMove((int) pointer.getX() + intX, (int) pointer.getY() + intY);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private float convert(String s, long millis) {

		float value = Float.parseFloat(s);
		value -= 512;
		if (Math.abs(value) < 10)
			return 0;

		value /= 1024;
		value *= Math.abs(value);
		value *= sensitivity;
		value *= millis;
		return value;
	}

	@Override
	public String toString() {
		return "Muis" + (invertY ? " (Y-as omkeren)" : "");
	}
}
