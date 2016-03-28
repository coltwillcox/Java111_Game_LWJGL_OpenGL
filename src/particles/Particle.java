package particles;

import entities.Camera;
import entities.Player;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;

public class Particle {

    private float gravityEffect;
    private float lifeLength;
    private float rotation;
    private float scale;
    private float blend;
    private float distance;
    private float elapsedTime = 0;
    private Vector3f position;
    private Vector3f velocity;
    private Vector2f texOffset1 = new Vector2f();
    private Vector2f texOffset2 = new Vector2f();
    private ParticleTexture texture;
    private Vector3f reusableChange = new Vector3f();

    //Constructor.
    public Particle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation, float scale) {
        this.texture = texture;
        this.position = position;
        this.velocity = velocity;
        this.gravityEffect = gravityEffect;
        this.lifeLength = lifeLength;
        this.rotation = rotation;
        this.scale = scale;
        ParticleMaster.addParticle(this);
    }

    protected boolean update(Camera camera) {
        velocity.setY(velocity.getY() + Player.GRAVITY * gravityEffect * DisplayManager.getFrameTimeSeconds());
        reusableChange.set(velocity);
        reusableChange.scale(DisplayManager.getFrameTimeSeconds());
        Vector3f.add(reusableChange, position, position);
        distance = Vector3f.sub(camera.getPosition(), position, null).lengthSquared();
        updateTextureCoordInfo();
        elapsedTime += DisplayManager.getFrameTimeSeconds();
        return (elapsedTime < lifeLength);
    }

    private void updateTextureCoordInfo() {
        float lifeFactor = elapsedTime / lifeLength;
        int stageCount = texture.getNumberOfRows() * texture.getNumberOfRows();
        float atlasProgression = lifeFactor * stageCount;
        int index1 = (int) Math.floor(atlasProgression);
        int index2 = index1 < stageCount - 1 ? index1 + 1 : index1; //Check if there is next index (next image in atlas image).
        this.blend = atlasProgression % 1;
        setTextureOffset(texOffset1, index1);
        setTextureOffset(texOffset2, index2);
    }

    private void setTextureOffset (Vector2f offset, int index) {
        int column = index % texture.getNumberOfRows();
        int row = index / texture.getNumberOfRows();
        offset.setX((float) column / texture.getNumberOfRows());
        offset.setY((float) row / texture.getNumberOfRows());
    }

    //Getters.
    public ParticleTexture getTexture() {
        return texture;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    public float getBlend() {
        return blend;
    }

    public Vector2f getTexOffset1() {
        return texOffset1;
    }

    public Vector2f getTexOffset2() {
        return texOffset2;
    }

    public float getDistance() {
        return distance;
    }

}