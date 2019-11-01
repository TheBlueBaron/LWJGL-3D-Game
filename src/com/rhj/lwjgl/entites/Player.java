package com.rhj.lwjgl.entites;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import com.rhj.lwjgl.models.TexturedModel;
import com.rhj.lwjgl.renderengine.DisplayManager;
import com.rhj.lwjgl.terrains.Terrain;

public class Player extends Entity {
	
	private static final float RUN_SPEED = 20.0f;
	private static final float TURN_SPEED = 160.0f;
	private static final float GRAVITY =  -50.0f;
	private static final float JUMP_POWER = 30.0f;
	
	private static final float TERRAIN_HEIGHT = 0.0f;
	
	private float currentSpeed = 0.0f;
	private float currentTurnSpeed = 0.0f;
	private float upwardsSpeed = 0.0f;
	
	private boolean isInAir = false;

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	public void move(Terrain terrain) {
		checkInputs();
		super.increaseRotation(0.0f, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0.0f);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0.0f, dz);
		upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0.0f, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0.0f);
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		if(super.getPosition().y < terrainHeight) {
			upwardsSpeed = 0.0f;
			isInAir = false;
			super.getPosition().y = terrainHeight;
		}
	}
	
	private void jump() {
		if(!isInAir) {
			upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}		
	}
	
	private void checkInputs() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			currentSpeed = RUN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			currentSpeed = -RUN_SPEED;
		} else {
			currentSpeed = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			currentTurnSpeed = TURN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			currentTurnSpeed = -TURN_SPEED;
		} else {
			currentTurnSpeed = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}
	}

}
