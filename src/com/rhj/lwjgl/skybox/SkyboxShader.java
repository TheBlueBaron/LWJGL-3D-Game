package com.rhj.lwjgl.skybox;

import org.lwjgl.util.vector.Matrix4f;

import com.rhj.lwjgl.entites.Camera;
import com.rhj.lwjgl.shaders.ShaderProgram;
import com.rhj.lwjgl.toolbox.Maths;

public class SkyboxShader extends ShaderProgram{
	
    private static final String VERTEX_FILE = "src/com/rhj/lwjgl/skybox/skyboxVertexShader.vert";
    private static final String FRAGMENT_FILE = "src/com/rhj/lwjgl/skybox/skyboxFragmentShader.frag";
     
    private int location_projectionMatrix;
    private int location_viewMatrix;
     
    public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
     
    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }
 
    public void loadViewMatrix(Camera camera){
        Matrix4f matrix = Maths.createViewMatrix(camera);
        matrix.m30 = 0.0f;
        matrix.m31 = 0.0f;
        matrix.m32 = 0.0f;
        super.loadMatrix(location_viewMatrix, matrix);
    }
     
    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
    }
 
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

}
