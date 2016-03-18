package renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Created by colt on 3/17/16.
 */

public class Renderer {

    //Called once every frame.
    public void prepare() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glClearColor(1, 0, 0, 1); //Red, green, blue, alpha.
    }

    public void render(RawModel model) {
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0); //What is it rendering, number of vertices to render, type of data we are giving it, where should it start.
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

}