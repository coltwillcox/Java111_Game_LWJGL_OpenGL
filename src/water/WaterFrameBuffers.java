package water;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import java.nio.ByteBuffer;

/**
 * Created by colt on 3/25/16.
 */

public class WaterFrameBuffers {

    protected static final int REFLECTION_WIDTH = 320;
    private static final int REFLECTION_HEIGHT = 180;
    protected static final int REFRACTION_WIDTH = 1280;
    private static final int REFRACTION_HEIGHT = 720;
    private int reflectionFrameBuffer;
    private int reflectionTexture;
    private int reflectionDepthBuffer;
    private int refractionFrameBuffer;
    private int refractionTexture;
    private int refractionDepthTexture;

    //Constructor. Call when loading the game.
    public WaterFrameBuffers() {
        initialiseReflectionFrameBuffer();
        initialiseRefractionFrameBuffer();
    }

    //Call before rendering to this FBO.
    public void bindReflectionFrameBuffer() {
        bindFrameBuffer(reflectionFrameBuffer, REFLECTION_WIDTH, REFLECTION_HEIGHT);
    }

    //Call before rendering to this FBO.
    public void bindRefractionFrameBuffer() {
        bindFrameBuffer(refractionFrameBuffer, REFRACTION_WIDTH, REFRACTION_HEIGHT);
    }

    //Call to switch to default frame buffer.
    public void unbindCurrentFrameBuffer() {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }

    private void initialiseReflectionFrameBuffer() {
        reflectionFrameBuffer = createFrameBuffer();
        reflectionTexture = createTextureAttachment(REFLECTION_WIDTH, REFLECTION_HEIGHT);
        reflectionDepthBuffer = createDepthBufferAttachment(REFLECTION_WIDTH, REFLECTION_HEIGHT);
        unbindCurrentFrameBuffer();
    }

    private void initialiseRefractionFrameBuffer() {
        refractionFrameBuffer = createFrameBuffer();
        refractionTexture = createTextureAttachment(REFRACTION_WIDTH, REFRACTION_HEIGHT);
        refractionDepthTexture = createDepthTextureAttachment(REFRACTION_WIDTH, REFRACTION_HEIGHT);
        unbindCurrentFrameBuffer();
    }

    private void bindFrameBuffer(int frameBuffer, int width, int height){
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0); //To make sure the texture isn't bound.
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
        GL11.glViewport(0, 0, width, height);
    }

    private int createFrameBuffer() {
        int frameBuffer = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer); //Generate name for frame buffer.
        GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0); //Create the framebuffer. Indicate that we will always render to color attachment 0.
        return frameBuffer;
    }

    private int createTextureAttachment(int width, int height) {
        int texture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, texture, 0);
        return texture;
    }

    private int createDepthTextureAttachment(int width, int height) {
        int texture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, texture, 0);
        return texture;
    }

    private int createDepthBufferAttachment(int width, int height) {
        int depthBuffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width, height);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthBuffer);
        return depthBuffer;
    }

    //Call when closing the game.
    public void cleanUp() {
        GL30.glDeleteFramebuffers(reflectionFrameBuffer);
        GL11.glDeleteTextures(reflectionTexture);
        GL30.glDeleteRenderbuffers(reflectionDepthBuffer);
        GL30.glDeleteFramebuffers(refractionFrameBuffer);
        GL11.glDeleteTextures(refractionTexture);
        GL11.glDeleteTextures(refractionDepthTexture);
    }

    //Getters.
    //Get the resulting texture.
    public int getReflectionTexture() {
        return reflectionTexture;
    }

    //Get the resulting texture.
    public int getRefractionTexture() {
        return refractionTexture;
    }

    //Get the resulting depth texture.
    public int getRefractionDepthTexture() {
        return refractionDepthTexture;
    }

}