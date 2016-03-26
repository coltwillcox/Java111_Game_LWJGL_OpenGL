package water;

import entities.Light;
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
    private int locationMapDUDV;
    private int locationMoveFactor;
    private int locationCameraPosition;
	private int locationMapNormal;
    private int locationLightColor;
    private int locationLightPosition;
    private int locationMapDepth;


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
        locationMapDUDV = getUniformLocation("mapDUDV");
        locationMoveFactor = getUniformLocation("moveFactor");
        locationCameraPosition = getUniformLocation("cameraPosition");
        locationMapNormal = getUniformLocation("mapNormal");
        locationLightColor = getUniformLocation("lightColor");
        locationLightPosition = getUniformLocation("lightPosition");
        locationMapDepth = getUniformLocation("mapDepth");
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

    public void loadMoveFactor(float moveFactor) {
        super.loadFloat(locationMoveFactor, moveFactor);
    }

    public void loadLight(Light sun) {
        super.loadVector(locationLightColor, sun.getColor());
        super.loadVector(locationLightPosition, sun.getPosition());
    }

    public void connectTextureUnits() {
        super.loadInt(locationReflectionTexture, 0);
        super.loadInt(locationRefractionTexture, 1);
        super.loadInt(locationMapDUDV, 2);
        super.loadInt(locationMapNormal, 3);
        super.loadInt(locationMapDepth, 4);
    }

}