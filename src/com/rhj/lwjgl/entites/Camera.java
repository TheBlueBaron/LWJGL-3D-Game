package com.rhj.lwjgl.entites;

import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private Vector3f position = new Vector3f(100.0f, 20.0f, 0.0f);
	private float pitch = 10.0f;
	private float yaw;
	private float roll;
	
	
	public void move() {
		
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
