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
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;
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

        //Terrain.
        List<Terrain> terrains = new ArrayList<>();
        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("textureTerrainGrass"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("textureTerrainMud"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("textureTerrainGrassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("textureTerrainPath"));
        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("mapBlend"));
        Terrain terrain = new Terrain(0, 0, loader, texturePack, blendMap, "mapHeight");
        terrains.add(terrain);

        //Entities
        List<Entity> entities = new ArrayList<>(); //List to keep all entities. Manual and random created ones.
        //Charizard.
        ModelData dataCharizard = OBJFileLoader.loadOBJ("modelCharizard");
        RawModel modelCharizard = loader.loadToVAO(dataCharizard.getVertices(), dataCharizard.getTextureCoords(), dataCharizard.getNormals(), dataCharizard.getIndices());
        TexturedModel staticModelCharizard = new TexturedModel(modelCharizard, new ModelTexture(loader.loadTexture("textureCharizard")));
        ModelTexture textureCharizard = staticModelCharizard.getTexture();
        textureCharizard.setShineDamper(100);
        textureCharizard.setReflectivity(100);
        Entity entityCharizard = new Entity(staticModelCharizard, new Vector3f(50, terrain.getHeightOfTerrain(50, 50), 50), 0, 0, 0, 0.5f);
        entities.add(entityCharizard);
        //Low poly apple tree.
        ModelData dataLowPolyTree = OBJFileLoader.loadOBJ("modelLowPolyTree");
        RawModel modelLowPolyTree = loader.loadToVAO(dataLowPolyTree.getVertices(), dataLowPolyTree.getTextureCoords(), dataLowPolyTree.getNormals(), dataLowPolyTree.getIndices());
        TexturedModel staticModelLowPolyTree = new TexturedModel(modelLowPolyTree, new ModelTexture(loader.loadTexture("textureAtlasLowPolyTree")));
        ModelTexture textureLowPolyTree = staticModelLowPolyTree.getTexture();
        textureLowPolyTree.setNumberOfRows(2);
        //Pine tree.
        ModelData dataTree = OBJFileLoader.loadOBJ("modelTree");
        RawModel modelTree = loader.loadToVAO(dataTree.getVertices(), dataTree.getTextureCoords(), dataTree.getNormals(), dataTree.getIndices());
        TexturedModel staticModelTree = new TexturedModel(modelTree, new ModelTexture(loader.loadTexture("textureTree")));
        ModelTexture textureTree = staticModelTree.getTexture();
        textureTree.setHasTransparency(true);
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
        //Lamp.
        ModelData dataLamp = OBJFileLoader.loadOBJ("modelLamp");
        RawModel modelLamp = loader.loadToVAO(dataLamp.getVertices(), dataLamp.getTextureCoords(), dataLamp.getNormals(), dataLamp.getIndices());
        TexturedModel staticModelLamp = new TexturedModel(modelLamp, new ModelTexture(loader.loadTexture("textureLamp")));
        ModelTexture textureLamp = staticModelLamp.getTexture();
        textureLamp.setUseFakeLighting(true);
        entities.add(new Entity(staticModelLamp, new Vector3f(455, terrain.getHeightOfTerrain(455, 400), 400), 0, 0, 0, 1));
        //Random entities locations.
        Random random = new Random();
        float x;
        float y;
        float z;
        for (int i = 0; i < 200; i++) {
            x = random.nextFloat() * 800;
            z = random.nextFloat() * 800;
            y = terrain.getHeightOfTerrain(x, z);
            entities.add(new Entity(staticModelGrass, new Vector3f(x, y, z), 0, 0, 0, 1));

            x = random.nextFloat() * 800;
            z = random.nextFloat() * 800;
            y = terrain.getHeightOfTerrain(x, z);
            entities.add(new Entity(staticModelFern, random.nextInt(4), new Vector3f(x, y, z), 0, 0, 0, 1));

            x = random.nextFloat() * 800;
            z = random.nextFloat() * 800;
            y = terrain.getHeightOfTerrain(x, z);
            entities.add(new Entity(staticModelTree, random.nextInt(4), new Vector3f(x, y, z), 0, 0, 0, 1.5f));


            x = random.nextFloat() * 800;
            z = random.nextFloat() * 600;
            y = terrain.getHeightOfTerrain(x, z) + 2;
            entities.add(new Entity(staticModelBox, new Vector3f(x, y, z), 0, 0, 0, 2));
        }

        //Player
        ModelData dataPlayer = OBJFileLoader.loadOBJ("modelPlayer");
        RawModel modelPlayer = loader.loadToVAO(dataPlayer.getVertices(), dataPlayer.getTextureCoords(), dataPlayer.getNormals(), dataPlayer.getIndices());
        TexturedModel staticModelPlayer = new TexturedModel(modelPlayer, new ModelTexture(loader.loadTexture("texturePlayer")));
        ModelTexture texturePlayer = staticModelPlayer.getTexture();
        texturePlayer.setShineDamper(100);
        texturePlayer.setReflectivity(100);
        Player player = new Player(staticModelPlayer, new Vector3f(450, 0, 450), 0, 225, 0, 0.5f);

        //Lights! Camera!
        List<Light> lights = new ArrayList<>();
        lights.add(new Light(new Vector3f(1000, 1000, 7000), new Vector3f(0.8f, 0.8f, 1.0f))); //Sun. :)
        lights.add(new Light(new Vector3f(455, terrain.getHeightOfTerrain(470, 400) + 14, 400), new Vector3f(2f, 2f, 0.1f), new Vector3f(1, 0.01f, 0.002f))); //Lamp light.
        Camera camera = new Camera(player);

        MasterRenderer renderer = new MasterRenderer(loader);
        MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);

        //Water.
        WaterFrameBuffers fbos = new WaterFrameBuffers();
        WaterShader waterShader = new WaterShader();
        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), fbos);
        List<WaterTile> waterTiles = new ArrayList<>();
        WaterTile water = new WaterTile(400, 400, -15); //x, z, height (or y).
        waterTiles.add(water);


        //Game logic, rendering...
        while (!Display.isCloseRequested()) {
            player.move(terrain);
            camera.move();
            picker.update();

            GL11.glEnable(GL30.GL_CLIP_DISTANCE0); //Enable clip plane 0.

            //Render reflection texture.
            fbos.bindReflectionFrameBuffer();
            float distance = 2 * (camera.getPosition().getY() - water.getHeight());
            camera.getPosition().setY(camera.getPosition().getY() - distance);
            camera.invertPitch();
            renderer.renderScene(entities, terrains, lights, camera, new Vector4f(0, 1, 0, -water.getHeight() )); //Clip plane with height.
            camera.getPosition().setY(camera.getPosition().getY() + distance);
            camera.invertPitch();

            //Render refraction texture.
            fbos.bindRefractionFrameBuffer();
            renderer.renderScene(entities, terrains, lights, camera, new Vector4f(0, -1, 0, water.getHeight()));

            //Render scene and player to screen.
            GL11.glDisable(GL30.GL_CLIP_DISTANCE0); //Disable cliping plane.
            fbos.unbindCurrentFrameBuffer(); //Unbind buffer first.
            renderer.processEntity(player);
            renderer.renderScene(entities, terrains, lights, camera, new Vector4f(0, -1, 0, 15));

            //Dragging Charizard.
            entityCharizard.increaseRotation(0, 0.1f, 0);
            Vector3f terrainPoint = picker.getCurrentTerrainPoint();
            if (terrainPoint != null && Mouse.isButtonDown(0))
                entityCharizard.setPosition(terrainPoint);

            //Render water.
            waterRenderer.render(waterTiles, camera);

            //Render GUI.
            guiRenderer.render(guis);

            DisplayManager.updateDisplay();
        }

        //Clear memory and close program.
        fbos.cleanUp();
        waterShader.cleanUp();
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

}