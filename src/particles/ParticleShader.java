package particles;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import renderEngine.MasterShader;

public class ParticleShader extends MasterShader {

    private static final String VERTEX_FILE = "src/particles/particleVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/particles/particleFragmentShader.txt";
    private int locationProjectionMatrix;
    private int locationNumberOfRows;

	//Constructor.
    public ParticleShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        locationNumberOfRows = super.getUniformLocation("numberOfRows");
        locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "modelViewMatrix");
        super.bindAttribute(5, "texOffsets");
        super.bindAttribute(6, "blendFactor");

    }

    protected void loadProjectionMatrix(Matrix4f projectionMatrix) {
        super.loadMatrix(locationProjectionMatrix, projectionMatrix);
    }

    protected void loadNumberOfRows(float numberOfRows) {
        super.loadFloat(locationNumberOfRows, numberOfRows);
    }

}