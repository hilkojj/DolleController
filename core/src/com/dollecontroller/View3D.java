package com.dollecontroller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.math.Vector3;

public class View3D {

	private PerspectiveCamera cam;
	private ModelInstance controllerModel;
	private ModelBatch modelBatch;
	private Environment environment;

	public View3D() {

		cam = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		ModelLoader loader = new ObjLoader();
		Model model = loader.loadModel(Gdx.files.internal("controller.obj"));
		controllerModel = new ModelInstance(model);

		modelBatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		cam.position.set(10, 10, 10);
		cam.lookAt(new Vector3());
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

	}

	public void render() {

		cam.rotateAround(Vector3.Zero, Vector3.Y, 1);
		cam.update();

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
