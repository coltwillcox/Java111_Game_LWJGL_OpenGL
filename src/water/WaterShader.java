package water;

import org.lwjgl.util.vector.Matrix4f;
import shaders.ShaderProgram;
import toolbox.Maths;
import entities.Camera;

public class WaterShader extends ShaderProgram {

	private final static String VERTEX_FILE = "src/water/waterVertexShader.txt";
	private final static String FRAGMENT_FILE = "src/water/waterFragmentShader.txt";
    private int locationProjectionMatrix;
    private int locationModelMatrix;
	private int locationViewMatrix;
	private int locationReflectionTexture;
    private int locationRefractionTexture;
    private int locationDudvMap;
    private int locationMoveFactor;
    private int locationCameraPosition;

	//Constructor.
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
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(locationProjectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		loadMatrix(locationViewMatrix, viewMatrix);
        super.loadVector(locationCameraPosition, camera.getPosition());
	}

	public void loadModelMatrix(Matrix4f modelMatrix) {
		loadMatrix(locationModelMatrix, modelMatrix);
	}

    public void connectTextureUnits() {
        super.loadInt(locationReflectionTexture, 0);
        super.loadInt(locationRefractionTexture, 1);
        super.loadInt(locationDudvMap, 2);
    }

    public void loadMoveFactor(float moveFactor) {
        super.loadFloat(locationMoveFactor, moveFactor);
    }

}