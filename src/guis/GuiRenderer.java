package guis;

import models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import renderEngine.Loader;
import toolbox.Maths;

import java.util.List;

/**
 * Created by colt on 3/23/16.
 */

public class GuiRenderer {

    private final RawModel quad;
    private GuiShader shader;

    //Constructor.
    public GuiRenderer(Loader loader) {
        float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
        quad = loader.loadToVAO(positions);
        shader = new GuiShader();
    }

    public void render(List<GuiTexture> guis) {
        shader.start();
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL11.glEnable(GL11.GL_BLEND); //Enable alpha blending.
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); //What kind of alpha blending.
        GL11.glDisable(GL11.GL_DEPTH_TEST); //Disable depth testing. Used for overlaping transparent images.
        //Rendering.
        for (GuiTexture gui:guis) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0); //Activate texture unit 0.
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture()); //Bind texture to texture unit 0 (previously activated).
            Matrix4f matrix = Maths.createTransformationMatrix(gui.getPosition(), gui.getScale());
            shader.loadTransformation(matrix);
            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
        }
        GL11.glEnable(GL11.GL_DEPTH_TEST); //Enable depth testing.
        GL11.glDisable(GL11.GL_BLEND); //Disable alpha blending.
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    public void cleanUp() {
        shader.cleanUp();
    }

}