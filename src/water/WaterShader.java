package water;

import org.lwjgl.util.vector.Matrix4f;
import shaders.ShaderProgram;
import toolbox.Maths;
import entities.Camera;

public class WaterShader extends ShaderProgram {

	private final static String VERTEX_FILE = "src/water/waterVertex.txt";
	private final static String FRAGMENT_FILE = "src/water/waterFragment.txt";
	private int locationModelMatrix;
	private int locationViewMatrix;
	private int locationProjectionMatrix;

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
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(locationProjectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		loadMatrix(locationViewMatrix, viewMatrix);
	}

	public void loadModelMatrix(Matrix4f modelMatrix) {
		loadMatrix(locationModelMatrix, modelMatrix);
	}

}