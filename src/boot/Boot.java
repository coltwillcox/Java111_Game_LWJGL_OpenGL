package boot;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import objConverterNormalMapping.OBJFileLoaderNM;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import particles.Particle;
import particles.ParticleMaster;
import particles.ParticleSystem;
import particles.ParticleTexture;
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
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by colt on 3/17/16.
 */

public class Boot {

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        Loader loader = new Loader();
        MasterRenderer renderer = new MasterRenderer(loader);

        //Particles.
        ParticleTexture particleTexture = new ParticleTexture(loader.loadTexture("textureParticleAtlasFire"), 4, false);
        ParticleMaster.init(loader, renderer.getProjectionMatrix());
        ParticleSystem particleSystem = new ParticleSystem(particleTexture, 400, 10, 0.1f, 5, 1.5f);
        particleSystem.randomizeRotation();
        particleSystem.setDirection(new Vector3f(0, 1, 0), 0.1f);
        particleSystem.setLifeError(0.1f);
        particleSystem.setSpeedError(0.4f);
        particleSystem.setScaleError(0.8f);

        //Text.
        TextMaster.init(loader);
        FontType fontArialDF = new FontType(loader.loadTexture("fontArialDF"), new File("res/fontArialDF.fnt"));
        GUIText text = new GUIText("mrk ZAJAC", 3, fontArialDF, new Vector2f(0, 0.1f), 1f, true);
        text.setColor(0, 1, 0);

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
        List<Entity> entitiesNormalMap = new ArrayList<>(); //Normal Mapped entities.
        //Charizard.
        ModelData dataCharizard = OBJFileLoader.loadOBJ("modelCharizard");
        RawModel modelCharizard = loader.loadToVAO(dataCharizard.getVertices(), dataCharizard.getTextureCoords(), dataCharizard.getNormals(), dataCharizard.getIndices());
        TexturedModel staticModelCharizard = new TexturedModel(modelCharizard, new ModelTexture(loader.loadTexture("textureCharizard")));
        ModelTexture textureCharizard = staticModelCharizard.getTexture();
        textureCharizard.setShineDamper(100);
        textureCharizard.setReflectivity(100);
        Entity entityCharizard = new Entity(staticModelCharizard, new Vector3f(380, terrain.getHeightOfTerrain(380, 470), 470), 0, 0, 0, 0.5f);
        entities.add(entityCharizard);
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
        //Rocks.
        ModelData dataRocks = OBJFileLoader.loadOBJ("modelRocks");
        RawModel modelRocks = loader.loadToVAO(dataRocks.getVertices(), dataRocks.getTextureCoords(), dataRocks.getNormals(), dataRocks.getIndices());
        TexturedModel staticModelRocks = new TexturedModel(modelRocks, new ModelTexture(loader.loadTexture("textureRocks")));
        ModelTexture textureRocks = staticModelBox.getTexture();
        //Lamp.
        ModelData dataLamp = OBJFileLoader.loadOBJ("modelLamp");
        RawModel modelLamp = loader.loadToVAO(dataLamp.getVertices(), dataLamp.getTextureCoords(), dataLamp.getNormals(), dataLamp.getIndices());
        TexturedModel staticModelLamp = new TexturedModel(modelLamp, new ModelTexture(loader.loadTexture("textureLamp")));
        ModelTexture textureLamp = staticModelLamp.getTexture();
        textureLamp.setUseFakeLighting(true);
        entities.add(new Entity(staticModelLamp, new Vector3f(455, terrain.getHeightOfTerrain(455, 400), 400), 0, 0, 0, 1));
        //Barrel. Normal map.
        TexturedModel texturedModelBarrel = new TexturedModel(OBJFileLoaderNM.loadOBJ("modelBarrel", loader), new ModelTexture(loader.loadTexture("textureBarrel")));
        texturedModelBarrel.getTexture().setMapNormal(loader.loadTexture("mapNormalBarrel"));
        texturedModelBarrel.getTexture().setShineDamper(10);
        texturedModelBarrel.getTexture().setReflectivity(0.5f);
        //Boulder. Normal map.
        TexturedModel texturedModelBoulder = new TexturedModel(OBJFileLoaderNM.loadOBJ("modelBoulder", loader), new ModelTexture(loader.loadTexture("textureBoulder")));
        texturedModelBoulder.getTexture().setMapNormal(loader.loadTexture("mapNormalBoulder"));
        texturedModelBoulder.getTexture().setShineDamper(10);
        texturedModelBoulder.getTexture().setReflectivity(0.5f);
        //Random entities locations.
        Random random = new Random();
        float x;
        float y;
        float z;
        for (int i = 0; i < 400; i++) {
            x = random.nextFloat() * 800;
            z = random.nextFloat() * 800;
            y = terrain.getHeightOfTerrain(x, z);
            entities.add(new Entity(staticModelTree, random.nextInt(4), new Vector3f(x, y, z), 0, 0, 0, 1.5f));
        }
        for (int i = 0; i < 50; i++) {
            x = random.nextFloat() * 800;
            z = random.nextFloat() * 800;
            y = terrain.getHeightOfTerrain(x, z);
            entities.add(new Entity(staticModelGrass, new Vector3f(x, y, z), 0, 0, 0, 1));

            x = random.nextFloat() * 800;
            z = random.nextFloat() * 800;
            y = terrain.getHeightOfTerrain(x, z);
            entities.add(new Entity(staticModelFern, random.nextInt(4), new Vector3f(x, y, z), 0, 0, 0, 1));

            x = random.nextFloat() * 800;
            z = random.nextFloat() * 600;
            y = terrain.getHeightOfTerrain(x, z) + 2;
            entities.add(new Entity(staticModelBox, new Vector3f(x, y, z), 0, 0, 0, 2));

            x = random.nextFloat() * 800;
            z = random.nextFloat() * 600;
            y = terrain.getHeightOfTerrain(x, z) - 1;
            entities.add(new Entity(staticModelRocks, new Vector3f(x, y, z), 0, 0, 0, 2));

            x = random.nextFloat() * 800;
            z = random.nextFloat() * 800;
            y = terrain.getHeightOfTerrain(x, z) + 3;
            entitiesNormalMap.add(new Entity(texturedModelBarrel, new Vector3f(x, y, z), 0, 0, 0, 0.5f));

            x = random.nextFloat() * 800;
            z = random.nextFloat() * 800;
            y = terrain.getHeightOfTerrain(x, z) + 2;
            entitiesNormalMap.add(new Entity(texturedModelBoulder, new Vector3f(x, y, z), 0, 0, 0, 0.5f));
        }

        //Player
        ModelData dataPlayer = OBJFileLoader.loadOBJ("modelPlayer");
        RawModel modelPlayer = loader.loadToVAO(dataPlayer.getVertices(), dataPlayer.getTextureCoords(), dataPlayer.getNormals(), dataPlayer.getIndices());
        TexturedModel staticModelPlayer = new TexturedModel(modelPlayer, new ModelTexture(loader.loadTexture("texturePlayer")));
        ModelTexture texturePlayer = staticModelPlayer.getTexture();
        texturePlayer.setShineDamper(100);
        texturePlayer.setReflectivity(100);
        Player player = new Player(staticModelPlayer, new Vector3f(450, 0, 450), 0, 225, 0, 0.5f);
        entities.add(player);

        //Lights! Camera!
        List<Light> lights = new ArrayList<>();
        lights.add(new Light(new Vector3f(1000, 1000, 7000), new Vector3f(0.8f, 0.8f, 1.0f))); //Sun. :)
        lights.add(new Light(new Vector3f(455, terrain.getHeightOfTerrain(470, 400) + 14, 400), new Vector3f(2f, 2f, 0.1f), new Vector3f(1, 0.01f, 0.002f))); //Lamp light.
        Camera camera = new Camera(player);

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
            ParticleMaster.update(camera);

            GL11.glEnable(GL30.GL_CLIP_DISTANCE0); //Enable clip plane 0.

            //Render reflection texture.
            fbos.bindReflectionFrameBuffer();
            float distance = 2 * (camera.getPosition().getY() - water.getHeight());
            camera.getPosition().setY(camera.getPosition().getY() - distance);
            camera.invertPitch();
            renderer.renderScene(entities, entitiesNormalMap, terrains, lights, camera, new Vector4f(0, 1, 0, -water.getHeight() + 1f )); //Clip plane with height.
            camera.getPosition().setY(camera.getPosition().getY() + distance);
            camera.invertPitch();

            //Render refraction texture.
            fbos.bindRefractionFrameBuffer();
            renderer.renderScene(entities, entitiesNormalMap, terrains, lights, camera, new Vector4f(0, -1, 0, water.getHeight()));

            //Render scene and player to screen.
            GL11.glDisable(GL30.GL_CLIP_DISTANCE0); //Disable cliping plane.
            fbos.unbindCurrentFrameBuffer(); //Unbind buffer first.
            renderer.processEntity(player);
            renderer.renderScene(entities, entitiesNormalMap, terrains, lights, camera, new Vector4f(0, -1, 0, 15));

            //Charizard rotation.
            entityCharizard.increaseRotation(0, 0.1f, 0);

            //Render water.
            waterRenderer.render(waterTiles, camera, lights.get(0)); //0 is Sun.

            //Render particles.
            ParticleMaster.renderParticles(camera);
            Vector3f terrainPoint = picker.getCurrentTerrainPoint();
            if (terrainPoint != null && Mouse.isButtonDown(0))
                particleSystem.generateParticles(terrainPoint);

            //Render GUI.
            guiRenderer.render(guis);

            //Render text.
            TextMaster.render();

            DisplayManager.updateDisplay();
        }

        //Clear memory and close program.
        ParticleMaster.cleanUp();
        TextMaster.cleanUp();
        fbos.cleanUp();
        waterShader.cleanUp();
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

}