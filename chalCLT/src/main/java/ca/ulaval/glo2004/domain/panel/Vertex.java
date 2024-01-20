package ca.ulaval.glo2004.domain.panel;

public class Vertex {
    float x, y, z;
    public Vertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vertex(Vertex other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    public float getX() {
        return x;
    }
    public Vertex setX(float x) {
        this.x = x;
        return this;
    }
    public float getY() {
        return y;
    }
    public Vertex setY(float y) {
        this.y = y;
        return this;
    }
    public float getZ() {
        return z;
    }
    public Vertex setZ(float z) {
        this.z = z;
        return this;
    }
    public Vertex rotate90DegreesAroundZ() {
        return new Vertex(-y, x, z);
    }
    public Vertex translate(float offsetX, float offsetY, float offsetZ) {
        return new Vertex(this.x + offsetX, this.y + offsetY, this.z + offsetZ);
    }
    public Vertex mirror180DegreesAroundx() {
        return new Vertex(-x, y, z);
    }
}
