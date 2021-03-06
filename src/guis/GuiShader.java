package guis;

import org.lwjgl.util.vector.Matrix4f;
import renderEngine.MasterShader;

/**
 * Created by colt on 3/23/16.
 */

public class GuiShader extends MasterShader {

    private static final String VERTEX_FILE = "/guis/guiVertexShader.txt";
    private static final String FRAGMENT_FILE = "/guis/guiFragmentShader.txt";
    private int locationTransformationMatrix;

    //Constructor.
    public GuiShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadTransformation(Matrix4f matrix) {
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