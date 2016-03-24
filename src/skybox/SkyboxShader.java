package skybox;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import shaders.ShaderProgram;
import toolbox.Maths;

/**
 * Created by colt on 3/24/16.
 */

public class SkyboxShader extends ShaderProgram {

    private static final String VERTEX_FILE = "src/skybox/skyboxVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/skybox/skyboxFragmentShader.txt";
    private int locationProjectionMatrix;
    private int locationViewMatrix;

    //Constructor.
    public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(locationProjectionMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f matrix = Maths.createViewMatrix(camera);
        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = 0;
        super.loadMatrix(locationViewMatrix, matrix);
    }

    @Override
    protected void getAllUniformLocations() {
        locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
        locationViewMatrix = super.getUniformLocation("viewMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

}