package com.dollecontroller.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.dollecontroller.actuators.Actuator;
import com.dollecontroller.actuators.ActuatorConstructor;

public enum Input {

	LEFT_JOYSTICK	(new Vector3(-1.6f, -.7f, 1.8f), "images/joystick.png", "Linker genotsstok"),

	RIGHT_JOYSTICK	(new Vector3(1.6f, -.7f, 1.8f), "images/joystick.png", "Rechter genotsstok"),

	LEFT			(new Vector3(-3.7f, .75f, 1.4f), "images/leftButton.png", "Pijltje naar links"),
	RIGHT			(new Vector3(-2.4f, .75f, 1.4f), "images/rightButton.png", "Pijltje naar rechts"),
	UP				(new Vector3(-3.1f, 1.3f, 1.4f), "images/upButton.png", "Pijltje naar boven"),
	DOWN			(new Vector3(-3.1f, .1f, 1.4f), "images/downButton.png", "Pijltje naar beneden"),

	SQUARE			(new Vector3(3.7f, .75f, 1.4f), "images/square.png", "Vierkantje"),
	CIRCLE			(new Vector3(2.4f, .75f, 1.4f), "images/circle.png", "Rondje"),
	TRIANGLE		(new Vector3(3.1f, 1.3f, 1.4f), "images/triangle.png", "Driehoekje"),
	CROSS			(new Vector3(3.1f, .1f, 1.4f), "images/cross.png", "Kruisje"),

	L1				(new Vector3(-3.1f, 2.2f, .3f), "images/l1.png", "L1"),
	L2				(new Vector3(-3.1f, 1.8f, -.3f), "images/l2.png", "L2"),

	R1				(new Vector3(3.1f, 2.2f, .3f), "images/r1.png", "R1"),
	R2				(new Vector3(3.1f, 1.8f, -.3f), "images/r2.png", "R2");

	public final static Input[]
			BUTTONS = {LEFT, RIGHT, UP, DOWN, CIRCLE, TRIANGLE, CROSS, SQUARE, L1, L2, R1, R2},
			JOYSTICKS = {LEFT_JOYSTICK, RIGHT_JOYSTICK};

	public String value;
	public Actuator actuator;
	public ActuatorConstructor actuatorConstructor;
	public String[] constructorArgs;

	public final Texture icon;
	public final String
			iconPath,
			name;
	public final Vector3 position;

	Input(Vector3 position, String iconPath, String name) {
		this.position = position;
		this.iconPath = iconPath;
		this.name = name;
		icon = new Texture(Gdx.files.internal(iconPath));
	}

	public float getFloat() {
		return Float.parseFloat(value);
	}

}
