package com.dollecontroller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class UI {

	private OrthographicCamera cam = new OrthographicCamera();
	private SpriteBatch batch = new SpriteBatch();
	private Sprite box = new Sprite(new Texture(Gdx.files.internal("box.png")));
	private Texture infoIcon = new Texture(Gdx.files.internal("info.png"));
	private Color infoIconColor = new Color();
	private BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"));

	public void render() {

		batch.setProjectionMatrix(cam.combined);
		batch.begin();

		infoIconColor.lerp(DolleApp.inputProcessor.status.color, .3f);
		batch.setColor(infoIconColor);
		batch.draw(infoIcon, 32, 32);
		batch.setColor(Color.WHITE);
		font.setColor(infoIconColor);
		font.draw(batch, DolleApp.inputProcessor.status.description, 32 + 60, 32 + 24);

		batch.end();

	}

	public void resize(int width, int height) {
		cam.setToOrtho(false, width, height);
	}

}
