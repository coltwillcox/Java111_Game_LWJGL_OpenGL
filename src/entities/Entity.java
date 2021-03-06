package entities;

import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by colt on 3/18/16.
 */

public class Entity {

    private TexturedModel model;
    private Vector3f position;
    private float rotX;
    private float rotY;
    private float rotZ;
    private float scale;
    private int textureIndex = 0; //Texture number from atlas (eg. first row = 0, 1, 2, second row = 3, 4, 5, and third row = 6, 7, 8). If one texture is in image, it is/will be 0.

    //Constructor.
    public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    //Constructor 2, with texture index.
    public Entity(TexturedModel model, int textureIndex, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        this.model = model;
        this.textureIndex = textureIndex;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    public void increasePosition(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public void increaseRotation(float dx, float dy, float dz) {
        this.rotX += dx;
        this.rotY += dy;
        this.rotZ += dz;
    }

    //Getters and setters.
    public float getTextureXOffset(){
        int column = textureIndex % model.getTexture().getNumberOfRows();
        return (float) column / (float) model.getTexture().getNumberOfRows();
    }

    public float getTextureYOffset(){
        int row = textureIndex / model.getTexture().getNumberOfRows(); //No need to float because they're both integers.
        return (float) row / (float) model.getTexture().getNumberOfRows();
    }

    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRotX() {
        return rotX;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

}