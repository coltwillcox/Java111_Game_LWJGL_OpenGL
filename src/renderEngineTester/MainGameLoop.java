package renderEngineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import models.RawModel;
import renderEngine.MasterRenderer;
import terrains.Terrain;
import textures.ModelTexture;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by colt on 3/17/16.
 */

public class MainGameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        Loader loader = new Loader();
        List<Entity> entities = new ArrayList<>();

        //Dragon.
        ModelData dataDragon = OBJFileLoader.loadOBJ("modelDragon");
        RawModel modelDragon = loader.loadToVAO(dataDragon.getVertices(), dataDragon.getTextureCoords(), dataDragon.getNormals(), dataDragon.getIndices());
        TexturedModel staticModelDragon = new TexturedModel(modelDragon, new ModelTexture(loader.loadTexture("textureDragon")));
        ModelTexture textureDragon = staticModelDragon.getTexture();
        textureDragon.setShineDamper(100);
        textureDragon.setReflectivity(100);
        Entity entityDragon = new Entity(staticModelDragon, new Vector3f(0, 0, -25), 0, 0, 0, 1);
        entities.add(entityDragon);

        //Low poly apple tree.
        ModelData dataLowPolyTree = OBJFileLoader.loadOBJ("modelLowPolyTree");
        RawModel modelLowPolyTree = loader.loadToVAO(dataLowPolyTree.getVertices(), dataLowPolyTree.getTextureCoords(), dataLowPolyTree.getNormals(), dataLowPolyTree.getIndices());
        TexturedModel staticModelLowPolyTree = new TexturedModel(modelLowPolyTree, new ModelTexture(loader.loadTexture("textureLowPolyTree")));
        ModelTexture textureLowPolyTree = staticModelLowPolyTree.getTexture();
        textureLowPolyTree.setShineDamper(100);
        textureLowPolyTree.setReflectivity(100);
        Entity entityLowPolyTree = new Entity(staticModelLowPolyTree, new Vector3f(-10, 0, -25), 0, 0, 0, 0.1f);
        entities.add(entityLowPolyTree);

        //Spikey tree.
        ModelData dataTree = OBJFileLoader.loadOBJ("modelTree");
        RawModel modelTree = loader.loadToVAO(dataTree.getVertices(), dataTree.getTextureCoords(), dataTree.getNormals(), dataTree.getIndices());
        TexturedModel staticModelTree = new TexturedModel(modelTree, new ModelTexture(loader.loadTexture("textureTree")));
        ModelTexture textureTree = staticModelTree.getTexture();
        textureTree.setShineDamper(100);
        textureTree.setReflectivity(100);
        Entity entityTree = new Entity(staticModelTree, new Vector3f(-15, 0, -25), 0, 0, 0, 1);
        entities.add(entityTree);

        //Grass.
        ModelData dataGrass = OBJFileLoader.loadOBJ("modelGrass");
        RawModel modelGrass = loader.loadToVAO(dataGrass.getVertices(), dataGrass.getTextureCoords(), dataGrass.getNormals(), dataGrass.getIndices());
        TexturedModel staticModelGrass = new TexturedModel(modelGrass, new ModelTexture(loader.loadTexture("textureGrass")));
        ModelTexture textureGrass = staticModelGrass.getTexture();
        textureGrass.setHasTransparency(true);
        textureGrass.setUseFakeLighting(true);
        textureGrass.setShineDamper(100);
        textureGrass.setReflectivity(100);
        Entity entityGrass = new Entity(staticModelGrass, new Vector3f(-10, 0, -25), 0, 0, 0, 1);
        entities.add(entityGrass);

        //Fern.
        ModelData dataFern = OBJFileLoader.loadOBJ("modelFern");
        RawModel modelFern = loader.loadToVAO(dataFern.getVertices(), dataFern.getTextureCoords(), dataFern.getNormals(), dataFern.getIndices());
        TexturedModel staticModelFern = new TexturedModel(modelFern, new ModelTexture(loader.loadTexture("textureFern")));
        ModelTexture textureFern = staticModelFern.getTexture();
        textureFern.setHasTransparency(true);
        textureFern.setShineDamper(100);
        textureFern.setReflectivity(100);
        Entity entityFern = new Entity(staticModelFern, new Vector3f(-10, 0, -20), 0, 0, 0, 1);
        entities.add(entityFern);

        //Random entities.
        Random random = new Random();
        for (int i = 0; i < 500; i++)
            entities.add(new Entity(staticModelGrass, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, 0, 0, 1));

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
            for (Entity e:entities)renderer.processEntity(e);
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }

        //Clear memory and close program.
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

}