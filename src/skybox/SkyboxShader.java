package skybox;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import shaders.ShaderProgram;
import toolbox.Maths;

/**
 * Created by colt on 3/24/16.
 */

public class SkyboxShader extends ShaderProgram {

    private static final String VERTEX_FILE = "src/skybox/skyboxVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/skybox/skyboxFragmentShader.txt";
    public static final float ROTATE_SPEED = 1f; //Skybox rotate speed.
    private int locationProjectionMatrix;
    private int locationViewMatrix;
    private int locationFogColor;
    private int locationCubeMap;
    private int locationCubeMap2;
    private int locationBlendFactor;
    private float rotation = 0;

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
        rotation += ROTATE_SPEED * DisplayManager.getFrameTimeSeconds();
        Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 1, 0), matrix, matrix);
        super.loadMatrix(locationViewMatrix, matrix);
    }

    public void loadFogColor(float r, float g, float b) {
        super.loadVector(locationFogColor, new Vector3f(r, g, b));
    }

    public void connectTextureUnits() {
        super.loadInt(locationCubeMap, 0);
        super.loadInt(locationCubeMap2, 1);
    }

    public void loadBlendFactor(float blend) {
        super.loadFloat(locationBlendFactor, blend);
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