package renderEngine;

import de.matthiasmann.twl.utils.PNGDecoder;
import models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import textures.TextureData;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
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

    //Overload. Used for GUI.
    public RawModel loadToVAO(float[] positions, int dimensions) {
        int vaoID = createVAO();
        this.storeDataInAttributeList(0, dimensions, positions);
        unbindVAO();
        return new RawModel(vaoID, positions.length / dimensions);
    }

    //Overload. User for normal mapping.
    public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, float[] tangents, int[] indices) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, positions); //Store data (positions) in VAO's attributeList 0, with 3D coords.
        storeDataInAttributeList(1, 2, textureCoords); //2D (texture) coords.
        storeDataInAttributeList(2, 3, normals); //Normal vectors.
        storeDataInAttributeList(3, 3, tangents); //Tangents vectors.
        unbindVAO(); //Must unbind VAO when finished using it.
        return new RawModel(vaoID, indices.length);
    }

    //Overload. User for fonts.
    public int loadToVAO(float[] positions, float[] textureCoords) {
        int vaoID = createVAO();
        storeDataInAttributeList(0, 2, positions); //Store data (positions) in VAO's attributeList 0, with 3D coords.
        storeDataInAttributeList(1, 2, textureCoords); //2D (texture) coords.
        unbindVAO(); //Must unbind VAO when finished using it.
        return vaoID;
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

    public int loadCubeMap(String[] textureFiles) {
        int texID = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
        for (int i = 0; i < textureFiles.length; i++) {
            TextureData data = decodeTextureFile("res/" + textureFiles[i] + ".png");
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
        }
        //Set MAG and MIN filter (make texture smooth).
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        textures.add(texID);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE); //Removes edges of skybox.
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        return texID;
    }

    //Load image into ByteBuffer, and return it as TextureData object.
    private TextureData decodeTextureFile(String fileName) {
        int width = 0;
        int height = 0;
        ByteBuffer buffer = null;
        try {
            FileInputStream in = new FileInputStream(fileName);
            PNGDecoder decoder = new PNGDecoder(in);
            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width * 4, PNGDecoder.Format.RGBA);
            buffer.flip();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Tried to load texture " + fileName + " didn't work!");
            System.exit(-1);
        }
        return new TextureData(buffer, width, height);
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