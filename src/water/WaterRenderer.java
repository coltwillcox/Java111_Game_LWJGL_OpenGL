package water;

import java.util.List;

import entities.Light;
import models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import toolbox.Maths;
import entities.Camera;

public class WaterRenderer {

    public static final String MAP_DUDV = "mapDUDVWater";
    public static final String MAP_NORMAL = "mapNormalWater";
    public static final float WAVE_SPEED = 0.03f;
    private float moveFactor = 0;
    private int mapDUDV;
    private int mapNormal;
    private RawModel quad;
    private WaterShader shader;
    private WaterFrameBuffers fbos;

    //Constructor.
    public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffers fbos) {
        this.shader = shader;
        this.fbos = fbos;
        mapDUDV = loader.loadTexture(MAP_DUDV);
        mapNormal = loader.loadTexture(MAP_NORMAL);
        shader.start();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
        setUpVAO(loader);
    }

    public void render(List<WaterTile> water, Camera camera, Light sun) {
        prepareRender(camera, sun);
        for (WaterTile tile:water) {
            Matrix4f modelMatrix = Maths.createTransformationMatrix(new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0, WaterTile.TILE_SIZE);
            shader.loadModelMatrix(modelMatrix);
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
        }
        unbind();
    }

    private void prepareRender(Camera camera, Light sun) {
        shader.start();
        shader.loadViewMatrix(camera);
        moveFactor += WAVE_SPEED * DisplayManager.getFrameTimeSeconds();
        moveFactor %= 1;
        shader.loadMoveFactor(moveFactor);
        shader.loadLight(sun);
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mapDUDV);
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mapNormal);
        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionDepthTexture());
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void unbind() {
        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    private void setUpVAO(Loader loader) {
        // Just x and z vectex positions here, y is set to 0 in v.shader
        float[] vertices = {-1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1};
        quad = loader.loadToVAO(vertices, 2);
    }

}