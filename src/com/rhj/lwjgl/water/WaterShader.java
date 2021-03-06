package com.rhj.lwjgl.water;

import org.lwjgl.util.vector.Matrix4f;

import com.rhj.lwjgl.entites.Camera;
import com.rhj.lwjgl.entites.Light;
import com.rhj.lwjgl.shaders.ShaderProgram;
import com.rhj.lwjgl.toolbox.Maths;

public class WaterShader extends ShaderProgram {

	private final static String VERTEX_FILE = "src/com/rhj/lwjgl/water/waterVertex.vert";
	private final static String FRAGMENT_FILE = "src/com/rhj/lwjgl/water/waterFragment.frag";

	private int locationModelMatrix;
	private int locationViewMatrix;
	private int locationProjectionMatrix;
	private int locationReflectionTexture;
	private int locationRefractionTexture;
	private int locationDudvMap;
	private int locationMoveFactor;
	private int locationCameraPosition;
	private int locationNormalMap;
	private int locationLightColor;
	private int locationLightPosition;
	private int locationDepthMap;

	public WaterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		locationProjectionMatrix = getUniformLocation("projectionMatrix");
		locationViewMatrix = getUniformLocation("viewMatrix");
		locationModelMatrix = getUniformLocation("modelMatrix");
		locationReflectionTexture = getUniformLocation("reflectionTexture");
		locationRefractionTexture = getUniformLocation("refractionTexture");
		locationDudvMap = getUniformLocation("dudvMap");
		locationMoveFactor = getUniformLocation("moveFactor");
		locationCameraPosition = getUniformLocation("cameraPosition");
		locationNormalMap = getUniformLocation("normalMap");
		locationLightColor = getUniformLocation("lightColor");
		locationLightPosition = getUniformLocation("lightPosition");
		locationDepthMap = getUniformLocation("depthMap");
	}
	
	public void loadMoveFactor(float moveFactor) {
		super.loadFloat(locationMoveFactor, moveFactor);
	}
	
	public void connectTextureUnits() {
		super.loadInt(locationReflectionTexture, 0);
		super.loadInt(locationRefractionTexture, 1);
		super.loadInt(locationDudvMap, 2);
		super.loadInt(locationNormalMap, 3);
		super.loadInt(locationDepthMap, 4);
	}
	
	public void loadLight(Light sun) {
		super.loadVector(locationLightColor, sun.getColor());
		super.loadVector(locationLightPosition, sun.getPosition());
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(locationProjectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		loadMatrix(locationViewMatrix, viewMatrix);
		super.loadVector(locationCameraPosition, camera.getPosition());
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		loadMatrix(locationModelMatrix, modelMatrix);
	}

}
