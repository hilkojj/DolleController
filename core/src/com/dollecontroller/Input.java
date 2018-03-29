package com.dollecontroller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public enum Input {

	LEFT_JOYSTICK	(new Vector3(-1.6f, -.7f, 1.8f), "images/joystick.png"),

	RIGHT_JOYSTICK	(new Vector3(1.6f, -.7f, 1.8f), "images/joystick.png"),

	LEFT			(new Vector3(-3.7f, .75f, 1.4f), "images/leftButton.png"),
	RIGHT			(new Vector3(-2.4f, .75f, 1.4f), "images/rightButton.png"),
	UP				(new Vector3(-3.1f, 1.3f, 1.4f), "images/upButton.png"),
	DOWN			(new Vector3(-3.1f, .1f, 1.4f), "images/downButton.png"),

	SQUARE			(new Vector3(3.7f, .75f, 1.4f), "images/square.png"),
	CIRCLE			(new Vector3(2.4f, .75f, 1.4f), "images/circle.png"),
	TRIANGLE		(new Vector3(3.1f, 1.3f, 1.4f), "images/triangle.png"),
	CROSS			(new Vector3(3.1f, .1f, 1.4f), "images/cross.png"),

	L1				(new Vector3(-3.1f, 2.2f, .3f), "images/l1.png"),
	L2				(new Vector3(-3.1f, 1.8f, -.3f), "images/l2.png"),

	R1				(new Vector3(3.1f, 2.2f, .3f), "images/r1.png"),
	R2				(new Vector3(3.1f, 1.8f, -.3f), "images/r2.png");

	String value;
	Actuator actuator;
	final Texture icon;
	final Vector3 position;

	Input(Vector3 position, String iconPath) {
		this.position = position;
		icon = new Texture(Gdx.files.internal(iconPath));
	}

	float getFloat() {
		return Float.parseFloat(value);
	}

}
