package com.dollecontroller;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class DolleApp extends ApplicationAdapter {

	public static Array<Actuator> actuators;
	public static InputProcessor inputProcessor;
	public static View3D view3D;
	public static boolean running = true, inFront = true;

	private Texture background;
	private SpriteBatch spriteBatch;

	@Override
	public void create () {
		actuators = new Array<>();
		inputProcessor = new InputProcessor();
		view3D = new View3D();

		background = new Texture(Gdx.files.internal("background.png"));
		spriteBatch = new SpriteBatch();
	}

	@Override
	public void render () {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		spriteBatch.begin();
		spriteBatch.draw(background, 0, 0, 640, 480);
		spriteBatch.end();

		view3D.render();
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
	}
}
