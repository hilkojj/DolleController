package com.dollecontroller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import static com.dollecontroller.DolleApp.HEIGHT;
import static com.dollecontroller.DolleApp.WIDTH;
import static com.dollecontroller.Input.*;

public class UI {

	private static class Box {

		String title;
		Vector2 position, lineBegin, dotPos = new Vector2();
		Input[] inputs;

		Box(String title, Vector2 position, Vector2 lineBegin, Input... inputs) {
			this.title = title;
			this.position = position;
			this.lineBegin = lineBegin;
			this.inputs = inputs;
		}

	}

	private Box[] boxes = {
			new Box(
					"L1 & L2",
					new Vector2(0, 64 + 530), new Vector2(360, 140),
					L1, L2
			),
			new Box(
					"Pijltjes",
					new Vector2(0, 64 + 265), new Vector2(360, 140),
					LEFT, RIGHT, UP, DOWN
			),
			new Box(
					"Linker Genotsstok",
					new Vector2(0, 64), new Vector2(360, 140),
					LEFT_JOYSTICK
			),
			new Box(
					"R1 & R2",
					new Vector2(WIDTH - 380, 64 + 530), new Vector2(40, 140),
					R1, R2
			),
			new Box(
					"Figuurtjes",
					new Vector2(WIDTH - 380, 64 + 265), new Vector2(40, 140),
					SQUARE, CIRCLE, TRIANGLE, CROSS
			),
			new Box(
					"Rechter Genotsstok",
					new Vector2(WIDTH - 380, 64), new Vector2(20, 140),
					RIGHT_JOYSTICK
			)
	};

	private OrthographicCamera cam = new OrthographicCamera();
	private SpriteBatch batch = new SpriteBatch();
	private Sprite
			boxSprite = new Sprite(new Texture(Gdx.files.internal("images/box.png"))),
			line = new Sprite(new Texture(Gdx.files.internal("images/line.png")));
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

		Vector2 newDotPos = new Vector2();
		boolean single = false;
		int inputs = 0;

		for (Input i : box.inputs) {
			inputs++;

			float bX = box.position.x + 40, bY = box.position.y + 210 - inputs * 43;

			renderInputButton(i, bX, bY);

			temp.set(i.position);
			DolleApp.view3D.cam.project(temp);

			if (mouseOverInputButton(bX, bY)) {
				single = true;
				newDotPos.set(temp.x, temp.y);
				box.lineBegin.y += bY + 20 - box.position.y;
				box.lineBegin.y /= 2;
			} else if (!single) {
				newDotPos.add(temp.x, temp.y);
				box.lineBegin.y += 140;
				box.lineBegin.y /= 2;
			}

		}

		if (!single)
			newDotPos.scl(1f / inputs);

		if (box.dotPos.isZero())
			box.dotPos.set(newDotPos);

		box.dotPos.lerp(newDotPos, .3f);

		drawBezier(box);
		batch.draw(dot, box.dotPos.x - 16, box.dotPos.y - 16);


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
		return mouseX >= x && mouseX <= x + buttonTexture.getWidth()
				&& mouseY >= y && mouseY <= y + buttonTexture.getHeight();
	}

	private Bezier<Vector2> bezier = new Bezier<>(new Vector2(), new Vector2(), new Vector2());

	private void drawBezier(Box box) {

		float[] vertices = line.getVertices();

		float
				beginX = box.position.x + box.lineBegin.x,
				beginY = box.position.y + box.lineBegin.y;
		bezier.points.get(0).set(beginX, beginY);
		bezier.points.get(2).set(box.dotPos).add(16, 0);
		bezier.points.get(1).set((box.dotPos.x + beginX) / 2, box.dotPos.y);

		Vector2
				pos = new Vector2(), prevPos = new Vector2(),
				normal = new Vector2(), prevNormal = new Vector2();

		for (float t = 0; t <= 1.05f; t += .01f) {

			bezier.valueAt(pos, t);
			pos.sub(16, 16);

			if (prevPos.isZero()) {
				prevPos.set(pos);
				continue;
			}

			normal.set(pos).sub(prevPos);
			if (box.position.x + box.lineBegin.x < box.dotPos.x)
				normal.set(-normal.y, normal.x);
			else
				normal.set(normal.y, -normal.x);
			normal.nor().scl(32);

			vertices[SpriteBatch.X1] = prevPos.x;
			vertices[SpriteBatch.Y1] = prevPos.y;
			vertices[SpriteBatch.X4] = pos.x;
			vertices[SpriteBatch.Y4] = pos.y;

			vertices[SpriteBatch.X2] = prevPos.x + prevNormal.x;
			vertices[SpriteBatch.Y2] = prevPos.y + prevNormal.y;
			vertices[SpriteBatch.X3] = pos.x + normal.x;
			vertices[SpriteBatch.Y3] = pos.y + normal.y;

			Color c = DolleApp.inputProcessor.status.color;

			line.setColor(
					Math.max(1 - t, c.r),
					Math.max(1 - t, c.g),
					Math.max(1 - t, c.b),
					t
			);

			batch.draw(line.getTexture(), vertices, 0, vertices.length);

			prevPos.set(pos);
			prevNormal.set(normal);
		}

		batch.setColor(Color.WHITE);
	}

	public void resize(int width, int height) {
		cam.setToOrtho(false, width, height);
	}

}
