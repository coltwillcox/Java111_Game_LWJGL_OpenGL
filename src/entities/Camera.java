package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by colt on 3/18/16.
 */

public class Camera {

    private Vector3f position = new Vector3f(0, 0, 0);
    private float pitch;    //How much high or low camera is aimed.
    private float yaw;      //How much left or right.
    private float roll;     //How much camera is tilted.

    //Constructor.
    public Camera() {}

    public void move() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W))
            position.z -= 0.2f;
        if (Keyboard.isKeyDown(Keyboard.KEY_S))
            position.z += 0.2f;
        if (Keyboard.isKeyDown(Keyboard.KEY_A))
            position.x -= 0.2f;
        if (Keyboard.isKeyDown(Keyboard.KEY_D))
            position.x += 0.2f;
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
            position.y -= 0.2f;
        if (Keyboard.isKeyDown(Keyboard.KEY_UP))
            position.y += 0.2f;
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
            yaw -= 0.2f;
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
            yaw += 0.2f;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

}