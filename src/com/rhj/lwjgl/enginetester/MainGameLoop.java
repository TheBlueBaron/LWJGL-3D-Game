package com.rhj.lwjgl.enginetester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.rhj.lwjgl.entites.Camera;
import com.rhj.lwjgl.entites.Entity;
import com.rhj.lwjgl.models.RawModel;
import com.rhj.lwjgl.models.TexturedModel;
import com.rhj.lwjgl.renderengine.DisplayManager;
import com.rhj.lwjgl.renderengine.Loader;
import com.rhj.lwjgl.renderengine.Renderer;
import com.rhj.lwjgl.shaders.StaticShader;
import com.rhj.lwjgl.textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);
		
		float[] vertices = {
			-0.5f,  0.5f, -0.5f,	
			-0.5f, -0.5f, -0.5f,	
			 0.5f, -0.5f, -0.5f,	
			 0.5f,  0.5f, -0.5f,		
			
			-0.5f,  0.5f,  0.5f,	
			-0.5f, -0.5f,  0.5f,	
			 0.5f, -0.5f,  0.5f,	
			 0.5f,  0.5f,  0.5f,
			
			 0.5f,  0.5f, -0.5f,	
			 0.5f, -0.5f, -0.5f,	
			 0.5f, -0.5f,  0.5f,	
			 0.5f,  0.5f,  0.5f,
			
			-0.5f,  0.5f, -0.5f,	
			-0.5f, -0.5f, -0.5f,	
			-0.5f, -0.5f,  0.5f,	
			-0.5f,  0.5f,  0.5f,
			
			-0.5f,  0.5f,  0.5f,
			-0.5f,  0.5f, -0.5f,
			 0.5f,  0.5f, -0.5f,
			 0.5f,  0.5f,  0.5f,
			
			-0.5f, -0.5f,  0.5f,
			-0.5f, -0.5f, -0.5f,
			 0.5f, -0.5f, -0.5f,
			 0.5f, -0.5f,  0.5f
		};
		
		int[] indices = {
				0,  1,  3,	
				3,  1,  2,	
				4,  5,  7,
				7,  5,  6,
				8,  9,  11,
				11, 9,  10,
				12, 13, 15,
				15, 13, 14,	
				16, 17, 19,
				19, 17, 18,
				20, 21, 23,
				23, 21, 22
		};
		
		float[] textureCoords = {
				0, 0,
				0, 1,
				1, 1,
				1, 0,			
				0, 0,
				0, 1,
				1, 1,
				1, 0,			
				0, 0,
				0, 1,
				1, 1,
				1, 0,
				0, 0,
				0, 1,
				1, 1,
				1, 0,
				0, 0,
				0, 1,
				1, 1,
				1, 0,
				0, 0,
				0, 1,
				1, 1,
				1, 0
		};
		
		RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
		ModelTexture texture = new ModelTexture(loader.loadTexture("textures/shapes"));
		TexturedModel staticModel = new TexturedModel(model, texture);
		
		Entity entity = new Entity(staticModel, new Vector3f(0.0f, 0.0f, -5.0f), 0, 0, 0, 1);
		
		Camera camera = new Camera();
		
		while(!Display.isCloseRequested()) {
			entity.increaseRotation(1.0f, 1.0f, 0.0f);
			camera.move();
			renderer.prepare();
			shader.start();
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
