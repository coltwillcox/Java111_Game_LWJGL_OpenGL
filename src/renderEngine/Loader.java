package renderEngine;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by colt on 3/17/16.
 */

public class Loader {

    private List<Integer> vaos = new ArrayList<>(); //List of VAOs.
    private List<Integer> vbos = new ArrayList<>(); //List of VBOs.

    public RawModel loadToVAO(float[] positions) {
        int vaoID = createVAO();
        storeDataInAttributeList(0, positions); //Store data in attributeList 0.
        unbindVAO(); //Must unbind VAO when finished using it.
        return new RawModel(vaoID, positions.length/3); //Each vertex has 3 floats (x, y, z).
    }

    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays(); //Create empty VAO and return its ID.
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID); //Activate VAO by binding it.
        return vaoID;
    }

    private void storeDataInAttributeList(int attributeNumber, float[] data) {
        int vboID = GL15.glGenBuffers(); //Create empty VBO.
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID); //Bind buffer.
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW); //Store buffer into the VBO.
        GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0, 0); //Put VBO in VAO.
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); //Unbind current VBO.
    }

    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip(); //Prepare buffer to be read from. Flip from writing (default) to reading.
        return buffer;
    }

    public void cleanUp() {
        for (int vao:vaos)
            GL30.glDeleteVertexArrays(vao);
        for (int vbo:vbos)
            GL15.glDeleteBuffers(vbo);
    }

}