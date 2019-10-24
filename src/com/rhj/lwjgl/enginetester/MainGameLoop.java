package com.rhj.lwjgl.enginetester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.rhj.lwjgl.entites.Camera;
import com.rhj.lwjgl.entites.Entity;
import com.rhj.lwjgl.entites.Light;
import com.rhj.lwjgl.models.RawModel;
import com.rhj.lwjgl.models.TexturedModel;
import com.rhj.lwjgl.renderengine.DisplayManager;
import com.rhj.lwjgl.renderengine.Loader;
import com.rhj.lwjgl.renderengine.OBJLoader;
import com.rhj.lwjgl.renderengine.Renderer;
import com.rhj.lwjgl.shaders.StaticShader;
import com.rhj.lwjgl.textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);
		
		RawModel model = OBJLoader.loadObjModel("dragon", loader);
		//ModelTexture texture = new ModelTexture(loader.loadTexture("textures/purple"));
		TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("textures/purple")));
		ModelTexture texture = staticModel.getTexture();
		texture.setShineDamper(35.0f);
		texture.setReflectivity(0.75f);
		
		Entity entity = new Entity(staticModel, new Vector3f(0.0f, 0.0f, -25.0f), 0, 0, 0, 1);
		Light light = new Light(new Vector3f(0.0f, 0.0f, -20.0f), new Vector3f(1.0f, 1.0f, 1.0f));
		
		Camera camera = new Camera();
		
		while(!Display.isCloseRequested()) {
			entity.increaseRotation(0.0f, 1.0f, 0.0f);
			camera.move();
			renderer.prepare();
			shader.start();
			shader.loadLight(light);
			shader.loadViewMatrix(camera);
			renderer.render(entity, shader);
			shader.stop();
			DisplayManager.updateDisplay();
		}
		
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
