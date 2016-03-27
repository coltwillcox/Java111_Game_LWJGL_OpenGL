package textures;

/**
 * Created by colt on 3/18/16.
 */

public class ModelTexture {

    private int textureID;
    private int numberOfRows = 1; //Default is one, if image (atlas) contains only one texture.
    private int mapNormal;
    private float shineDamper = 1;
    private float reflectivity = 0;
    private boolean hasTransparency = false; //For rendering back of textures.
    private boolean useFakeLighting = false; //For grass lighting.

    //Constructor.
    public ModelTexture(int ID) {
        this.textureID = ID;
    }

    //Getters and setters.
    public int getID() {
        return this.textureID;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public boolean isHasTransparency() {
        return hasTransparency;
    }

    public void setHasTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
    }

    public boolean isUseFakeLighting() {
        return useFakeLighting;
    }

    public void setUseFakeLighting(boolean useFakeLighting) {
        this.useFakeLighting = useFakeLighting;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public int getMapNormal() {
        return mapNormal;
    }

    public void setMapNormal(int mapNormal) {
        this.mapNormal = mapNormal;
    }

}