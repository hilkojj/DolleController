package com.dollecontroller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import static com.dollecontroller.DolleApp.HEIGHT;
import static com.dollecontroller.DolleApp.WIDTH;
import static com.dollecontroller.Input.*;

public class UI {

	private static class Box {

		String title;
		Vector2 position;
		Input[] inputs;

		Box(String title, Vector2 position, Input... inputs) {
			this.title = title;
			this.position = position;
			this.inputs = inputs;
		}

	}

	private Box[] boxes = {
			new Box(
					"L1 & L2",
					new Vector2(0, 64 + 530),
					L1, L2
			),
			new Box(
					"Pijltjes",
					new Vector2(0, 64 + 265),
					LEFT, RIGHT, UP, DOWN
			),
			new Box(
					"Linker Genotsstok",
					new Vector2(0, 64),
					LEFT_JOYSTICK
			),
			new Box(
					"R1 & R2",
					new Vector2(WIDTH - 380, 64 + 530),
					R1, R2
			),
			new Box(
					"Figuurtjes",
					new Vector2(WIDTH - 380, 64 + 265),
					SQUARE, CIRCLE, TRIANGLE, CROSS
			),
			new Box(
					"Rechter Genotsstok",
					new Vector2(WIDTH - 380, 64),
					RIGHT_JOYSTICK
			)
	};

	private OrthographicCamera cam = new OrthographicCamera();
	private SpriteBatch batch = new SpriteBatch();
	private Sprite boxSprite = new Sprite(new Texture(Gdx.files.internal("images/box.png")));
	private Texture
			controllerIcon = new Texture(Gdx.files.internal("images/controllerIcon.png")),
			dot = new Texture(Gdx.files.internal("images/dot.png")),
			buttonTexture = new Texture(Gdx.files.internal("images/value.png"));
	private Color infoIconColor = new Color();
	private BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"));
	private ShaderProgram buttonShader = new ShaderProgram(
			SpriteBatch.createDefaultShader().getVertexShaderSource(),
			Gdx.files.internal("glsl/button.glsl").readString()
	);
	private float pressTimer = 0;

	public void render() {

		batch.setProjectionMatrix(cam.combined);
		batch.begin();

		infoIconColor.lerp(DolleApp.inputProcessor.status.color, .3f);
		batch.setColor(infoIconColor);
		batch.draw(controllerIcon, 32, 32);
		batch.setColor(Color.WHITE);
		font.setColor(infoIconColor);
		font.draw(batch, DolleApp.inputProcessor.status.description, 32 + 60, 32 + 24);
		font.setColor(Color.WHITE);

		for (Box b : boxes)
			renderBox(b);

		batch.end();

	}

	private Vector3 temp = new Vector3();

	private void renderBox(Box box) {
		boxSprite.setPosition(box.position.x, box.position.y);
		boxSprite.draw(batch);
		font.draw(batch, box.title, box.position.x + 43, box.position.y + 238);

		Vector2 dotPos = new Vector2();
		int inputs = 0;

		for (Input i : box.inputs) {
			inputs++;
			renderInputButton(i, box.position.x + 40, box.position.y + 210 - inputs * 43);
			temp.set(i.position);
			DolleApp.view3D.cam.project(temp);
			dotPos.add(temp.x, temp.y);
		}

		dotPos.scl(1f / inputs);
		batch.draw(dot, dotPos.x - 16, dotPos.y - 16);


	}

	private void renderInputButton(Input i, float x, float y) {

		float mouseX = Gdx.input.getX();
		mouseX -= x;
		mouseX /= buttonTexture.getWidth();

		if (Gdx.input.isButtonPressed(0))
			pressTimer = Math.min(1, pressTimer + .01f);
		else
			pressTimer = Math.max(pressTimer - .006f, 0);

		if (mouseOverInputButton(x, y)) {

			batch.end();
			batch.setShader(buttonShader);
			buttonShader.begin();
			batch.begin();
			buttonShader.setUniformf("x", mouseX);
			buttonShader.setUniformf("press", pressTimer);

			batch.draw(buttonTexture, x, y);
			batch.end();
			buttonShader.end();

			batch.setShader(null);
			batch.begin();
		}

		batch.draw(i.icon, x + 5, y + 5);
	}

	private boolean mouseOverInputButton(float x, float y) {
		float mouseX = Gdx.input.getX(), mouseY = HEIGHT - Gdx.input.getY();
		return mouseX > x && mouseX < x + buttonTexture.getWidth()
				&& mouseY > y && mouseY < y + buttonTexture.getHeight();
	}

	public void resize(int width, int height) {
		cam.setToOrtho(false, width, height);
	}

}
