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
import textures.TerrainTexture;
import textures.TerrainTexturePack;

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

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("textureTerrainGrass"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("textureTerrainMud"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("textureTerrainGrassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("textureTerrainPath"));
        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("mapBlend"));

        List<Entity> entities = new ArrayList<>();

        //Dragon.
        ModelData dataBunny = OBJFileLoader.loadOBJ("modelBunny");
        RawModel modelBunny = loader.loadToVAO(dataBunny.getVertices(), dataBunny.getTextureCoords(), dataBunny.getNormals(), dataBunny.getIndices());
        TexturedModel staticModelBunny = new TexturedModel(modelBunny, new ModelTexture(loader.loadTexture("textureBunny")));
        ModelTexture textureBunny = staticModelBunny.getTexture();
        textureBunny.setShineDamper(100);
        textureBunny.setReflectivity(100);
        Entity entityBunny = new Entity(staticModelBunny, new Vector3f(0, 0, -25), 0, 0, 0, 0.2f);
        entities.add(entityBunny);

        //Low poly apple tree.
        ModelData dataLowPolyTree = OBJFileLoader.loadOBJ("modelLowPolyTree");
        RawModel modelLowPolyTree = loader.loadToVAO(dataLowPolyTree.getVertices(), dataLowPolyTree.getTextureCoords(), dataLowPolyTree.getNormals(), dataLowPolyTree.getIndices());
        TexturedModel staticModelLowPolyTree = new TexturedModel(modelLowPolyTree, new ModelTexture(loader.loadTexture("textureLowPolyTree")));
        ModelTexture textureLowPolyTree = staticModelLowPolyTree.getTexture();
        textureLowPolyTree.setShineDamper(100);
        textureLowPolyTree.setReflectivity(100);

        //Spikey tree.
        ModelData dataTree = OBJFileLoader.loadOBJ("modelTree");
        RawModel modelTree = loader.loadToVAO(dataTree.getVertices(), dataTree.getTextureCoords(), dataTree.getNormals(), dataTree.getIndices());
        TexturedModel staticModelTree = new TexturedModel(modelTree, new ModelTexture(loader.loadTexture("textureTree")));
        ModelTexture textureTree = staticModelTree.getTexture();
        textureTree.setShineDamper(100);
        textureTree.setReflectivity(100);

        //Grass.
        ModelData dataGrass = OBJFileLoader.loadOBJ("modelGrass");
        RawModel modelGrass = loader.loadToVAO(dataGrass.getVertices(), dataGrass.getTextureCoords(), dataGrass.getNormals(), dataGrass.getIndices());
        TexturedModel staticModelGrass = new TexturedModel(modelGrass, new ModelTexture(loader.loadTexture("textureGrass")));
        ModelTexture textureGrass = staticModelGrass.getTexture();
        textureGrass.setHasTransparency(true);
        textureGrass.setUseFakeLighting(true);
        textureGrass.setShineDamper(100);
        textureGrass.setReflectivity(100);

        //Fern.
        ModelData dataFern = OBJFileLoader.loadOBJ("modelFern");
        RawModel modelFern = loader.loadToVAO(dataFern.getVertices(), dataFern.getTextureCoords(), dataFern.getNormals(), dataFern.getIndices());
        TexturedModel staticModelFern = new TexturedModel(modelFern, new ModelTexture(loader.loadTexture("textureFern")));
        ModelTexture textureFern = staticModelFern.getTexture();
        textureFern.setHasTransparency(true);
        textureFern.setShineDamper(100);
        textureFern.setReflectivity(100);

        //Random entities.
        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            entities.add(new Entity(staticModelGrass, new Vector3f(random.nextFloat() * 200 - 100, 0, random.nextFloat() * -200), 0, 0, 0, 1));
            entities.add(new Entity(staticModelFern, new Vector3f(random.nextFloat() * 200 - 100, 0, random.nextFloat() * -200), 0, 0, 0, 0.5f));
            entities.add(new Entity(staticModelTree, new Vector3f(random.nextFloat() * 200 - 100, 0, random.nextFloat() * -200), 0, 0, 0, 4));
            entities.add(new Entity(staticModelLowPolyTree, new Vector3f(random.nextFloat() * 200 - 100, 0, random.nextFloat() * -200), 0, 0, 0, 0.3f));
        }

        Light light = new Light(new Vector3f(200, 200, 100), new Vector3f(1, 0.6f, 0.6f));

        Terrain terrain = new Terrain(-1, -1, loader, texturePack, blendMap);
        Terrain terrain2 = new Terrain(0, -1, loader, texturePack, blendMap);

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