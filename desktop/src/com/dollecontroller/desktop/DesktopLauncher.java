package com.dollecontroller.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dollecontroller.DolleApp;

import static com.dollecontroller.DolleApp.DEFAULT_HEIGHT;
import static com.dollecontroller.DolleApp.DEFAULT_WIDTH;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.samples = 8;
		config.width = DEFAULT_WIDTH;
		config.height = DEFAULT_HEIGHT;
		config.title = "Dolle controller !!!!";
		new LwjglApplication(new DolleApp(), config);
	}
}
