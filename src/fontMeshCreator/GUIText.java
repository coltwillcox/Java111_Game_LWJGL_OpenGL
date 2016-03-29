package fontMeshCreator;

import fontRendering.TextMaster;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Represents a piece of text in the game.
 */

public class GUIText {

    private int textMeshVao;
    private int vertexCount;
    private int numberOfLines;
    private float fontSize;
    private float lineMaxSize;
    private boolean centerText = false;
    private String textString;
    private FontType font;
    private Vector2f position;
    private Vector3f colour = new Vector3f(0f, 0f, 0f);

	/**
	 * Creates a new text, loads the text's quads into a VAO, and adds the text
	 * to the screen.
	 * @param text - The text.
	 * @param fontSize - The font size of the text, where a font size of 1 is the default size.
	 * @param font - The font that this text should use.
	 * @param position - The position on the screen where the top left corner of the text should be rendered.
     *                 The top left corner of the screen is (0, 0) and the bottom right is (1, 1).
	 * @param maxLineLength - Basically the width of the virtual page in terms of screen width (1 is full screen width,
     *                      0.5 is half the width of the screen, etc.) Text cannot go off the edge of the page,
     *                      so if the text is longer than this length it will go onto the next line.
     *                      When text is centered it is centered into the middle of the line, based on this line length value.
	 * @param centered - Whether the text should be centered or not.
	 */
    //Constructor.
	public GUIText(String text, float fontSize, FontType font, Vector2f position, float maxLineLength, boolean centered) {
		this.textString = text;
		this.fontSize = fontSize;
		this.font = font;
		this.position = position;
		this.lineMaxSize = maxLineLength;
		this.centerText = centered;
        TextMaster.loadText(this);
	}

	public void remove() {
        TextMaster.removeText(this);
	}

    //Getters and setters.
    public FontType getFont() {
        return font;
    }

    /**
     * Set the colour of the text.
     * @param r - Red value, between 0 and 1.
     * @param g - Green value, between 0 and 1.
     * @param b - Blue value, between 0 and 1.
     */

	public void setColor(float r, float g, float b) {
		colour.set(r, g, b);
	}

	public Vector3f getColor() {
		return colour;
	}

	public int getNumberOfLines() {
		return numberOfLines;
	}

	public Vector2f getPosition() {
		return position;
	}

	public int getMesh() {
		return textMeshVao;
	}

	/**
	 * Set the VAO and vertex count for this text.
	 * @param vao - The VAO containing all the vertex data for the quads on which the text will be rendered.
	 * @param verticesCount - The total number of vertices in all of the quads.
	 */

	public void setMeshInfo(int vao, int verticesCount) {
		this.textMeshVao = vao;
		this.vertexCount = verticesCount;
	}

	public int getVertexCount() {
		return this.vertexCount;
	}

	protected float getFontSize() {
		return fontSize;
	}

	protected void setNumberOfLines(int number) {
		this.numberOfLines = number;
	}

	protected boolean isCentered() {
		return centerText;
	}

	protected float getMaxLineSize() {
		return lineMaxSize;
	}

	protected String getTextString() {
		return textString;
	}

    public void setTextString(String textString) {
        this.textString = textString;
    }

}