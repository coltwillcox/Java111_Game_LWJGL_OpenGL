package renderEngineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
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

        //GUI.
        List<GuiTexture> guis = new ArrayList<>(); //List for all GUI objects (images).
        GuiTexture coins = new GuiTexture(loader.loadTexture("textureGuiCoins"), new Vector2f(-0.8f, 0.85f), new Vector2f(0.25f, 0.35f)); //Texture, position, scale.
        guis.add(coins);
        GuiTexture health = new GuiTexture(loader.loadTexture("textureGuiHealth"), new Vector2f(-0.7f, -0.85f), new Vector2f(0.25f, 0.35f));
        guis.add(health);
        GuiTexture armor = new GuiTexture(loader.loadTexture("textureGuiArmor"), new Vector2f(0.7f, -0.85f), new Vector2f(0.25f, 0.35f));
        guis.add(armor);
        GuiRenderer guiRenderer = new GuiRenderer(loader);

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("textureTerrainGrass"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("textureTerrainMud"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("textureTerrainGrassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("textureTerrainPath"));
        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("mapBlend"));
        Terrain terrain = new Terrain(-1, -1, loader, texturePack, blendMap, "mapHeight");
        //Terrain terrain2 = new Terrain(0, -1, loader, texturePack, blendMap, "mapHeight");

        List<Entity> entities = new ArrayList<>(); //List to keep all entities. Manual and random created ones.

        //Charizard.
        ModelData dataCharizard = OBJFileLoader.loadOBJ("modelCharizard");
        RawModel modelCharizard = loader.loadToVAO(dataCharizard.getVertices(), dataCharizard.getTextureCoords(), dataCharizard.getNormals(), dataCharizard.getIndices());
        TexturedModel staticModelCharizard = new TexturedModel(modelCharizard, new ModelTexture(loader.loadTexture("textureCharizard")));
        ModelTexture textureCharizard = staticModelCharizard.getTexture();
        textureCharizard.setShineDamper(100);
        textureCharizard.setReflectivity(100);
        Entity entityCharizard = new Entity(staticModelCharizard, new Vector3f(-50, -2, -50), 0, 0, 0, 0.5f);
        entities.add(entityCharizard);

        //Low poly apple tree.
        ModelData dataLowPolyTree = OBJFileLoader.loadOBJ("modelLowPolyTree");
        RawModel modelLowPolyTree = loader.loadToVAO(dataLowPolyTree.getVertices(), dataLowPolyTree.getTextureCoords(), dataLowPolyTree.getNormals(), dataLowPolyTree.getIndices());
        TexturedModel staticModelLowPolyTree = new TexturedModel(modelLowPolyTree, new ModelTexture(loader.loadTexture("textureAtlasLowPolyTree")));
        ModelTexture textureLowPolyTree = staticModelLowPolyTree.getTexture();
        textureLowPolyTree.setNumberOfRows(2);

        //Spikey tree.
        ModelData dataTree = OBJFileLoader.loadOBJ("modelTree");
        RawModel modelTree = loader.loadToVAO(dataTree.getVertices(), dataTree.getTextureCoords(), dataTree.getNormals(), dataTree.getIndices());
        TexturedModel staticModelTree = new TexturedModel(modelTree, new ModelTexture(loader.loadTexture("textureAtlasTree")));
        ModelTexture textureTree = staticModelTree.getTexture();
        textureTree.setNumberOfRows(2);

        //Grass.
        ModelData dataGrass = OBJFileLoader.loadOBJ("modelGrass");
        RawModel modelGrass = loader.loadToVAO(dataGrass.getVertices(), dataGrass.getTextureCoords(), dataGrass.getNormals(), dataGrass.getIndices());
        TexturedModel staticModelGrass = new TexturedModel(modelGrass, new ModelTexture(loader.loadTexture("textureGrass")));
        ModelTexture textureGrass = staticModelGrass.getTexture();
        textureGrass.setHasTransparency(true);
        textureGrass.setUseFakeLighting(true);

        //Fern.
        ModelData dataFern = OBJFileLoader.loadOBJ("modelFern");
        RawModel modelFern = loader.loadToVAO(dataFern.getVertices(), dataFern.getTextureCoords(), dataFern.getNormals(), dataFern.getIndices());
        TexturedModel staticModelFern = new TexturedModel(modelFern, new ModelTexture(loader.loadTexture("textureAtlasFern")));
        ModelTexture textureFern = staticModelFern.getTexture();
        textureFern.setNumberOfRows(2);
        textureFern.setHasTransparency(true);

        //Box.
        ModelData dataBox = OBJFileLoader.loadOBJ("modelBox");
        RawModel modelBox = loader.loadToVAO(dataBox.getVertices(), dataBox.getTextureCoords(), dataBox.getNormals(), dataBox.getIndices());
        TexturedModel staticModelBox = new TexturedModel(modelBox, new ModelTexture(loader.loadTexture("textureBox")));
        ModelTexture textureBox = staticModelBox.getTexture();
        textureBox.setHasTransparency(true);
        textureBox.setShineDamper(100);
        textureBox.setReflectivity(100);

        //Player
        ModelData dataPlayer = OBJFileLoader.loadOBJ("modelPlayer");
        RawModel modelPlayer = loader.loadToVAO(dataPlayer.getVertices(), dataPlayer.getTextureCoords(), dataPlayer.getNormals(), dataPlayer.getIndices());
        TexturedModel staticModelPlayer = new TexturedModel(modelPlayer, new ModelTexture(loader.loadTexture("texturePlayer")));
        ModelTexture texturePlayer = staticModelPlayer.getTexture();
        texturePlayer.setShineDamper(100);
        texturePlayer.setReflectivity(100);
        Player player = new Player(staticModelPlayer, new Vector3f(-30, 0, -30), 0, 225, 0, 0.5f);

        //Camera! Light!
        Camera camera = new Camera(player);
        Light light = new Light(new Vector3f(200, 200, 100), new Vector3f(1, 0.6f, 0.6f));

        //Random entities.
        Random random = new Random();
        float x;
        float y;
        float z;
        for (int i = 0; i < 200; i++) {
            x = random.nextFloat() * 800 - 400;
            z = random.nextFloat() * -600;
            y = terrain.getHeightOfTerrain(x, z);
            entities.add(new Entity(staticModelGrass, new Vector3f(x, y, z), 0, 0, 0, 1));

            x = random.nextFloat() * 800 - 400;
            z = random.nextFloat() * -600;
            y = terrain.getHeightOfTerrain(x, z);
            entities.add(new Entity(staticModelFern, random.nextInt(4), new Vector3f(x, y, z), 0, 0, 0, 1));

            x = random.nextFloat() * 800 - 400;
            z = random.nextFloat() * -600;
            y = terrain.getHeightOfTerrain(x, z);
            entities.add(new Entity(staticModelTree, random.nextInt(4), new Vector3f(x, y, z), 0, 0, 0, 10));

            x = random.nextFloat() * 800 - 400;
            z = random.nextFloat() * -600;
            y = terrain.getHeightOfTerrain(x, z);
            entities.add(new Entity(staticModelLowPolyTree, random.nextInt(4), new Vector3f(x, y, z), 0, 0, 0, 1));

            x = random.nextFloat() * 800 - 400;
            z = random.nextFloat() * -600;
            y = terrain.getHeightOfTerrain(x, z) + 2;
            entities.add(new Entity(staticModelBox, new Vector3f(x, y, z), 0, 0, 0, 2));
        }

        MasterRenderer renderer = new MasterRenderer();

        //Game logic, render...
        while (!Display.isCloseRequested()) {
            entityCharizard.increaseRotation(0, 0.1f, 0);
            camera.move();
            player.move(terrain);
            renderer.processEntity(player);
            renderer.processTerrain(terrain);
            //renderer.processTerrain(terrain2);
            for (Entity e : entities) renderer.processEntity(e);
            renderer.render(light, camera);
            guiRenderer.render(guis);
            DisplayManager.updateDisplay();
        }

        //Clear memory and close program.
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

}