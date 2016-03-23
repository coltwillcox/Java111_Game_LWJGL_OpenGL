package entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * Created by colt on 3/19/16.
 */

public class Light {

    private Vector3f position;
    private Vector3f color;
    private Vector3f attenuation = new Vector3f(1, 0, 0);

    //Constructor.
    public Light(Vector3f position, Vector3f color) {
        this.position = position;
        this.color = color;
    }

    //Constructor 2.
    public Light(Vector3f position, Vector3f color, Vector3f attenuation) {
        this.position = position;
        this.color = color;
        this.attenuation = attenuation;
    }

    //Getters and setters.
    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public Vector3f getAttenuation() {
        return attenuation;
    }

}