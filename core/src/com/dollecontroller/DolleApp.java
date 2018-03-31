package com.dollecontroller;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dollecontroller.input.InputProcessor;
import com.dollecontroller.libgdx.UI;
import com.dollecontroller.libgdx.View3D;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class DolleApp extends Application implements ApplicationListener {

	public static final int WIDTH = 1600, HEIGHT = 900;

	public static InputProcessor inputProcessor;
	public static View3D view3D;
	public static UI ui;
	public static boolean running = true, inFront = true;

	private Texture background;
	private SpriteBatch spriteBatch;

	@Override
	public void create () {
		inputProcessor = new InputProcessor();
		view3D = new View3D();
		ui = new UI();

		background = new Texture(Gdx.files.internal("images/background.png"));
		spriteBatch = new SpriteBatch();
		new Thread(Application::launch).start();
	}

	@Override
	public void render () {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		// render background (stretched)
		spriteBatch.begin();
		spriteBatch.draw(background, 0, 0, WIDTH, HEIGHT);
		spriteBatch.end();

		view3D.render();
		ui.render();
	}

	@Override
	public void pause() {
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
