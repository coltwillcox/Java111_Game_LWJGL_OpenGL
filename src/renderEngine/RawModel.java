package renderEngine;

/**
 * Created by colt on 3/17/16.
 */

public class RawModel {

    private int vaoID;
    private int vertexCount;

    //Constructor.
    public RawModel(int vaoID, int vertexCount) {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }

}