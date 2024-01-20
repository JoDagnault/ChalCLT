package ca.ulaval.glo2004.domain.panel;

public enum normalVectors {
    XPos(new Vertex(1, 0, 0)),
    XNeg(new Vertex(-1, 0, 0)),
    YPos(new Vertex(0, 1, 0)),
    YNeg(new Vertex(0, -1, 0)),
    ZPos(new Vertex(0, 0, 1)),
    ZNeg(new Vertex(0, 0, 1));

    private final Vertex vertex;
    public Vertex getVertex() {
        return vertex;
    }
    normalVectors(Vertex vertex) {
        this.vertex = vertex;
    }
}
