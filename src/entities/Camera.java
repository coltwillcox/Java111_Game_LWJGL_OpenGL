package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by colt on 3/18/16.
 */

public class Camera {

    private Vector3f position = new Vector3f(0, 0, 0);
    private float pitch = 10; //Up and down look. Default is 10.
    private float yaw; //Left and right look.
    private float roll;
    private Player player;
    private float distanceFromPlayer = 30;
    private float angleAroundPlayer = 0;
    private float horizontalDistance;
    private float verticalDistance;


    //Constructor.
    public Camera(Player player) {
        this.player = player;
    }

    public void move() {
        calculateZoom();
        calculatePitch();
        calculateAngleAroundPlayer();
        horizontalDistance = calculateHorizontalDistance();
        verticalDistance = calculateVerticalDistance();
        calculateCameraPosition();
        yaw = 180 - (player.getRotY() + angleAroundPlayer);
    }

    private void calculateZoom() {
        float zoomLevel = Mouse.getDWheel() * 0.05f;
        if (distanceFromPlayer - zoomLevel > 5f && distanceFromPlayer - zoomLevel < 60f) //Min and max zoom.
            distanceFromPlayer -= zoomLevel;
    }

    private void calculatePitch() {
        if (Mouse.isButtonDown(1)) {
            float pitchChange = Mouse.getDY() * 0.1f;
            if (pitch - pitchChange > 1 && pitch - pitchChange < 85) //Min and max pitch.
                pitch -= pitchChange;
        }
    }

    private void calculateAngleAroundPlayer() {
        if (Mouse.isButtonDown(1)) {
            float angleChange = Mouse.getDX() * 0.3f;
            angleAroundPlayer -= angleChange;
        }
    }

    private float calculateHorizontalDistance() {
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance() {
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }

    private void calculateCameraPosition() {
        float theta = player.getRotY() + angleAroundPlayer;
        float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
        position.setX(player.getPosition().getX() - offsetX);
        position.setZ(player.getPosition().getZ() - offsetZ);
        position.setY(player.getPosition().getY() + verticalDistance);
    }

    public void invertPitch() {
        this.pitch = -pitch;
    }

    //Getters.
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