package particles;

/**
 * Created by colt on 3/28/16.
 */

public class ParticleTexture {

    private int textureID;
    private int numberOfRows;
    private boolean additive;

    //Constructor.
    public ParticleTexture(int textureID, int numberOfRows, boolean additive) {
        this.textureID = textureID;
        this.numberOfRows = numberOfRows;
        this.additive = additive;
    }

    //Getters.
    public int getTextureID() {
        return textureID;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public boolean isAdditive() {
        return additive;
    }
}