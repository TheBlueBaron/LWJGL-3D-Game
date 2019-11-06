package com.rhj.lwjgl.skybox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.rhj.lwjgl.entites.Camera;
import com.rhj.lwjgl.renderengine.DisplayManager;
import com.rhj.lwjgl.shaders.ShaderProgram;
import com.rhj.lwjgl.toolbox.Maths;

public class SkyboxShader extends ShaderProgram{
	
    private static final String VERTEX_FILE = "src/com/rhj/lwjgl/skybox/skyboxVertexShader.vert";
    private static final String FRAGMENT_FILE = "src/com/rhj/lwjgl/skybox/skyboxFragmentShader.frag";
    
    private static final float ROTATE_SPEED = 0.15f;
     
    private int locationProjectionMatrix;
    private int locationViewMatrix;
    private int locationFogColor;
    private int locationCubeMap;
    private int locationCubeMap2;
    private int locationBlendFactor;
    
    private float rotation = 0.0f;
     
    public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
     
    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(locationProjectionMatrix, matrix);
    }
 
    public void loadViewMatrix(Camera camera){
        Matrix4f matrix = Maths.createViewMatrix(camera);
        matrix.m30 = 0.0f;
        matrix.m31 = 0.0f;
        matrix.m32 = 0.0f;
        rotation += ROTATE_SPEED * DisplayManager.getFrameTimeSeconds();
        Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0.0f, 1.0f, 0.0f), matrix, matrix);
        super.loadMatrix(locationViewMatrix, matrix);
    }
    
    public void loadFogColor(float r, float g, float b) {
    	super.loadVector(locationFogColor, new Vector3f(r, g, b));
    }
    
    public void connectTextureUnits() {
    	super.loadInt(locationCubeMap, 0);
    	super.loadInt(locationCubeMap2, 1);
    }
    
    public void loadBlendFactor(float blendFactor) {
    	super.loadFloat(locationBlendFactor, blendFactor);
    }
     
    @Override
    protected void getAllUniformLocations() {
        locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
        locationViewMatrix = super.getUniformLocation("viewMatrix");
        locationFogColor = super.getUniformLocation("fogColor");
        locationCubeMap = super.getUniformLocation("cubeMap");
        locationCubeMap2 = super.getUniformLocation("cubeMap2");
        locationBlendFactor = super.getUniformLocation("blendFactor");
    }
 
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

}
