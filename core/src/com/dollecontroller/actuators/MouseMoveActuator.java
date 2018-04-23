package com.dollecontroller.actuators;

import com.dollecontroller.input.Input;

public class MouseMoveActuator extends Actuator {

	private int direction;
	private float speed;
	private String name;

	public MouseMoveActuator(int direction, float speed) {
		this.direction = direction;
		this.speed = speed;
		name = "Muis naar " + new String[]{"boven", "links", "onder", "rechts"}[direction];
	}

	@Override
	public void update(Input i, long deltaTime) {



	}

	@Override
	public String toString() {
		return name;
	}
}
