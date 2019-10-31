package com.rhj.lwjgl.enginetester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.rhj.lwjgl.entites.Camera;
import com.rhj.lwjgl.entites.Entity;
import com.rhj.lwjgl.entites.Light;
import com.rhj.lwjgl.entites.Player;
import com.rhj.lwjgl.models.RawModel;
import com.rhj.lwjgl.models.TexturedModel;
import com.rhj.lwjgl.objconverter.ModelData;
import com.rhj.lwjgl.objconverter.OBJFileLoader;
import com.rhj.lwjgl.renderengine.DisplayManager;
import com.rhj.lwjgl.renderengine.Loader;
import com.rhj.lwjgl.renderengine.MasterRenderer;
import com.rhj.lwjgl.terrains.Terrain;
import com.rhj.lwjgl.textures.ModelTexture;
import com.rhj.lwjgl.textures.TerrainTexture;
import com.rhj.lwjgl.textures.TerrainTexturePack;

public class MainGameLoop {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		
		// TERRAIN TEXTURE LOADING / BINDING
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		// --------------------------------------------------------------------------------------------------
		// Environment Models
		
		ModelData treeData = OBJFileLoader.loadOBJ("tree");
		ModelData tree2Data = OBJFileLoader.loadOBJ("lowPolyTree");
		ModelData grassData = OBJFileLoader.loadOBJ("grassModel");
		ModelData fernData = OBJFileLoader.loadOBJ("fern");
		
		RawModel treeModel = loader.loadToVAO(treeData.getVertices(), treeData.getTextureCoords(), treeData.getNormals(), treeData.getIndices());
		TexturedModel staticModel = new TexturedModel(treeModel, new ModelTexture(loader.loadTexture("tree")));
		
		RawModel tree2Model = loader.loadToVAO(tree2Data.getVertices(), tree2Data.getTextureCoords(), tree2Data.getNormals(), tree2Data.getIndices());
		TexturedModel tree2 = new TexturedModel(tree2Model, new ModelTexture(loader.loadTexture("lowPolyTree")));
		
		RawModel grassModel = loader.loadToVAO(grassData.getVertices(), grassData.getTextureCoords(), grassData.getNormals(), grassData.getIndices());
		TexturedModel grass = new TexturedModel(grassModel, new ModelTexture(loader.loadTexture("grass")));
		grass.getTexture().setHasTransparancy(true);
		grass.getTexture().setUseFakeLighting(true);
		
		RawModel fernModel = loader.loadToVAO(fernData.getVertices(), fernData.getTextureCoords(), fernData.getNormals(), fernData.getIndices());
		TexturedModel fern = new TexturedModel(fernModel, new ModelTexture(loader.loadTexture("fern")));		
		fern.getTexture().setHasTransparancy(true);

		ModelTexture texture = staticModel.getTexture();
		texture.setShineDamper(50.0f);
		texture.setReflectivity(0.0f);
		
		// --------------------------------------------------------------------------------------------------
		// Player Model
		
		ModelData playerModel = OBJFileLoader.loadOBJ("person");
		
		RawModel playerRaw = loader.loadToVAO(playerModel.getVertices(), playerModel.getTextureCoords(), playerModel.getNormals(), playerModel.getIndices());
		TexturedModel playerTex = new TexturedModel(playerRaw, new ModelTexture(loader.loadTexture("playerTexture")));
		
		Terrain terrain = new Terrain(-0.5f, -0.5f, loader, texturePack, blendMap);
		
		List<Entity> entities = new ArrayList<Entity>();
		Random rand = new Random();
		
		for(int i = 0; i < 150; i++) {
			Entity e = new Entity(staticModel, new Vector3f(rand.nextFloat() * 800 - 400, 0.0f, rand.nextFloat() * 800 - 400), 0, 0, 0, 4.0f);
			entities.add(e);
			e = new Entity(tree2, new Vector3f(rand.nextFloat() * 800 - 400, 0.0f, rand.nextFloat() * 800 - 400), 0, 0, 0, 0.5f);
			entities.add(e);
		}
		
		for(int i = 0; i < 500; i++) {
			Entity e = new Entity(grass, new Vector3f(rand.nextFloat() * 800 - 400, 0, rand.nextFloat() * 800 - 400), 0, 0, 0, 1);
			entities.add(e);
			e = new Entity(fern, new Vector3f(rand.nextFloat() * 800 - 400, 0, rand.nextFloat() * 800 - 400), 0, 0, 0, 0.6f);
			entities.add(e);
		}
	
		Light light = new Light(new Vector3f(20000.0f, 40000.0f, 20000.0f), new Vector3f(1.0f, 1.0f, 1.0f));		
		
		MasterRenderer renderer = new MasterRenderer();
		
		Player player = new Player(playerTex, new Vector3f(100.0f, 0.0f, -50.0f), 0, 180.0f, 0, 0.6f);
		Camera camera = new Camera(player);
		
		while(!Display.isCloseRequested()) {
			camera.move();
			player.move();
			renderer.processEntity(player);
			for(Entity e : entities) {
				renderer.processEntity(e);			
			}
			renderer.processTerrain(terrain);
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
