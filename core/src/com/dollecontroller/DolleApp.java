package com.dollecontroller;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dollecontroller.actuators.ActuatorConstructor;
import com.dollecontroller.input.Input;
import com.dollecontroller.input.InputProcessor;
import com.dollecontroller.javafx.PreferencesController;
import com.dollecontroller.libgdx.UI;
import com.dollecontroller.libgdx.View3D;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.File;

public class DolleApp extends Application implements ApplicationListener {

	public static final int WIDTH = 1600, HEIGHT = 900;
	public static final String DEFAULT_CONFIG_NAME = "Configuratie 1";

	public static InputProcessor inputProcessor;
	public static View3D view3D;
	public static UI ui;
	public static boolean running = true, inFront = true;
	public static String configName = "Configuratie 1";
	public static int dialogs;

	private String prevBackgroundPath;
	private Texture background;
	private SpriteBatch spriteBatch;

	@Override
	public void create () {
		inputProcessor = new InputProcessor();
		view3D = new View3D();
		ui = new UI();

		PreferencesController.loadPreferences();
		ActuatorConstructor.loadConfig(PreferencesController.PREFERENCES.getOrDefault("lastConfig", DEFAULT_CONFIG_NAME));

		background = new Texture(Gdx.files.internal("images/background.png"));
		spriteBatch = new SpriteBatch();

		new Thread(Application::launch).start();
		new Thread(() -> {

			long prevTime = System.currentTimeMillis();

			while (running) {

				long time = System.currentTimeMillis();
				long delta = time - prevTime;

				if (inputProcessor.status == InputProcessor.Status.CONNECTED) {

					for (Input i : Input.values())
						if (i.actuator != null && i.value != null)
							i.actuator.update(i, delta);
				}

				if (delta < 5) {
					try {
						Thread.sleep(5 - delta);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				prevTime = time;
			}

		}, "Actuators").start();
	}

	@Override
	public void render () {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		String backgroundPath = PreferencesController.PREFERENCES.getOrDefault("backgroundPath", "");
		if (!backgroundPath.equals(prevBackgroundPath)) {
			prevBackgroundPath = backgroundPath;

			FileHandle f = new FileHandle(new File(backgroundPath));
			if (!f.exists()) {
				f = Gdx.files.internal("images/background.png");
				PreferencesController.PREFERENCES.put("backgroundPath", "");
				PreferencesController.savePreferences();
				prevBackgroundPath = "";
			}

			background = new Texture(f);
		}

		// render background (stretched)
		spriteBatch.begin();
		spriteBatch.draw(background, 0, 0, WIDTH, HEIGHT);
		spriteBatch.end();

		view3D.render();
		ui.render();
	}

	@Override
	public void pause() {
		if (dialogs > 0)
			return;
		inFront = false;
		Gdx.graphics.setContinuousRendering(false);
	}

	@Override
	public void resume() {
		inFront = true;
		Gdx.graphics.setContinuousRendering(true);
	}

	@Override
	public void resize(int width, int height) {
		view3D.resize(width, height);
		ui.resize(width, height);
	}

	@Override
	public void dispose() {
		running = false;
	}

	@Override
	public void start(Stage primaryStage) {
		Platform.setImplicitExit(false);
	}

}
