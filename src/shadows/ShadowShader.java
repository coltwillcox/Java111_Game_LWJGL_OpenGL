package shadows;

import org.lwjgl.util.vector.Matrix4f;
import renderEngine.MasterShader;

public class ShadowShader extends MasterShader {
	
	private static final String VERTEX_FILE = "/shadows/shadowVertexShader.txt";
	private static final String FRAGMENT_FILE = "/shadows/shadowFragmentShader.txt";
	private int locationMvpMatrix;

	//Constructor.
    protected ShadowShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		locationMvpMatrix = super.getUniformLocation("mvpMatrix");
	}
	
	protected void loadMvpMatrix(Matrix4f mvpMatrix){
		super.loadMatrix(locationMvpMatrix, mvpMatrix);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "inPosition");
        super.bindAttribute(1, "inTextureCoords");
	}

}