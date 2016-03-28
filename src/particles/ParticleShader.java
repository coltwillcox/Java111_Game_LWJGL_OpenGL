package particles;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import renderEngine.MasterShader;

public class ParticleShader extends MasterShader {

    private static final String VERTEX_FILE = "src/particles/particleVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/particles/particleFragmentShader.txt";
    private int locationModelViewMatrix;
    private int locationProjectionMatrix;
    private int locationTexOffset1;
    private int locationTexOffset2;
    private int locationTexCoordInfo;

	//Constructor.
    public ParticleShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        locationModelViewMatrix = super.getUniformLocation("modelViewMatrix");
        locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
        locationTexOffset1 = super.getUniformLocation("texOffset1");
        locationTexOffset2 = super.getUniformLocation("texOffset2");
        locationTexCoordInfo = super.getUniformLocation("texCoordInfo");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

    protected void loadModelViewMatrix(Matrix4f modelView) {
        super.loadMatrix(locationModelViewMatrix, modelView);
    }

    protected void loadProjectionMatrix(Matrix4f projectionMatrix) {
        super.loadMatrix(locationProjectionMatrix, projectionMatrix);
    }

    protected void loadTextureCoordInfo(Vector2f offset1, Vector2f offset2, float numberOfRows, float blend) {
        super.load2DVector(locationTexOffset1, offset1);
        super.load2DVector(locationTexOffset2, offset2);
        super.load2DVector(locationTexCoordInfo, new Vector2f(numberOfRows, blend));
    }

}