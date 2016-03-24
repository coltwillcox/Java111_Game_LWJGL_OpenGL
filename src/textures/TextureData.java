package textures;

import java.nio.ByteBuffer;

/**
 * Created by colt on 3/24/16.
 */

public class TextureData {

    private int width;
    private int height;
    private ByteBuffer buffer;

    //Constructor.
    public TextureData(ByteBuffer buffer, int width, int height) {
        this.buffer = buffer;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

}