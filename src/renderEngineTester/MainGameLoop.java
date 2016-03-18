package renderEngineTester;

import models.TexturedModel;
import org.lwjgl.opengl.Display;
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
        Renderer renderer = new Renderer();
        StaticShader shader = new StaticShader();

        //OpenGL expects vertices to be defined counter clockwise by default.
        float[] vertices = {
                -0.5f, 0.5f, 0f,    //V0
                -0.5f, -0.5f, 0f,   //V1
                0.5f, -0.5f, 0f,    //V2
                0.5f, 0.5f, 0f      //V3
        };
        int[] indices = {
                0, 1, 3,    //Top left triangle (V0, V1, V3).
                3, 1, 2     //Bottom right triangle (V3, V1, V2).
        };
        float[] textureCoords = {
                0, 0,   //V0
                0, 1,   //V1
                1, 1,   //V2
                1, 0    //V3
        };

        RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
        ModelTexture texture = new ModelTexture(loader.loadTexture("textureJoy")); //Will load texture and return its ID.
        TexturedModel texturedModel = new TexturedModel(model, texture);

        //Game logic, render...
        while(!Display.isCloseRequested()){
            renderer.prepare();
            shader.start();
            renderer.render(texturedModel);
            shader.stop();
            DisplayManager.updateDisplay();
        }

        //Clear memory and close program.
        shader.cleanUo();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

}