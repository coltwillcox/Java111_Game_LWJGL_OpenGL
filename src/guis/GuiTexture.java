package guis;

import org.lwjgl.util.vector.Vector2f;

/**
 * Created by colt on 3/23/16.
 */

public class GuiTexture {

    private int texture; //Texture ID.
    private Vector2f position;
    private Vector2f scale;

    //Constructor.
    public GuiTexture(int texture, Vector2f position, Vector2f scale) {
        this.texture = texture;
        this.position = position;
        this.scale = scale;
    }

    //Getters.
    public int getTexture() {
        return texture;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getScale() {
        return scale;
    }

}