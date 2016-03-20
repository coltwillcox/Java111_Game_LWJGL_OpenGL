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
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;

/**
 * Created by colt on 3/17/16.
 */

public class MainGameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        Loader loader = new Loader();

        //Dragon.
        RawModel modelDragon = OBJLoader.loadObjModel("modelDragon", loader);
        TexturedModel staticModelDragon = new TexturedModel(modelDragon, new ModelTexture(loader.loadTexture("textureDragon")));
        ModelTexture textureDragon = staticModelDragon.getTexture();
        textureDragon.setShineDamper(100);
        textureDragon.setReflectivity(100);
        Entity entityDragon = new Entity(staticModelDragon, new Vector3f(0, 0, -25), 0, 0, 0, 1);

        //Low poly apple tree.
        RawModel modelLowPolyTree = OBJLoader.loadObjModel("modelLowPolyTree", loader);
        TexturedModel staticModelLowPolyTree = new TexturedModel(modelLowPolyTree, new ModelTexture(loader.loadTexture("textureLowPolyTree")));
        ModelTexture textureLowPolyTree = staticModelLowPolyTree.getTexture();
        textureLowPolyTree.setShineDamper(100);
        textureLowPolyTree.setReflectivity(100);
        Entity entityLowPolyTree = new Entity(staticModelLowPolyTree, new Vector3f(-10, 0, -25), 0, 0, 0, 0.1f);

        //Spikey tree.
        RawModel modelTree = OBJLoader.loadObjModel("modelTree", loader);
        TexturedModel staticModelTree = new TexturedModel(modelTree, new ModelTexture(loader.loadTexture("textureTree")));
        ModelTexture textureTree = staticModelTree.getTexture();
        textureTree.setShineDamper(100);
        textureTree.setReflectivity(100);
        Entity entityTree = new Entity(staticModelTree, new Vector3f(-15, 0, -25), 0, 0, 0, 1);

        //Grass.
        RawModel modelGrass = OBJLoader.loadObjModel("modelGrass", loader);
        TexturedModel staticModelGrass = new TexturedModel(modelGrass, new ModelTexture(loader.loadTexture("textureGrass")));
        ModelTexture textureGrass = staticModelGrass.getTexture();
        textureGrass.setHasTransparency(true);
        textureGrass.setUseFakeLighting(true);
        textureGrass.setShineDamper(100);
        textureGrass.setReflectivity(100);
        Entity entityGrass = new Entity(staticModelGrass, new Vector3f(-10, 0, -25), 0, 0, 0, 1);

        //Fern.
        RawModel modelFern = OBJLoader.loadObjModel("modelFern", loader);
        TexturedModel staticModelFern = new TexturedModel(modelFern, new ModelTexture(loader.loadTexture("textureFern")));
        ModelTexture textureFern = staticModelFern.getTexture();
        textureFern.setHasTransparency(true);
        textureFern.setShineDamper(100);
        textureFern.setReflectivity(100);
        Entity entityFern = new Entity(staticModelFern, new Vector3f(-10, 0, -20), 0, 0, 0, 1);

        Light light = new Light(new Vector3f(200, 200, 100), new Vector3f(1, 1, 1));

        Terrain terrain = new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("textureTerrainGrass")));
        Terrain terrain2 = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("textureTerrainGrass")));

        Camera camera = new Camera();
        MasterRenderer renderer = new MasterRenderer();

        //Game logic, render...
        while (!Display.isCloseRequested()) {
            //entityDragon.increaseRotation(0, 0.1f, 0);
            camera.move();
            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            renderer.processEntity(entityDragon);
            renderer.processEntity(entityLowPolyTree);
            renderer.processEntity(entityTree);
            renderer.processEntity(entityGrass);
            renderer.processEntity(entityFern);
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }

        //Clear memory and close program.
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

}