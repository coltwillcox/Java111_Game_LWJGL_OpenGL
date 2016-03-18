package models;

import textures.ModelTexture;

/**
 * Created by colt on 3/18/16.
 */

public class TexturedModel {

    private RawModel rawModel;
    private ModelTexture texture;

    //Constructor.
    public TexturedModel(RawModel rawModel, ModelTexture texture) {
        this.rawModel = rawModel;
        this.texture = texture;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public ModelTexture getTexture() {
        return texture;
    }

}