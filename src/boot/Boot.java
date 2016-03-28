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
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
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
        ParticleMaster.init(loader, renderer.getProjectionMatrix());
        //Flame particle. Used for mouse click.
        ParticleTexture particleTextureFlame = new ParticleTexture(loader.loadTexture("textureParticleAtlasFlame"), 4, false);
        ParticleSystem particleSystemFlame = new ParticleSystem(particleTextureFlame, 40, 10, 0.1f, 5, 1.5f);
        particleSystemFlame.randomizeRotation();
        particleSystemFlame.setDirection(new Vector3f(0, 1, 0), 0.1f);
        particleSystemFlame.setLifeError(0.1f);
        particleSystemFlame.setSpeedError(0.4f);
        particleSystemFlame.setScaleError(0.8f);
        //Fire particle.
        ParticleTexture particleTextureFire = new ParticleTexture(loader.loadTexture("textureParticleAtlasFire"), 8, true);
        ParticleSystem particleSystemFire = new ParticleSystem(particleTextureFire, 40, 5, 0, 3, 5f);
        particleSystemFire.randomizeRotation();
        particleSystemFire.setDirection(new Vector3f(0, 1, 0), 0.1f);
        particleSystemFire.setLifeError(0.1f);
        particleSystemFire.setSpeedError(0.4f);
        particleSystemFire.setScaleError(0.8f);
        //Smoke particle.
        ParticleTexture particleTextureSmoke = new ParticleTexture(loader.loadTexture("textureParticleAtlasSmoke"), 8, true);
        ParticleSystem particleSystemSmoke = new ParticleSystem(particleTextureSmoke, 40, 5, 0, 3, 5f);
        particleSystemSmoke.randomizeRotation();
        particleSystemSmoke.setDirection(new Vector3f(0, 1, 0), 0.1f);
        particleSystemSmoke.setLifeError(0.1f);
        particleSystemSmoke.setSpeedError(0.4f);
        particleSystemSmoke.setScaleError(0.8f);

        //Text.
        TextMaster.init(loader);
        FontType fontArialDF = new FontType(loader.loadTexture("fontArialDF"), new File("res/fontArialDF.fnt"));
        GUIText text = new GUIText("mrk ZAJAC", 3, fontArialDF, new Vector2f(0, 0.1f), 1f, true);
        text.setColor(0, 1, 0);

        //GUI.
        GuiRenderer guiRenderer = new GuiRenderer(loader);
        List<GuiTexture> guis = new ArrayList<>(); //List for all GUI objects (images).
        GuiTexture hud = new GuiTexture(loader.loadTexture("textureHUD"), new Vector2f(0, 0), new Vector2f(1.6f, 2.55f)); //Texture, position, scale.
        guis.add(hud);

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
        //Stormtrooper. Normal map. Player.
        TexturedModel texturedModelStormtrooper = new TexturedModel(OBJFileLoaderNM.loadOBJ("modelStormtrooper", loader), new ModelTexture(loader.loadTexture("textureStormtrooper")));
        texturedModelStormtrooper.getTexture().setMapNormal(loader.loadTexture("mapNormalStormtrooper"));
        texturedModelStormtrooper.getTexture().setShineDamper(100);
        texturedModelStormtrooper.getTexture().setReflectivity(1f);
        //R2D2. Normal map.
        TexturedModel texturedModelR2D2 = new TexturedModel(OBJFileLoaderNM.loadOBJ("modelR2D2", loader), new ModelTexture(loader.loadTexture("textureR2D2")));
        texturedModelR2D2.getTexture().setMapNormal(loader.loadTexture("mapNormalR2D2"));
        texturedModelR2D2.getTexture().setShineDamper(100);
        texturedModelR2D2.getTexture().setReflectivity(1f);
        entitiesNormalMap.add(new Entity(texturedModelR2D2, new Vector3f(380, terrain.getHeightOfTerrain(380, 470), 470), 0, 180, 0, 1.5f));
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
        Player player = new Player(texturedModelStormtrooper, new Vector3f(450, 0, 450), 0, 225, 0, 2f);
        entities.add(player);

        //Lights! Camera!
        List<Light> lights = new ArrayList<>();
        lights.add(new Light(new Vector3f(1000, 1000, 7000), new Vector3f(0.8f, 0.8f, 1.0f))); //Sun. :)
        lights.add(new Light(new Vector3f(455, terrain.getHeightOfTerrain(470, 400) + 14, 400), new Vector3f(2f, 2f, 0.1f), new Vector3f(1, 0.01f, 0.002f))); //Lamp light.
        Camera camera = new Camera(player);

        //Mouse picker.
        MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);

        //Water.
        WaterFrameBuffers fbos = new WaterFrameBuffers();
        WaterShader waterShader = new WaterShader();
        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), fbos);
        List<WaterTile> waterTiles = new ArrayList<>();
        WaterTile water = new WaterTile(400, 400, 0); //x, z, height (or y).
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

            //Render water.
            waterRenderer.render(waterTiles, camera, lights.get(0)); //0 is Sun.

            //Render particles.
            ParticleMaster.renderParticles(camera);
            Vector3f terrainPoint = picker.getCurrentTerrainPoint();
            if (terrainPoint != null && Mouse.isButtonDown(0))
                particleSystemFlame.generateParticles(terrainPoint);
            particleSystemFire.generateParticles(new Vector3f(300, terrain.getHeightOfTerrain(300, 450), 450));
            particleSystemSmoke.generateParticles(new Vector3f(380, terrain.getHeightOfTerrain(380, 470), 470));

            //Render hud and text if camera goes FPS.
            if (camera.getDistanceFromPlayer() <= 0) {
                guiRenderer.render(guis);
                TextMaster.render();
            }

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