package fontMeshCreator;

/**
 * Simple data structure class holding information about a certain glyph in the font texture atlas.
 * All sizes are for a font-size of 1.
 */

public class Character {

	private int id;
	private double xTextureCoord;
	private double yTextureCoord;
	private double xMaxTextureCoord;
	private double yMaxTextureCoord;
	private double xOffset;
	private double yOffset;
	private double sizeX;
	private double sizeY;
	private double xAdvance;

	/**
	 * @param id - The ASCII value of the character.
	 * @param xTextureCoord - The x texture coordinate for the top left corner of the character in the texture atlas.
	 * @param yTextureCoord - The y texture coordinate for the top left corner of the character in the texture atlas.
	 * @param xTexSize - The width of the character in the texture atlas.
	 * @param yTexSize - The height of the character in the texture atlas.
	 * @param xOffset - The x distance from the curser to the left edge of the character's quad.
	 * @param yOffset - The y distance from the curser to the top edge of the character's quad.
	 * @param sizeX - The width of the character's quad in screen space.
	 * @param sizeY - The height of the character's quad in screen space.
	 * @param xAdvance - How far in pixels the cursor should advance after adding this character.
	 */
    //Constructor.
    protected Character(int id, double xTextureCoord, double yTextureCoord, double xTexSize, double yTexSize, double xOffset, double yOffset, double sizeX, double sizeY, double xAdvance) {
		this.id = id;
		this.xTextureCoord = xTextureCoord;
		this.yTextureCoord = yTextureCoord;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.xMaxTextureCoord = xTexSize + xTextureCoord;
		this.yMaxTextureCoord = yTexSize + yTextureCoord;
		this.xAdvance = xAdvance;
	}

	//Getters.
    protected int getId() {
		return id;
	}

	protected double getxTextureCoord() {
		return xTextureCoord;
	}

	protected double getyTextureCoord() {
		return yTextureCoord;
	}

	protected double getXMaxTextureCoord() {
		return xMaxTextureCoord;
	}

	protected double getYMaxTextureCoord() {
		return yMaxTextureCoord;
	}

	protected double getxOffset() {
		return xOffset;
	}

	protected double getyOffset() {
		return yOffset;
	}

	protected double getSizeX() {
		return sizeX;
	}

	protected double getSizeY() {
		return sizeY;
	}

	protected double getxAdvance() {
		return xAdvance;
	}

}