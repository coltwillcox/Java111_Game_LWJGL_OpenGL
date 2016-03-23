package renderEngine;

import models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by colt on 3/17/16.
 */

public class Loader {

    private List<Integer> vaos = new ArrayList<>(); //List of VAOs (Vertex Array Object).
    private List<Integer> vbos = new ArrayList<>(); //List of VBOs (Vertex Buffer Object).
    private List<Integer> textures = new ArrayList<>(); //List of textures. Used later for cleaning them all.

    public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, positions); //Store data (positions) in VAO's attributeList 0, with 3D coords.
        storeDataInAttributeList(1, 2, textureCoords); //2D (texture) coords.
        storeDataInAttributeList(2, 3, normals); //Normal vectors.
        unbindVAO(); //Must unbind VAO when finished using it.
        return new RawModel(vaoID, indices.length);
    }

    //Method overload. Used for GUI.
    public RawModel loadToVAO(float[] positions) {
        int vaoID = createVAO();
        this.storeDataInAttributeList(0, 2, positions);
        unbindVAO();
        return new RawModel(vaoID, positions.length / 2);
    }

    public int loadTexture(String fileName) {
        Texture texture = null;
        try {
            texture = TextureLoader.getTexture("PNG", new FileInputStream("res/"+fileName+".png"));
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D); //Mipmapping (using the type of the texture). Further textures are in lower resolution.
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR); //(Texture type, behavior on smaller dimensions, what to do).
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -1); //Distance of lower resolution textures.
        } catch (IOException e) {
            e.printStackTrace();
        }
        int textureID = texture.getTextureID();
        textures.add(textureID);
        return textureID;
    }

    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays(); //Create empty VAO and return its ID.
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID); //Activate VAO by binding it.
        return vaoID;
    }

    private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
        int vboID = GL15.glGenBuffers(); //Create empty VBO and return its ID.
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID); //Bind buffer.
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW); //Store buffer into the VBO.
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0); //Put VBO in VAO.
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); //Unbind current VBO.
    }

    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    private void bindIndicesBuffer(int[] indices) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW); //Store into the VBO. (Type of VBO, data, how data is gona be used)
    }

    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length); //Create IntBuffer with size of the data.
        buffer.put(data);
        buffer.flip(); //Prepare buffer to be read from. Flip from writing (default) to reading.
        return buffer;
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public void cleanUp() {
        for (int vao:vaos)
            GL30.glDeleteVertexArrays(vao);
        for (int vbo:vbos)
            GL15.glDeleteBuffers(vbo);
        for (int tex:textures)
            GL11.glDeleteTextures(tex);
    }

}