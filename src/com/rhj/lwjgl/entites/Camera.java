package com.rhj.lwjgl.entites;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private Vector3f position = new Vector3f(0.0f, 5.0f, 0.0f);
	private float pitch;
	private float yaw;
	private float roll;
	
	
	public void move() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			position.z -= 0.3f;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			position.z += 2.0f;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			position.x += 2.0f;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			position.x -= 2.0f;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
			position.y -= 0.2f;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			position.y += 0.05f;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_E)) {
			yaw += 0.2f;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			yaw -= 0.2f;
		}
	}
	
	public Vector3f getPosition() {
		return position;
	}
	public float getPitch() {
		return pitch;
	}
	public float getYaw() {
		return yaw;
	}
	public float getRoll() {
		return roll;
	}	

}
