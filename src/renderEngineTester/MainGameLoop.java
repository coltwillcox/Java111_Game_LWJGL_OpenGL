package renderEngineTester;

import entities.Camera;
import entities.Entity;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import models.RawModel;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

/**
 * Created by colt on 3/17/16.
 */

public class MainGameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        Loader loader = new Loader();
        StaticShader shader = new StaticShader();
        Renderer renderer = new Renderer(shader);

        //OpenGL expects vertices to be defined counter clockwise by default.
        float[] vertices = {
                -0.5f, 0.5f, 0,
                -0.5f, -0.5f, 0,
                0.5f, -0.5f, 0,
                0.5f, 0.5f, 0,

                -0.5f, 0.5f, 1,
                -0.5f, -0.5f, 1,
                0.5f, -0.5f, 1,
                0.5f, 0.5f, 1,

                0.5f, 0.5f, 0,
                0.5f, -0.5f, 0,
                0.5f, -0.5f, 1,
                0.5f, 0.5f, 1,

                -0.5f, 0.5f, 0,
                -0.5f, -0.5f, 0,
                -0.5f, -0.5f, 1,
                -0.5f, 0.5f, 1,

                -0.5f, 0.5f, 1,
                -0.5f, 0.5f, 0,
                0.5f, 0.5f, 0,
                0.5f, 0.5f, 1,

                -0.5f, -0.5f, 1,
                -0.5f, -0.5f, 0,
                0.5f, -0.5f, 0,
                0.5f, -0.5f, 1
        };
        int[] indices = {
                0, 1, 3,
                3, 1, 2,
                4, 5, 7,
                7, 5, 6,
                8, 9, 11,
                11, 9, 10,
                12, 13, 15,
                15, 13, 14,
                16, 17, 19,
                19, 17, 18,
                20, 21, 23,
                23, 21, 22
        };
        float[] textureCoords = {
                0, 0,
                0, 1,
                1, 1,
                1, 0,
                0, 0,
                0, 1,
                1, 1,
                1, 0,
                0, 0,
                0, 1,
                1, 1,
                1, 0,
                0, 0,
                0, 1,
                1, 1,
                1, 0,
                0, 0,
                0, 1,
                1, 1,
                1, 0,
                0, 0,
                0, 1,
                1, 1,
                1, 0
        };

        RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
        TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("textureJoy")));
        Entity entity = new Entity(staticModel, new Vector3f(0, 0, -5), 0, 0, 0, 1);
        Camera camera = new Camera();

        //Game logic, render...
        while (!Display.isCloseRequested()) {
            entity.increaseRotation(1, 1, 1);
            camera.move();
            renderer.prepare();
            shader.start();
            shader.loadViewMatrix(camera);
            renderer.render(entity, shader); //Drawing entity, every frame.
            shader.stop();
            DisplayManager.updateDisplay();
        }

        //Clear memory and close program.
        shader.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

}