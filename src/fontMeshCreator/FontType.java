package fontMeshCreator;

import java.io.File;

/**
 * Represents a font. It holds the font's texture atlas as well as having the
 * ability to create the quad vertices for any text using this font.
 */
public class FontType {

	private int textureAtlas;
	private TextMeshCreator loader;

	/**
	 * Creates a new font and loads up the data about each character from the font file.
	 * @param textureAtlas - The ID of the font atlas texture.
	 * @param fontFile - The font file containing information about each character in the texture atlas.
	 */
    //Constructor.
	public FontType(int textureAtlas, String fontFile) {
		this.textureAtlas = textureAtlas;
		this.loader = new TextMeshCreator(fontFile);
	}

	/**
	 * Takes in an unloaded text and calculate all of the vertices for the quads
	 * on which this text will be rendered. The vertex positions and texture
	 * coords and calculated based on the information from the font file.
	 * @param text - the unloaded text.
	 */
	public TextMeshData loadText(GUIText text) {
		return loader.createTextMesh(text);
	}

    //Getter.
    public int getTextureAtlas() {
        return textureAtlas;
    }

}