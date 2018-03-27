package com.dollecontroller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class UI {

	private OrthographicCamera cam = new OrthographicCamera();
	private SpriteBatch batch = new SpriteBatch();
	private Sprite box = new Sprite(new Texture(Gdx.files.internal("box.png")));

	public void render() {

		batch.setProjectionMatrix(cam.combined);
		batch.begin();



		batch.end();

	}

	public void resize(int width, int height) {
		cam.setToOrtho(false, width, height);
	}

}
