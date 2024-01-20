package ca.ulaval.glo2004.domain.panel;

class Rectangle {
    private Vertex topLeft, topRight, bottomLeft, bottomRight;

    private Vertex normal;

    public Rectangle(Vertex topLeft, Vertex topRight, Vertex bottomLeft, Vertex bottomRight, Vertex normal) {
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
        this.normal = normal;
    }
    public Rectangle(Rectangle other) {
        this.topLeft = new Vertex(other.topLeft);
        this.topRight = new Vertex(other.topRight);
        this.bottomLeft = new Vertex(other.bottomLeft);
        this.bottomRight = new Vertex(other.bottomRight);
        this.normal = new Vertex(other.normal);
    }

    public Vertex getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(Vertex topLeft) {
        this.topLeft = topLeft;
    }

    public Vertex getTopRight() {
        return topRight;
    }

    public void setTopRight(Vertex topRight) {
        this.topRight = topRight;
    }

    public Vertex getBottomLeft() {
        return bottomLeft;
    }

    public void setBottomLeft(Vertex bottomLeft) {
        this.bottomLeft = bottomLeft;
    }

    public Vertex getBottomRight() {
        return bottomRight;
    }

    public void setBottomRight(Vertex bottomRight) {
        this.bottomRight = bottomRight;
    }
    public Vertex getNormal() {
        return normal;
    }

    // Method to retrieve all vertices
    public Vertex[] getAllVertices() {
        return new Vertex[]{topLeft, topRight, bottomRight, bottomLeft};
    }

    public void rotate90DegreesAroundZ() {
        this.topLeft = topLeft.rotate90DegreesAroundZ();
        this.topRight = topRight.rotate90DegreesAroundZ();
        this.bottomLeft = bottomLeft.rotate90DegreesAroundZ();
        this.bottomRight = bottomRight.rotate90DegreesAroundZ();
    }

    public void translate(float offsetX, float offsetY, float offsetZ) {
        this.topLeft = topLeft.translate(offsetX, offsetY, offsetZ);
        this.topRight = topRight.translate(offsetX, offsetY, offsetZ);
        this.bottomLeft = bottomLeft.translate(offsetX, offsetY, offsetZ);
        this.bottomRight = bottomRight.translate(offsetX, offsetY, offsetZ);
    }

    public void mirror180DegreesAroundx() {
        this.topLeft = topLeft.mirror180DegreesAroundx();
        this.topRight = topRight.mirror180DegreesAroundx();
        this.bottomLeft= bottomLeft.mirror180DegreesAroundx();
        this.bottomRight = bottomRight.mirror180DegreesAroundx();
    }
    public void addExtraHeight(float extraHeight) {
        this.topLeft.setZ(this.topLeft.z + extraHeight);
        this.topRight.setZ(this.topLeft.z + extraHeight);
        this.bottomLeft.setZ(this.topLeft.z + extraHeight);
        this.bottomRight.setZ(this.topLeft.z + extraHeight);
    }
}
