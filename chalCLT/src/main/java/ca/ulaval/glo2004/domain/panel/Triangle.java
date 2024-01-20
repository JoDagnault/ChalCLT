package ca.ulaval.glo2004.domain.panel;

public class Triangle {
    private Vertex v1, v2, v3;
    private Vertex normal;

    public Triangle(Vertex v1, Vertex v2, Vertex v3, Vertex normal) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.normal = normal;
        this.orderVertices();
    }

    private boolean isClockwise() {

        // Calculate vectors from v1 to v2 and v1 to v3
        double[] vector1 = {this.v2.getX() - this.v1.getX(), this.v2.getY() - this.v1.getY(), this.v2.getZ() - this.v1.getZ()};
        double[] vector2 = {this.v3.getX() - this.v1.getX(), this.v3.getY() - this.v1.getY(), this.v3.getZ() - this.v1.getZ()};

        // Calculate the cross product of vector1 and vector2
        double crossProductX = vector1[1] * vector2[2] - vector1[2] * vector2[1];
        double crossProductY = vector1[2] * vector2[0] - vector1[0] * vector2[2];
        double crossProductZ = vector1[0] * vector2[1] - vector1[1] * vector2[0];

        // Check the dot product of the cross product and the normal vector
        double dotProduct = crossProductX * this.normal.getX() + crossProductY * this.normal.getY() + crossProductZ * this.normal.getZ();

        // The vertices are counter-clockwise if the dot product is negative
        return dotProduct < 0;
    }

    private void orderVertices() {
        if (!this.isClockwise()) {
            // Swap v2 and v3 to ensure counter-clockwise order
            Vertex temp = v2;
            v2 = v3;
            v3 = temp;
        }
    }

    public Vertex[] getAllVertices() {
        return new Vertex[]{v1, v2, v3};
    }
    public Vertex getNormal() {
        return normal;
    }
}
