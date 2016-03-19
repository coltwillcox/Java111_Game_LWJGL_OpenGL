package renderEngineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import models.RawModel;
import renderEngine.OBJLoader;
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



        RawModel model = OBJLoader.loadObjModel("dragon", loader);
        TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("stallTexture")));
        Entity entity = new Entity(staticModel, new Vector3f(0, -5, -20), 0, 0, 0, 1);
        Light light = new Light(new Vector3f(0, 0, -20), new Vector3f(0, 1, 0));
        Camera camera = new Camera();

        //Game logic, render...
        while (!Display.isCloseRequested()) {
            entity.increaseRotation(0, 0.5f, 0);
            camera.move();
            renderer.prepare();
            shader.start();
            shader.loadLight(light);
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