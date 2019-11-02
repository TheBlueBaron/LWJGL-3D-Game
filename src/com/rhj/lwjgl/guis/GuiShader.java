package com.rhj.lwjgl.guis;

import org.lwjgl.util.vector.Matrix4f;

import com.rhj.lwjgl.shaders.ShaderProgram;

public class GuiShader extends ShaderProgram {
	
    private static final String VERTEX_FILE = "src/com/rhj/lwjgl/guis/guiVertexShader.vert";
    private static final String FRAGMENT_FILE = "src/com/rhj/lwjgl/guis/guiFragmentShader.frag";
     
    private int locationTransformationMatrix;
 
    public GuiShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
     
    public void loadTransformation(Matrix4f matrix){
        super.loadMatrix(locationTransformationMatrix, matrix);
    }
 
    @Override
    protected void getAllUniformLocations() {
        locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
    }
 
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

}
