package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

/**
 * Created by colt on 3/17/16.
 */

public class DisplayManager {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 800;
    private static final int FPS_CAP = 60;
    private static long lastFrameTime;
    private static float delta;

    public static void createDisplay() {
        ContextAttribs attribs = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true); //OpenGL version to use.
        try {
            Display.setTitle("NiSamNeZnamKakoSamOsnovnuŠkoluZavršio");
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), attribs);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        GL11.glViewport(0, 0, WIDTH, HEIGHT); //Where in the display to render the game? Use whole.
        lastFrameTime = getCurrentTime();
    }

    public static void updateDisplay() {
        Display.sync(FPS_CAP);
        Display.update();
        long currentFrameTime = getCurrentTime();
        delta = (currentFrameTime - lastFrameTime) / 1000f; //Give delta from last frame in seconds.
        lastFrameTime = currentFrameTime;
    }

    public static void closeDisplay() {
        Display.destroy();
    }

    private static long getCurrentTime() {
        return Sys.getTime() * 1000 / Sys.getTimerResolution(); //Ticks * 1000 / Number of ticks per second = Time in miliseconds.
    }

    public static float getFrameTimeSeconds() {
        return delta;
    }

}