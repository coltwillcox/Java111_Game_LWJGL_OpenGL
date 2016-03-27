package fontRendering;


import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.MasterShader;

public class FontShader extends MasterShader {

	private static final String VERTEX_FILE = "src/fontRendering/fontVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/fontRendering/fontFragmentShader.txt";
    private int locationColor;
    private int locationTranslation;

	//Constructor.
	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
        locationColor = super.getUniformLocation("color");
        locationTranslation = super.getUniformLocation("translation");
    }

	@Override
	protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }

    protected void loadColor(Vector3f color) {
        super.loadVector(locationColor, color);
    }

    protected void loadTranslation(Vector2f translation) {
        super.load2DVector(locationTranslation, translation);
    }

}