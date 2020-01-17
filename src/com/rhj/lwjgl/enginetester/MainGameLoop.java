package com.rhj.lwjgl.enginetester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.rhj.lwjgl.entites.Camera;
import com.rhj.lwjgl.entites.Entity;
import com.rhj.lwjgl.entites.Light;
import com.rhj.lwjgl.entites.Player;
import com.rhj.lwjgl.guis.GuiRenderer;
import com.rhj.lwjgl.guis.GuiTexture;
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
import com.rhj.lwjgl.toolbox.MousePicker;
import com.rhj.lwjgl.water.WaterFrameBuffers;
import com.rhj.lwjgl.water.WaterRenderer;
import com.rhj.lwjgl.water.WaterShader;
import com.rhj.lwjgl.water.WaterTile;

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
		// ENVIRONMENT MODELS
		
		ModelData treeData = OBJFileLoader.loadOBJ("tree");
		ModelData tree2Data = OBJFileLoader.loadOBJ("lowPolyTree");
		ModelData grassData = OBJFileLoader.loadOBJ("grassModel");
		ModelData fernData = OBJFileLoader.loadOBJ("fern");
		ModelData lampData = OBJFileLoader.loadOBJ("lamp");
		
		RawModel treeModel = loader.loadToVAO(treeData.getVertices(), treeData.getTextureCoords(), treeData.getNormals(), treeData.getIndices());
		TexturedModel staticModel = new TexturedModel(treeModel, new ModelTexture(loader.loadTexture("tree")));
		
		RawModel tree2Model = loader.loadToVAO(tree2Data.getVertices(), tree2Data.getTextureCoords(), tree2Data.getNormals(), tree2Data.getIndices());
		TexturedModel tree2 = new TexturedModel(tree2Model, new ModelTexture(loader.loadTexture("lowPolyTree")));
		tree2.getTexture().setNumberOfRows(2);
		
		RawModel grassModel = loader.loadToVAO(grassData.getVertices(), grassData.getTextureCoords(), grassData.getNormals(), grassData.getIndices());
		TexturedModel grass = new TexturedModel(grassModel, new ModelTexture(loader.loadTexture("grassTexture")));
		grass.getTexture().setHasTransparancy(true);
		grass.getTexture().setUseFakeLighting(true);
		
		RawModel fernModel = loader.loadToVAO(fernData.getVertices(), fernData.getTextureCoords(), fernData.getNormals(), fernData.getIndices());
		TexturedModel fern = new TexturedModel(fernModel, new ModelTexture(loader.loadTexture("fern")));		
		fern.getTexture().setHasTransparancy(true);
		fern.getTexture().setNumberOfRows(2);
		
		RawModel lampModel = loader.loadToVAO(lampData.getVertices(), lampData.getTextureCoords(), lampData.getNormals(), lampData.getIndices());
		TexturedModel lamp = new TexturedModel(lampModel, new ModelTexture(loader.loadTexture("lamp")));
		lamp.getTexture().setUseFakeLighting(true);

		ModelTexture texture = staticModel.getTexture();
		texture.setShineDamper(50.0f);
		texture.setReflectivity(0.0f);
		
		// --------------------------------------------------------------------------------------------------
		// PLAYER MODEL
		
		ModelData playerModel = OBJFileLoader.loadOBJ("person");
		
		RawModel playerRaw = loader.loadToVAO(playerModel.getVertices(), playerModel.getTextureCoords(), playerModel.getNormals(), playerModel.getIndices());
		TexturedModel playerTex = new TexturedModel(playerRaw, new ModelTexture(loader.loadTexture("playerTexture")));
		
		// --------------------------------------------------------------------------------------------------
		// TERRAIN
		
		List<Terrain> terrains = new ArrayList<Terrain>();
		
		Terrain terrain = new Terrain(-0.5f, -0.5f, loader, texturePack, blendMap, "heightmap");
		terrains.add(terrain);
		
		// --------------------------------------------------------------------------------------------------
		// ENTITY COLLECTION
		
		List<Entity> entities = new ArrayList<Entity>();
		Random rand = new Random();
		
		// --------------------------------------------------------------------------------------------------		
		// GENERATE TREES
		
		for(int i = 0; i < 150; i++) {
			float x = rand.nextFloat() * 800 - 400;
			float z = rand.nextFloat() * 800 - 400;
			float y = terrain.getHeightOfTerrain(x, z);
			Entity e = new Entity(staticModel, new Vector3f(x, y, z), 0, 0, 0, 4.0f);
			entities.add(e);
			x = rand.nextFloat() * 800 - 400;
			z = rand.nextFloat() * 800 - 400;
			y = terrain.getHeightOfTerrain(x, z);
			e = new Entity(tree2, new Vector3f(x, y, z), 0, 0, 0, 0.5f);
			entities.add(e);
		}
		
		// --------------------------------------------------------------------------------------------------
		// GENERATE TERRAIN DETAILS
		
		for(int i = 0; i < 500; i++) {
			float x = rand.nextFloat() * 800 - 400;
			float z = rand.nextFloat() * 800 - 400;
			float y = terrain.getHeightOfTerrain(x, z);
			Entity e = new Entity(fern, rand.nextInt(4), new Vector3f(x, y, z), 0, 0, 0, 0.6f);
			entities.add(e);
		}
	
		// --------------------------------------------------------------------------------------------------
		// LIGHTING
		
		List<Light> lights = new ArrayList<Light>();
		Light sun = new Light(new Vector3f(0.0f, 5000.0f, -7000.0f), new Vector3f(0.9f, 0.9f, 0.9f)); 
		lights.add(sun);
		lights.add(new Light(new Vector3f(0.0f, terrain.getHeightOfTerrain(0.0f, -100.0f) + 12.5f, -100.0f), new Vector3f(2.0f, 0.0f, 0.0f), new Vector3f(1.0f, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(100.0f, terrain.getHeightOfTerrain(100.0f, -110.0f) + 12.5f, -110.0f), new Vector3f(0.0f, 2.0f, 0.0f), new Vector3f(1.0f, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(0.0f, terrain.getHeightOfTerrain(0.0f, 30.0f) + 12.5f, 30.0f), new Vector3f(0.0f, 1.5f, 2.0f), new Vector3f(1.0f, 0.01f, 0.002f)));
		
//		Light movableLight = new Light(new Vector3f(293.0f, 7.0f, -305.0f), new Vector3f(2.0f, 0.0f, 0.0f), new Vector3f(1.0f, 0.01f, 0.002f));
//		lights.add(movableLight);
		
		entities.add(new Entity(lamp, new Vector3f(0.0f, terrain.getHeightOfTerrain(0.0f, -100.0f), -100.0f), 0, 0, 0, 1));
		entities.add(new Entity(lamp, new Vector3f(100.0f, terrain.getHeightOfTerrain(100.0f, -110.0f), -110.0f), 0, 0, 0, 1));
		entities.add(new Entity(lamp, new Vector3f(0.0f, terrain.getHeightOfTerrain(0.0f, 30.0f), 30.0f), 0, 0, 0, 1));
		
//		Entity movableLamp = new Entity(lamp, new Vector3f(293.0f, -6.8f, -305.0f), 0, 0, 0, 1);
//		entities.add(movableLamp);
		
		// --------------------------------------------------------------------------------------------------
		// MASTER RENDERER
		
		MasterRenderer renderer = new MasterRenderer(loader);
		
		// --------------------------------------------------------------------------------------------------
		// PLAYER AND CAMERA
		
		Player player = new Player(playerTex, new Vector3f(0.0f, 0.0f, -80.0f), 0, 180.0f, 0, 0.6f);
		entities.add(player);
		Camera camera = new Camera(player);
		
		// --------------------------------------------------------------------------------------------------
		// GUI
		
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
//		GuiTexture gui = new GuiTexture(loader.loadTexture("healthBar"), new Vector2f(-0.85f, 0.95f), new Vector2f(0.15f, 0.15f));
//		guis.add(gui);
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		// --------------------------------------------------------------------------------------------------
		// MOUSE PICKER
		
		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);
		
		// --------------------------------------------------------------------------------------------------
		// WATER
		
		WaterFrameBuffers fbos = new WaterFrameBuffers();
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), fbos);
		List<WaterTile> waters = new ArrayList<WaterTile>();
		WaterTile water = new WaterTile(-180.0f, -50.0f, -6.5f);
		waters.add(water);	
		
		// --------------------------------------------------------------------------------------------------
		// GAME LOOP
		
		while(!Display.isCloseRequested()) {
			player.move(terrain);
			camera.move();			
			picker.update();
			
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			
//			Vector3f terrainPoint = picker.getCurrentTerrainPoint();
//			if(terrainPoint != null) {
//				movableLamp.setPosition(terrainPoint);
//				movableLight.setPosition(new Vector3f(terrainPoint.x, terrainPoint.y + 12.5f, terrainPoint.z));
//			}
						
			// Render reflection texture
			fbos.bindReflectionFrameBuffer();
			float distance = 2 * (camera.getPosition().y - water.getHeight());
			camera.getPosition().y -= distance;
			camera.invertPitch();
			renderer.renderScene(entities, terrains, lights, camera, new Vector4f(0.0f, 1.0f, 0.0f, -water.getHeight() + 0.5f));
			camera.getPosition().y += distance;
			camera.invertPitch();
			
			// Render refraction texture
			fbos.bindRefractionFrameBuffer();
			renderer.renderScene(entities, terrains, lights, camera, new Vector4f(0.0f, -1.0f, 0.0f, water.getHeight() + 0.5f));
			
			// Render to screen
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0); // Some drivers only require this otherwise a large clip height is set for the scene
			fbos.unbindCurrentFrameBuffer();
			renderer.renderScene(entities, terrains, lights, camera, new Vector4f(0.0f, -1.0f, 0.0f, 15000.0f));
			waterRenderer.render(waters, camera, sun);

			guiRenderer.render(guis);
			DisplayManager.updateDisplay();
		}
		
		fbos.cleanUp();
		waterShader.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
