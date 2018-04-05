package com.dollecontroller.actuators;

import com.dollecontroller.input.Input;

import java.awt.*;

public abstract class Actuator {

	static Robot robot;

	static {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public abstract void update(Input i, long deltaTime);

	public boolean digitalRead(Input i) {
		return i.value.equals("1"); // todo??
	}

}
