package renderEngine;

import entities.Camera;
import entities.Entity;
import entities.EntityRenderer;
import entities.Light;
import entitiesNormalMapping.RendererNM;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;
import entities.EntityShader;
import shadows.ShadowMapMasterRenderer;
import terrains.TerrainRenderer;
import terrains.TerrainShader;
import skybox.SkyboxRenderer;
import terrains.Terrain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by colt on 3/19/16.
 */

public class MasterRenderer {

    public static final float FOV = 65;
    public static final float NEAR_PLANE = 0.1f;
    public static final float FAR_PLANE = 1000;
    public static final float RED = (float) 0.1f; //Fog color. Can be 100/255, easy to use with color pickers.
    public static final float GREEN = (float) 0.4f;
    public static final float BLUE = (float) 0.2f;
    private Matrix4f projectionMatrix;
    private EntityShader shader = new EntityShader();
    private EntityRenderer renderer;
    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader = new TerrainShader();
    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
    private Map<TexturedModel, List<Entity>> entitiesNormalMap = new HashMap<>();
    private List<Terrain> terrains = new ArrayList<>();
    private SkyboxRenderer skyboxRenderer;
    private RendererNM rendererNormalMap;
    private ShadowMapMasterRenderer shadowMapMasterRenderer;

    //Constructor.
    public MasterRenderer(Loader loader, Camera camera) {
        enableCulling(); //Do not render back faces of the model (inside of the model). Optimization.
        createProjectionMatrix();
        renderer = new EntityRenderer(shader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
        rendererNormalMap = new RendererNM(projectionMatrix);
        this.shadowMapMasterRenderer = new ShadowMapMasterRenderer(camera);
    }

    public static void enableCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    public void render(List<Light> lights, Camera camera, Vector4f plane) {
        prepare();
        shader.start();
        shader.loadPlane(plane);
        shader.loadSkyColor(RED, GREEN, BLUE);
        shader.loadLights(lights);
        shader.loadViewMatrix(camera);
        renderer.render(entities, shadowMapMasterRenderer.getToShadowMapSpaceMatrix());
        shader.stop();
        rendererNormalMap.render(entitiesNormalMap, plane, lights, camera);
        terrainShader.start();
        terrainShader.loadPlane(plane);
        terrainShader.loadSkyColor(RED, GREEN, BLUE);
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains, shadowMapMasterRenderer.getToShadowMapSpaceMatrix());
        terrainShader.stop();
        skyboxRenderer.render(camera, RED, GREEN, BLUE);
        terrains.clear();
        entities.clear();
        entitiesNormalMap.clear();
    }

    public void renderScene(List<Entity> entities, List<Entity> entitiesNormalMap, List<Terrain> terrains, List<Light> lights, Camera camera, Vector4f plane) {
        for (Terrain terrain: terrains)
            processTerrain(terrain);
        for (Entity entity:entities)
            processEntity(entity);
        for (Entity entity:entitiesNormalMap)
            processEntityNormalMap(entity);
        render(lights, camera, plane);
    }

    public void renderShadowMap(List<Entity> entityList, Light sun) {
        for (Entity entity : entityList)
            processEntity(entity);
        shadowMapMasterRenderer.render(entities, sun);
        entities.clear();
    }

    //Called once every frame.
    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClearColor(RED, GREEN, BLUE, 1); //Sky color. Red, green, blue, alpha.
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL13.glActiveTexture(GL13.GL_TEXTURE5);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMapTexture());
    }

    public void processTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);
        if (batch != null)
            batch.add(entity);
        else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    public void processEntityNormalMap(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entitiesNormalMap.get(entityModel);
        if (batch != null)
            batch.add(entity);
        else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entitiesNormalMap.put(entityModel, newBatch);
        }
    }

    private void createProjectionMatrix() {
        projectionMatrix = new Matrix4f();
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) (1f / Math.tan(Math.toRadians(FOV / 2f)));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public int getShadowMapTexture() {
        return shadowMapMasterRenderer.getShadowMap();
    }

    public void cleanUp() {
        shader.cleanUp();
        terrainShader.cleanUp();
        rendererNormalMap.cleanUp();
        shadowMapMasterRenderer.cleanUp();
    }

}