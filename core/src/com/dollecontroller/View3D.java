package com.dollecontroller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;

import static com.dollecontroller.DolleApp.DEFAULT_HEIGHT;
import static com.dollecontroller.DolleApp.DEFAULT_WIDTH;

public class View3D {

	private PerspectiveCamera cam;
	private ModelInstance controllerModel;
	private ModelBatch modelBatch;
	private Environment environment;
	private DirectionalLight light;
	private float prevX, prevY, factor;
	private SpriteBatch spriteBatch;
	private ShaderProgram blurShader, lightShader;
	private FrameBuffer frameBuffer, lightRaysBuffer;
	private Texture lightTexture;
	private float[] lightRotations = {0, 10, 20}, lightRotationSpeed = {0.1f, .2f, -.1f};
	private Color lightColor = new Color();

	public View3D() {

		cam = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0, 0, 10);
		cam.lookAt(new Vector3());
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		ModelLoader loader = new ObjLoader();
		Model model = loader.loadModel(Gdx.files.internal("controller.obj"));
		controllerModel = new ModelInstance(model);

		modelBatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f));
		light = new DirectionalLight().set(1, 1, 1, -1f, -0.8f, -0.2f);
		environment.add(light);

		spriteBatch = new SpriteBatch();

		blurShader = new ShaderProgram(
				SpriteBatch.createDefaultShader().getVertexShaderSource(),
				Gdx.files.internal("blur.glsl").readString()
		);
		System.out.println(blurShader.getLog());
		lightShader = new ShaderProgram(
				SpriteBatch.createDefaultShader().getVertexShaderSource(),
				Gdx.files.internal("light.glsl").readString()
		);
		System.out.println(lightShader.getLog());
		ShaderProgram.pedantic = false;

		frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, 128, 128, false);
		lightRaysBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, 512, 512, false);
		lightTexture = new Texture(Gdx.files.internal("light.png"));
		lightTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

	}

	public void render() {

		float x = Gdx.input.getX(), y = Gdx.input.getY();

		factor = factor * 3 + (Gdx.input.isButtonPressed(0) ? .4f : -.02f);
		factor /= 4;

		cam.rotateAround(Vector3.Zero, Vector3.Y, (prevX - x) * factor);
		cam.position.y += (prevY - y) * factor * -.3f;
		cam.position.nor().scl(10);
		cam.lookAt(Vector3.Zero);

		prevX = x;
		prevY = y;

		cam.update();
		light.direction.set(cam.direction).rotate(Vector3.X, 60).rotate(Vector3.Z, 70);

		frameBuffer.begin();

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 0);

		renderModel();

		spriteBatch.setShader(blurShader);
		spriteBatch.begin();
		spriteBatch.draw(frameBuffer.getColorBufferTexture(), 0, DEFAULT_HEIGHT, DEFAULT_WIDTH, -DEFAULT_HEIGHT);
		spriteBatch.end();

		frameBuffer.end();

		lightRaysBuffer.begin();

		spriteBatch.setShader(null);
		spriteBatch.begin();

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 0);

		for (int i = 0; i < lightRotations.length; i++) {

			lightRotations[i] += lightRotationSpeed[i];

			spriteBatch.draw(lightTexture, 0, 0, DEFAULT_WIDTH / 2, DEFAULT_HEIGHT / 2, DEFAULT_WIDTH, DEFAULT_HEIGHT, 1, 1,
					lightRotations[i], 0, 0, 512, 512, false, false);
		}


		spriteBatch.end();

		lightRaysBuffer.end();

		spriteBatch.setShader(lightShader);
		Texture blurMap = frameBuffer.getColorBufferTexture();
		blurMap.bind(1);
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
		lightShader.begin();
		lightColor.lerp(DolleApp.inputProcessor.status.color, .2f);
		lightColor.a = .3f;
		lightShader.setUniformf("statusColor", lightColor);
		lightShader.setUniformi("blurMap", 1);
		spriteBatch.begin();

		spriteBatch.draw(lightRaysBuffer.getColorBufferTexture(), 0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);

		spriteBatch.end();
		lightShader.end();

		renderModel();

	}

	private void renderModel() {
		modelBatch.begin(cam);
		modelBatch.render(controllerModel, environment);
		modelBatch.end();
	}

	public void resize(int width, int height) {
		cam.viewportWidth = width;
		cam.viewportHeight = height;
		cam.update();
	}

}
