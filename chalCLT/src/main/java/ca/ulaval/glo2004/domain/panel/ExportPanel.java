package ca.ulaval.glo2004.domain.panel;

import ca.ulaval.glo2004.domain.cabinComposition.*;
import ca.ulaval.glo2004.domain.utils.ImperialMeasure;
import ca.ulaval.glo2004.domain.utils.RoofUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.charset.StandardCharsets;

public class ExportPanel {

    private Cabin cabin;

    // Constructeur de ExportPanel
    public ExportPanel(Cabin p_cabin) {
        cabin = p_cabin;
        createOutputFolder();
    }

    // Créer le fichier de sortie
    public void createOutputFolder() {

        String outputFolderName = "output";

        String[] subfolders = {"Brut", "Fini", "Retrait"};

        String currentDirectory = System.getProperty("user.dir");


        Path outputPath = Paths.get(currentDirectory, outputFolderName);
        System.out.println(outputPath);

        if (!Files.exists(outputPath)) {
            try {
                Files.createDirectories(outputPath);
            } catch (Exception e) {
                System.err.println("Error creating folders: " + e.getMessage());
            }
        }
        for (String subfolder : subfolders) {
            Path subfolderPath = Paths.get(outputPath.toString(), subfolder);
            if(!Files.exists(subfolderPath)){
                try {
                    Files.createDirectories(subfolderPath);
                }
                catch (Exception e){
                    System.err.println("Error creating folders: " + e.getMessage());
                }
            }

        }

    }

    public String nameFiles(String suffix, Wall wall) {
        String lettre = "";
        if (wall.getWallType() == WallType.FRONT) {
            lettre = "F";
        }
        if (wall.getWallType() == WallType.BACK) {
            lettre = "A";
        }
        if (wall.getWallType() == WallType.LEFT) {
            lettre = "G";
        }
        if (wall.getWallType() == WallType.RIGHT) {
            lettre = "D";
        }
        String name = suffix + lettre;
        return name;
    }

    public void exportPanelBrut() {

        String outputFolderName = "output";
        String subfolderName = "Brut";

        String currentDirectory = System.getProperty("user.dir");

        Path outputPath = Paths.get(currentDirectory, outputFolderName);
        Path panneauxBrutPath = Paths.get(outputPath.toString(), subfolderName);
        String suffix = "chaltCLT_Brut_";

        for (Wall wall : cabin.getWallList()){

            String name = nameFiles(suffix,wall);

            Path fileName = Paths.get(panneauxBrutPath.toString(), name + ".stl");
            System.out.println(fileName);
            Rectangle[] rectangles = wallToRectangles(wall);
            Triangle[] triangles = rectanglesToTriangles(rectangles);
            exportToSTL(triangles, fileName);

        }
        exportRoofBrut(panneauxBrutPath.toString(), suffix);
    }

    // Exporter les panneaux finis
    public void exportPanelFinished() {
        String outputFolderName = "output";
        String subfolderName = "Fini";

        String currentDirectory = System.getProperty("user.dir");

        Path outputPath = Paths.get(currentDirectory, outputFolderName);
        Path panneauxFinisPath = Paths.get(outputPath.toString(), subfolderName);
        String suffix = "chaltCLT_Fini_";

        for (Wall wall : cabin.getWallList()) {

            String name = nameFiles(suffix,wall);

            Path fileName = Paths.get(panneauxFinisPath.toString(), name + ".stl");
            System.out.println(fileName);
            Rectangle[] rectangles = calculateFinishedWallRectangles(wall);
            Triangle[] triangles = rectanglesToTriangles(rectangles);
            exportToSTL(triangles, fileName);
        }
        exportRoof(panneauxFinisPath.toString(), suffix);
    }

    // Exporter les rainures
    public void exportPanelGroove() {
        // Specify the output folder name and subfolder name
        String outputFolderName = "output";
        String subfolderName = "Retrait";

        String currentDirectory = System.getProperty("user.dir");

        Path outputPath = Paths.get(currentDirectory, outputFolderName);
        Path panneauxRainurePath = Paths.get(outputPath.toString(), subfolderName);
        String suffix = "chaltCLT_Retrait_Rainure_";

        for (Wall wall : cabin.getWallList()) {

            Path fileName;
            String finame;
            String name = nameFiles(suffix,wall);

            Rectangle[][] rectanglesWithGrooves = wallToRectanglesWithGrooves(wall);

            // Export files for each set of rectangles
            for (int i = 0; i < rectanglesWithGrooves.length; i++) {

                if (i == 0) {
                    finame = name + "_1";
                } else if (i == 1) {
                    finame = name + "_2";
                }
                else{
                    continue;
                }

                fileName = Paths.get(panneauxRainurePath.toString(), finame + ".stl");

                Triangle[] triangles = rectanglesToTriangles(rectanglesWithGrooves[i]);
                exportToSTL(triangles, fileName);
            }
        }
        exportAccessory();
    }

    // Exporter les accessoires
    public void exportAccessory() {
        String outputFolderName = "output";
        String subfolderName = "Retrait";

        String currentDirectory = System.getProperty("user.dir");

        Path outputPath = Paths.get(currentDirectory, outputFolderName);
        Path accessoryPath = Paths.get(outputPath.toString(), subfolderName);

        for (Wall wall : cabin.getWallList()) {

            Path fileName;
            String suffix;
            suffix = "chaltCLT_Retrait_Accessory_";
            String name = nameFiles(suffix,wall);
            String finame = name;

            int j = 1;
            for (Accessory accessory : wall.getAccessoryList()) {

                Rectangle[] rectanglesAccessory = wallToRectanglesAccessory(wall, accessory);

                if (rectanglesAccessory.length > 1) {
                    finame = name + "_" + j;
                    j++;
                }

                fileName = Paths.get(accessoryPath.toString(), finame + ".stl");

                Triangle[] triangles = rectanglesToTriangles(rectanglesAccessory);
                exportToSTL(triangles, fileName);
            }
        }
    }

    public void exportRoof(String outputPath,  String suffix) {
        int panelIndex = 0;
        for (Triangle[] panel : calculateRoofTriangles(cabin.getRoof())) {

            Path fileName;

            if (panelIndex == 0){
                fileName = Paths.get(outputPath, suffix + "PG.stl");
            }
            else if (panelIndex == 1){
                fileName = Paths.get(outputPath, suffix + "PD.stl");
            }
            else if (panelIndex == 2){
                fileName = Paths.get(outputPath, suffix + "T.stl");
            }
            else {
                fileName = Paths.get(outputPath, suffix + "R.stl");
            }
            System.out.println(fileName);
            exportToSTL(panel, fileName);
            panelIndex++;
        }
    }

    public void exportRoofBrut(String outputPath,  String suffix) {
        int panelIndex = 0;
        for (Triangle[] panel : calculateRoofTrianglesBruts(cabin.getRoof())) {

            Path fileName;

            if (panelIndex == 0){
                fileName = Paths.get(outputPath, suffix + "PG.stl");
            }
            else if (panelIndex == 1){
                fileName = Paths.get(outputPath, suffix + "PD.stl");
            }
            else if (panelIndex == 2){
                fileName = Paths.get(outputPath, suffix + "T.stl");
            }
            else {
                fileName = Paths.get(outputPath, suffix + "R.stl");
            }
            System.out.println(fileName);
            exportToSTL(panel, fileName);
            panelIndex++;
        }
    }

    // Exportation en STL
    public void exportToSTL(Triangle[] triangles, Path fileName) {
        try (BufferedWriter bw = Files.newBufferedWriter(fileName, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            // Write the header
            bw.write("solid name\n");

            // Write each triangle
            for (Triangle triangle : triangles) {
                writeTriangle(bw, triangle);
            }

            // Write the footer
            bw.write("endsolid name\n");

        } catch (IOException e) {
            System.err.println("Erreur lors de l'export STL : " + e.getMessage());
        }
    }

    // Écrit les triangles
    private void writeTriangle(BufferedWriter bw, Triangle triangle) throws IOException {

        bw.write("facet normal " + triangle.getNormal().getX() + " " +
                triangle.getNormal().getY() + " " + triangle.getNormal().getZ() + "\n");
        bw.write("  outer loop\n");

        // Write each vertex
        for (Vertex vertex : triangle.getAllVertices()) {
            writeVertex(bw, vertex);
        }

        bw.write("  endloop\n");
        bw.write("endfacet\n");
    }

    // Écrit les vertex
    private void writeVertex(BufferedWriter bw, Vertex vertex) throws IOException {
        bw.write("    vertex " + vertex.getX() + " " + vertex.getY() + " " + vertex.getZ() + "\n");
    }

    // Créer les rectangles des murs bruts
    private Rectangle[] wallToRectangles(Wall wall) {

        float wallLength = wall.getWallDimension().getLengthFloat();
        float wallHeight = wall.getWallDimension().getHeightFloat();
        float wallWidth = wall.getWallDimension().getWidthFloat();

        Vertex topLeftFront, topRightFront, bottomLeftFront, bottomRightFront;
        Vertex topLeftBack, topRightBack, bottomLeftBack, bottomRightBack;

        if (wall.getWallType() == WallType.LEFT) {
            // Assign values for a side wall
            topLeftFront = new Vertex(wallWidth, 0, wallHeight);
            topRightFront = new Vertex(0, 0, wallHeight);
            bottomLeftFront = new Vertex(wallWidth, 0, 0);
            bottomRightFront = new Vertex(0, 0, 0);

            topLeftBack = new Vertex(wallWidth, wallLength, wallHeight);
            topRightBack = new Vertex(0, wallLength, wallHeight);
            bottomLeftBack = new Vertex(wallWidth, wallLength, 0);
            bottomRightBack = new Vertex(0, wallLength, 0);
        } else if (wall.getWallType() == WallType.RIGHT) {
            // Assign values for a front or back wall
            topLeftFront = new Vertex(cabin.getDimensionCabin().getLengthFloat(), 0, wallHeight);
            topRightFront = new Vertex(cabin.getDimensionCabin().getLengthFloat() - wallWidth, 0, wallHeight);
            bottomLeftFront = new Vertex(cabin.getDimensionCabin().getLengthFloat(), 0, 0);
            bottomRightFront = new Vertex(cabin.getDimensionCabin().getLengthFloat() - wallWidth, 0, 0);

            topLeftBack = new Vertex(cabin.getDimensionCabin().getLengthFloat(), wallLength, wallHeight);
            topRightBack = new Vertex(cabin.getDimensionCabin().getLengthFloat() - wallWidth, wallLength, wallHeight);
            bottomLeftBack = new Vertex(cabin.getDimensionCabin().getLengthFloat(), wallLength, 0);
            bottomRightBack = new Vertex(cabin.getDimensionCabin().getLengthFloat() - wallWidth, wallLength, 0);
        } else if (wall.getWallType() == WallType.FRONT) {
            topLeftFront = new Vertex(0, 0, wallHeight);
            topRightFront = new Vertex(wallLength, 0, wallHeight);
            bottomLeftFront = new Vertex(0, 0, 0);
            bottomRightFront = new Vertex(wallLength, 0, 0);

            topLeftBack = new Vertex(0, wallWidth, wallHeight);
            topRightBack = new Vertex(wallLength, wallWidth, wallHeight);
            bottomLeftBack = new Vertex(0, wallWidth, 0);
            bottomRightBack = new Vertex(wallLength, wallWidth, 0);
        } else {
            topLeftFront = new Vertex(0, cabin.getDimensionCabin().getWidthFloat(), wallHeight);
            topRightFront = new Vertex(wallLength, cabin.getDimensionCabin().getWidthFloat(), wallHeight);
            bottomLeftFront = new Vertex(0, cabin.getDimensionCabin().getWidthFloat(), 0);
            bottomRightFront = new Vertex(wallLength, cabin.getDimensionCabin().getWidthFloat(), 0);

            topLeftBack = new Vertex(0, cabin.getDimensionCabin().getWidthFloat() - wallWidth, wallHeight);
            topRightBack = new Vertex(wallLength, cabin.getDimensionCabin().getWidthFloat() - wallWidth, wallHeight);
            bottomLeftBack = new Vertex(0, cabin.getDimensionCabin().getWidthFloat() - wallWidth, 0);
            bottomRightBack = new Vertex(wallLength, cabin.getDimensionCabin().getWidthFloat() - wallWidth, 0);
        }
        // Create Rectangle objects
        Rectangle frontFace = new Rectangle(topLeftFront, topRightFront, bottomLeftFront, bottomRightFront, normalVectors.ZPos.getVertex());
        Rectangle backFace = new Rectangle(topLeftBack, topRightBack, bottomLeftBack, bottomRightBack, normalVectors.ZNeg.getVertex());
        Rectangle topFace = new Rectangle(topLeftBack, topRightBack, topLeftFront, topRightFront, normalVectors.YPos.getVertex());
        Rectangle bottomFace = new Rectangle(bottomLeftFront, bottomRightFront, bottomLeftBack, bottomRightBack, normalVectors.YNeg.getVertex());
        Rectangle leftFace = new Rectangle(topLeftBack, topLeftFront, bottomLeftBack, bottomLeftFront, normalVectors.XNeg.getVertex());
        Rectangle rightFace = new Rectangle(topRightFront, topRightBack, bottomRightFront, bottomRightBack, normalVectors.XPos.getVertex());

        // Return the array of Rectangle objects
        return new Rectangle[]{frontFace, backFace, topFace, bottomFace, leftFace, rightFace};
    }

    // Converti les rectangles en triangles
    private Triangle[] rectangleToTriangles(Rectangle rectangle) {

        Vertex[] vertices = rectangle.getAllVertices();

        Triangle triangle1 = new Triangle(vertices[0], vertices[1], vertices[2], rectangle.getNormal());
        Triangle triangle2 = new Triangle(vertices[0], vertices[2], vertices[3], rectangle.getNormal());

        return new Triangle[]{triangle1, triangle2};
    }

    // Converti les rectangles en triangles
    private Triangle[] rectanglesToTriangles(Rectangle[] rectangleList){

        List<Triangle> triangles = new ArrayList<>();

        for(Rectangle rectangle : rectangleList){
            Triangle[] rectangleTriangles = rectangleToTriangles(rectangle);
            triangles.add(rectangleTriangles[0]);
            triangles.add(rectangleTriangles[1]);
        }
        return triangles.toArray(new Triangle[0]);
    }

    private Rectangle[][] wallToRectanglesWithGrooves(Wall wall) {

        Rectangle[] leftGrooveRectangles, rightGrooveRectangles, wallRectangles;

        if (wall.getWallType() == WallType.LEFT || wall.getWallType() == WallType.RIGHT){

            Rectangle[][] grooveRectangles = calculateSideWallGrooveRectangles(wall);
            leftGrooveRectangles = grooveRectangles[0];
            rightGrooveRectangles = grooveRectangles[1];
            wallRectangles = calculateSideWallRectanglesWithGrooves(wall);
        }
        else {
            leftGrooveRectangles = calculateFacadeGrooveRectangles(wall);
            rightGrooveRectangles = calculateFacadeGrooveRectangles(wall);
            wallRectangles = calculateFacadeWallRectanglesWithGrooves(wall);
        }
        return new Rectangle[][]{leftGrooveRectangles, rightGrooveRectangles, wallRectangles};
    }
    private Rectangle[] calculateFacadeGrooveRectangles(Wall wall){

        float sideLength = wall.getWallDimension().getWidthFloat() / 2 + cabin.getErrorMargin().convertImperialToFloat() / 2;
        float height = wall.getWallDimension().getHeightFloat();

        // Create vertices for the groove
        Vertex bottomLeft = new Vertex(0, 0, 0);
        Vertex bottomRight = new Vertex(sideLength, 0, 0);
        Vertex bottomBack = new Vertex(0, sideLength, 0);
        Vertex bottomFront = new Vertex(sideLength, sideLength, 0);

        Vertex topLeft = new Vertex(0, 0, height);
        Vertex topRight = new Vertex(sideLength, 0, height);
        Vertex topBack = new Vertex(0, sideLength, height);
        Vertex topFront = new Vertex(sideLength, sideLength, height);

        // Create rectangles using the normal vectors from the enum
        Rectangle bottomFace = new Rectangle(bottomLeft, bottomRight, bottomBack, bottomFront, normalVectors.ZNeg.getVertex());
        Rectangle topFace = new Rectangle(topLeft, topRight, topBack, topFront, normalVectors.ZPos.getVertex());

        Rectangle leftFace = new Rectangle(bottomBack, bottomLeft, topBack, topLeft, normalVectors.XNeg.getVertex());
        Rectangle rightFace = new Rectangle(bottomRight, bottomFront, topRight, topFront, normalVectors.XPos.getVertex());

        Rectangle backFace = new Rectangle(bottomBack, bottomFront, topBack, topFront, normalVectors.YPos.getVertex());
        Rectangle frontFace = new Rectangle(bottomLeft, bottomRight, topLeft, topRight, normalVectors.YNeg.getVertex());

        // Return an array of rectangles
        return new Rectangle[]{
                bottomFace, topFace, leftFace, rightFace, frontFace, backFace
        };
    }

    private Rectangle[] calculateFacadeWallRectanglesWithGrooves(Wall wall){

        float wallLength = wall.getWallDimension().getLengthFloat();
        float wallHeight = wall.getWallDimension().getHeightFloat();
        float wallWidth = wall.getWallDimension().getWidthFloat();
        float wallWidthWithGroove = wall.getWallDimension().getWidthFloat() / 2 + cabin.getErrorMargin().convertImperialToFloat() / 2;
        float wallLengthWithoutGroove = wallLength - wallWidthWithGroove;
        float wallWidthWithoutError = wall.getWallDimension().getWidthFloat() / 2 - cabin.getErrorMargin().convertImperialToFloat() / 2;

        Vertex topLeftFront, topRightFront, bottomLeftFront, bottomRightFront;
        Vertex topLeftGrooveLeft, topCornerGrooveLeft,topRightGrooveLeft, bottomLeftGrooveLeft, bottomCornerGrooveLeft, bottomRightGrooveLeft;
        Vertex topLeftGrooveRight, topCornerGrooveRight, topRightGrooveRight, bottomLeftGrooveRight, bottomCornerGrooveRight, bottomRightGrooveRight;
        Vertex topLeftGrooveFace, bottomLeftGrooveFace, topRightGrooveFace, bottomRightGrooveFace;

        Rectangle frontface, backFace, leftSideWallFace, leftInnerGrooveFace, leftSideGrooveFace;
        Rectangle rightSideWallFace, rightInnerGrooveFace, rightSideGrooveFace;
        Rectangle topFaceLeftGroove, topFaceRightGroove, topFace;
        Rectangle bottomFaceLeftGroove, bottomFaceRightGroove, bottomFace;

        topLeftFront = new Vertex(0, 0, wallHeight);
        topRightFront = new Vertex(wallLength, 0, wallHeight);
        bottomLeftFront = new Vertex(0, 0, 0);
        bottomRightFront = new Vertex(wallLength, 0, 0);

        topLeftGrooveLeft = new Vertex(0, wallWidthWithoutError, wallHeight);
        topCornerGrooveLeft = new Vertex(wallWidthWithGroove, wallWidthWithoutError, wallHeight);
        topRightGrooveLeft = new Vertex(wallWidthWithGroove, wallWidth, wallHeight);
        bottomLeftGrooveLeft = new Vertex(0, wallWidthWithoutError, 0);
        bottomCornerGrooveLeft = new Vertex(wallWidthWithGroove, wallWidthWithoutError, 0);
        bottomRightGrooveLeft = new Vertex(wallWidthWithGroove, wallWidth, 0);

        topLeftGrooveRight = new Vertex(wallLengthWithoutGroove, wallWidth, wallHeight);
        topCornerGrooveRight = new Vertex(wallLengthWithoutGroove, wallWidthWithoutError, wallHeight);
        topRightGrooveRight = new Vertex(wallLength, wallWidthWithoutError, wallHeight);
        bottomLeftGrooveRight = new Vertex(wallLengthWithoutGroove, wallWidth, 0);
        bottomCornerGrooveRight = new Vertex(wallLengthWithoutGroove, wallWidthWithoutError, 0);
        bottomRightGrooveRight = new Vertex(wallLength, wallWidthWithoutError, 0);

        topLeftGrooveFace = new Vertex(wallWidthWithGroove, 0, wallHeight);
        bottomLeftGrooveFace = new Vertex(wallWidthWithGroove, 0, 0);
        topRightGrooveFace = new Vertex(wallLengthWithoutGroove, 0, wallHeight);
        bottomRightGrooveFace = new Vertex(wallLengthWithoutGroove, 0, 0);

        frontface = new Rectangle(topLeftFront, topRightFront, bottomLeftFront, bottomRightFront, normalVectors.YNeg.getVertex());
        backFace = new Rectangle(topRightGrooveLeft, bottomRightGrooveLeft, topLeftGrooveRight, bottomLeftGrooveRight, normalVectors.YPos.getVertex());

        leftSideWallFace = new Rectangle(topLeftGrooveLeft, topLeftFront, bottomLeftGrooveLeft, bottomLeftFront, normalVectors.XPos.getVertex());
        leftInnerGrooveFace = new Rectangle(topCornerGrooveLeft,topLeftGrooveLeft,bottomCornerGrooveLeft, bottomLeftGrooveLeft, normalVectors.YPos.getVertex());
        leftSideGrooveFace = new Rectangle(topRightGrooveLeft, topCornerGrooveLeft, bottomRightGrooveLeft, bottomCornerGrooveLeft, normalVectors.XPos.getVertex());

        rightSideWallFace = new Rectangle(topRightFront, topRightGrooveRight, bottomRightFront, bottomRightGrooveRight, normalVectors.XNeg.getVertex());
        rightInnerGrooveFace = new Rectangle(topCornerGrooveRight, topRightGrooveRight, bottomCornerGrooveRight, bottomRightGrooveRight, normalVectors.YPos.getVertex());
        rightSideGrooveFace = new Rectangle(topCornerGrooveRight, topLeftGrooveRight, bottomCornerGrooveRight, bottomLeftGrooveRight, normalVectors.XNeg.getVertex());

        topFaceLeftGroove = new Rectangle(topLeftGrooveLeft, topCornerGrooveLeft, topLeftFront, topLeftGrooveFace, normalVectors.ZPos.getVertex());
        topFaceRightGroove = new Rectangle(topCornerGrooveRight, topRightGrooveFace, topRightGrooveRight, topRightFront, normalVectors.ZPos.getVertex());
        topFace = new Rectangle(topRightGrooveLeft, topLeftGrooveRight, topLeftGrooveFace, topRightGrooveFace,  normalVectors.ZPos.getVertex());

        bottomFaceLeftGroove = new Rectangle(bottomLeftGrooveLeft,bottomCornerGrooveLeft, bottomLeftFront, bottomLeftGrooveFace, normalVectors.ZNeg.getVertex());
        bottomFaceRightGroove = new Rectangle(bottomCornerGrooveRight, bottomRightGrooveFace, bottomRightGrooveRight, bottomRightFront, normalVectors.ZNeg.getVertex());
        bottomFace = new Rectangle(bottomRightGrooveLeft, bottomLeftGrooveRight, bottomLeftGrooveFace, bottomRightGrooveFace, normalVectors.ZNeg.getVertex());

        if(wall.getWallType() == WallType.FRONT){
            topLeftFront.setZ(topLeftFront.getZ() + wallWidthWithGroove);
            topRightFront.setZ(topRightFront.getZ() + wallWidthWithGroove);
            topLeftGrooveLeft.setZ(topLeftGrooveLeft.getZ() + wallWidthWithGroove);
            topCornerGrooveLeft.setZ(topCornerGrooveLeft.getZ() + wallWidthWithGroove);
            topRightGrooveLeft.setZ(topRightGrooveLeft.getZ() + wallWidthWithGroove);
            topLeftGrooveRight.setZ(topLeftGrooveRight.getZ() + wallWidthWithGroove);
            topCornerGrooveRight.setZ(topCornerGrooveRight.getZ() + wallWidthWithGroove);
            topRightGrooveRight.setZ(topRightGrooveRight.getZ() + wallWidthWithGroove);
            topLeftGrooveFace.setZ(topLeftGrooveFace.getZ() + wallWidthWithGroove);
            topRightGrooveFace.setZ(topRightGrooveFace.getZ() + wallWidthWithGroove);
        }

        return new Rectangle[]{frontface, backFace, leftSideWallFace, leftInnerGrooveFace, leftSideGrooveFace, rightSideWallFace, rightInnerGrooveFace, rightSideGrooveFace,
                topFaceLeftGroove, topFaceRightGroove, topFace, bottomFaceLeftGroove, bottomFaceRightGroove, bottomFace};
    }

    private Rectangle[] calculateFacadeWallRectanglesWithGrooves(Wall wall, Rectangle[] frontFaceRectangles, Rectangle[] backFaceRectangles, Rectangle[] innerFaceRectangles){

        float wallLength = wall.getWallDimension().getLengthFloat();
        float wallHeight = wall.getWallDimension().getHeightFloat();
        float wallWidth = wall.getWallDimension().getWidthFloat();
        float wallWidthWithGroove = wall.getWallDimension().getWidthFloat() / 2 + cabin.getErrorMargin().convertImperialToFloat() / 2;
        float wallLengthWithoutGroove = wallLength - wallWidthWithGroove;
        float wallWidthWithoutError = wall.getWallDimension().getWidthFloat() / 2 - cabin.getErrorMargin().convertImperialToFloat() / 2;

        Vertex topLeftFront, topRightFront, bottomLeftFront, bottomRightFront;
        Vertex topLeftGrooveLeft, topCornerGrooveLeft,topRightGrooveLeft, bottomLeftGrooveLeft, bottomCornerGrooveLeft, bottomRightGrooveLeft;
        Vertex topLeftGrooveRight, topCornerGrooveRight, topRightGrooveRight, bottomLeftGrooveRight, bottomCornerGrooveRight, bottomRightGrooveRight;
        Vertex topLeftGrooveFace, bottomLeftGrooveFace, topRightGrooveFace, bottomRightGrooveFace;

        Rectangle leftSideWallFace, leftInnerGrooveFace, leftSideGrooveFace;
        Rectangle rightSideWallFace, rightInnerGrooveFace, rightSideGrooveFace;
        Rectangle topFaceLeftGroove, topFaceRightGroove, topFace;
        Rectangle bottomFaceLeftGroove, bottomFaceRightGroove;

        topLeftFront = new Vertex(0, 0, wallHeight);
        topRightFront = new Vertex(wallLength, 0, wallHeight);
        bottomLeftFront = new Vertex(0, 0, 0);
        bottomRightFront = new Vertex(wallLength, 0, 0);

        topLeftGrooveLeft = new Vertex(0, wallWidthWithoutError, wallHeight);
        topCornerGrooveLeft = new Vertex(wallWidthWithGroove, wallWidthWithoutError, wallHeight);
        topRightGrooveLeft = new Vertex(wallWidthWithGroove, wallWidth, wallHeight);
        bottomLeftGrooveLeft = new Vertex(0, wallWidthWithoutError, 0);
        bottomCornerGrooveLeft = new Vertex(wallWidthWithGroove, wallWidthWithoutError, 0);
        bottomRightGrooveLeft = new Vertex(wallWidthWithGroove, wallWidth, 0);

        topLeftGrooveRight = new Vertex(wallLengthWithoutGroove, wallWidth, wallHeight);
        topCornerGrooveRight = new Vertex(wallLengthWithoutGroove, wallWidthWithoutError, wallHeight);
        topRightGrooveRight = new Vertex(wallLength, wallWidthWithoutError, wallHeight);
        bottomLeftGrooveRight = new Vertex(wallLengthWithoutGroove, wallWidth, 0);
        bottomCornerGrooveRight = new Vertex(wallLengthWithoutGroove, wallWidthWithoutError, 0);
        bottomRightGrooveRight = new Vertex(wallLength, wallWidthWithoutError, 0);

        topLeftGrooveFace = new Vertex(wallWidthWithGroove, 0, wallHeight);
        bottomLeftGrooveFace = new Vertex(wallWidthWithGroove, 0, 0);
        topRightGrooveFace = new Vertex(wallLengthWithoutGroove, 0, wallHeight);
        bottomRightGrooveFace = new Vertex(wallLengthWithoutGroove, 0, 0);

        leftSideWallFace = new Rectangle(topLeftGrooveLeft, topLeftFront, bottomLeftGrooveLeft, bottomLeftFront, normalVectors.XPos.getVertex());
        leftInnerGrooveFace = new Rectangle(topCornerGrooveLeft,topLeftGrooveLeft,bottomCornerGrooveLeft, bottomLeftGrooveLeft, normalVectors.YPos.getVertex());
        leftSideGrooveFace = new Rectangle(topRightGrooveLeft, topCornerGrooveLeft, bottomRightGrooveLeft, bottomCornerGrooveLeft, normalVectors.XPos.getVertex());

        rightSideWallFace = new Rectangle(topRightFront, topRightGrooveRight, bottomRightFront, bottomRightGrooveRight, normalVectors.XNeg.getVertex());
        rightInnerGrooveFace = new Rectangle(topCornerGrooveRight, topRightGrooveRight, bottomCornerGrooveRight, bottomRightGrooveRight, normalVectors.YPos.getVertex());
        rightSideGrooveFace = new Rectangle(topCornerGrooveRight, topLeftGrooveRight, bottomCornerGrooveRight, bottomLeftGrooveRight, normalVectors.XNeg.getVertex());

        topFaceLeftGroove = new Rectangle(topLeftGrooveLeft, topCornerGrooveLeft, topLeftFront, topLeftGrooveFace, normalVectors.ZPos.getVertex());
        topFaceRightGroove = new Rectangle(topCornerGrooveRight, topRightGrooveFace, topRightGrooveRight, topRightFront, normalVectors.ZPos.getVertex());
        topFace = new Rectangle(topRightGrooveLeft, topLeftGrooveRight, topLeftGrooveFace, topRightGrooveFace,  normalVectors.ZPos.getVertex());

        bottomFaceLeftGroove = new Rectangle(bottomLeftGrooveLeft,bottomCornerGrooveLeft, bottomLeftFront, bottomLeftGrooveFace, normalVectors.ZNeg.getVertex());
        bottomFaceRightGroove = new Rectangle(bottomCornerGrooveRight, bottomRightGrooveFace, bottomRightGrooveRight, bottomRightFront, normalVectors.ZNeg.getVertex());

        List<Rectangle> rectangleList = new ArrayList<>(Arrays.asList(
                leftSideWallFace, leftInnerGrooveFace, leftSideGrooveFace,
                rightSideWallFace, rightInnerGrooveFace, rightSideGrooveFace,
                topFaceLeftGroove, topFaceRightGroove, topFace,
                bottomFaceLeftGroove, bottomFaceRightGroove
        ));

        if(wall.getWallType() == WallType.FRONT){

            Vertex oldTopLeftFront = new Vertex(topLeftFront);
            topLeftFront.setZ(topLeftFront.getZ() + wallWidthWithGroove);
            Vertex oldTopRightFront = new Vertex(topRightFront);
            topRightFront.setZ(topRightFront.getZ() + wallWidthWithGroove);

            Rectangle frontFill = new Rectangle(topLeftFront,topRightFront , oldTopLeftFront,   oldTopRightFront , normalVectors.XPos.getVertex());

            Vertex oldTopLeftBack = new Vertex(topRightGrooveLeft);
            Vertex oldTopRightBack = new Vertex(topLeftGrooveRight);

            Rectangle backFill = new Rectangle(topRightGrooveLeft,topLeftGrooveRight , oldTopLeftBack , oldTopRightBack,    normalVectors.XPos.getVertex());

            rectangleList.add(frontFill);
            rectangleList.add(backFill);

            topLeftGrooveLeft.setZ(topLeftGrooveLeft.getZ() + wallWidthWithGroove);
            topCornerGrooveLeft.setZ(topCornerGrooveLeft.getZ() + wallWidthWithGroove);
            topRightGrooveLeft.setZ(topRightGrooveLeft.getZ() + wallWidthWithGroove);
            topLeftGrooveRight.setZ(topLeftGrooveRight.getZ() + wallWidthWithGroove);
            topCornerGrooveRight.setZ(topCornerGrooveRight.getZ() + wallWidthWithGroove);
            topRightGrooveRight.setZ(topRightGrooveRight.getZ() + wallWidthWithGroove);
            topLeftGrooveFace.setZ(topLeftGrooveFace.getZ() + wallWidthWithGroove);
            topRightGrooveFace.setZ(topRightGrooveFace.getZ() + wallWidthWithGroove);
        }

        for (Rectangle rectangle : frontFaceRectangles){
            rectangleList.add(rectangle);
        }
        for (Rectangle rectangle : backFaceRectangles){

            if(rectangle.getTopLeft().getX() == 0){
                rectangle.setTopLeft(new Vertex(wallWidthWithGroove, rectangle.getTopLeft().getY(), rectangle.getTopLeft().getZ()));
                rectangle.setBottomLeft(new Vertex(wallWidthWithGroove, rectangle.getBottomLeft().getY(), rectangle.getBottomLeft().getZ()));
            }
            else if (rectangle.getTopRight().getX() == wallLength){
                rectangle.setTopRight(new Vertex(wallLengthWithoutGroove, rectangle.getTopLeft().getY(), rectangle.getTopLeft().getZ()));
                rectangle.setBottomRight(new Vertex(wallLengthWithoutGroove, rectangle.getBottomLeft().getY(), rectangle.getBottomLeft().getZ()));
            }
            rectangleList.add(rectangle);
        }
        for (Rectangle rectangle : innerFaceRectangles){
            rectangleList.add(rectangle);
        }

        Rectangle bottomFace = new Rectangle(bottomRightGrooveLeft, bottomLeftGrooveRight, bottomLeftGrooveFace, bottomRightGrooveFace, normalVectors.ZNeg.getVertex());
        rectangleList.add(bottomFace);

        return rectangleList.toArray(new Rectangle[0]);
    }


    private Rectangle[][] calculateSideWallGrooveRectangles(Wall wall){

        float wallHeight = wall.getWallDimension().getHeightFloat();
        float wallWidth = wall.getWallDimension().getWidthFloat();
        float wallWidthWithError = wall.getWallDimension().getWidthFloat() / 2 + cabin.getErrorMargin().convertImperialToFloat() / 2;
        float wallWidthWithoutError = wall.getWallDimension().getWidthFloat() / 2 - cabin.getErrorMargin().convertImperialToFloat() / 2;

        Vertex topTopLeft, topTopMid, topTopRight, topBottomRight, topBottomLeft, topCorner, topLeftMid;
        Vertex bottomTopLeft, bottomTopMid, bottomTopRight, bottomBottomRight, bottomBottomLeft, bottomCorner, bottomLeftMid;

        Rectangle topSmall, topBig, bottomSmall, bottomBig;
        Rectangle sideTop, sideRight, sideBottom, sideLeftBottom, sideLeftTop, sideLeftMid;

        topTopLeft = new Vertex(0, 0, wallHeight);
        topTopMid = new Vertex(wallWidthWithoutError,0, wallHeight);
        topTopRight = new Vertex(wallWidth, 0, wallHeight);
        topBottomRight = new Vertex(wallWidth, wallWidth, wallHeight);
        topBottomLeft = new Vertex(wallWidthWithoutError, wallWidth, wallHeight);
        topCorner = new Vertex(wallWidthWithoutError, wallWidthWithError, wallHeight);
        topLeftMid = new Vertex(0, wallWidthWithError, wallHeight);

        bottomTopLeft = new Vertex(0, 0, 0);
        bottomTopMid = new Vertex(wallWidthWithoutError,0, 0);
        bottomTopRight = new Vertex(wallWidth, 0, 0);
        bottomBottomRight = new Vertex(wallWidth, wallWidth, 0);
        bottomBottomLeft = new Vertex(wallWidthWithoutError, wallWidth, 0);
        bottomCorner = new Vertex(wallWidthWithoutError, wallWidthWithError, 0);
        bottomLeftMid = new Vertex(0, wallWidthWithError, 0);

        topSmall = new Rectangle(topTopLeft, topTopMid, topLeftMid, topCorner, normalVectors.ZPos.getVertex());
        topBig = new Rectangle(topTopMid, topTopRight, topBottomLeft, topBottomRight, normalVectors.ZPos.getVertex());
        bottomSmall = new Rectangle(bottomTopLeft, bottomTopMid, bottomLeftMid, bottomCorner, normalVectors.ZNeg.getVertex());
        bottomBig = new Rectangle(bottomTopMid, bottomTopRight, bottomBottomLeft, bottomBottomRight, normalVectors.ZNeg.getVertex());

        sideTop = new Rectangle(topTopLeft, topTopRight, bottomTopLeft, bottomTopRight, normalVectors.YNeg.getVertex());
        sideRight = new Rectangle(topTopRight, topBottomRight,bottomTopRight, bottomBottomRight, normalVectors.XPos.getVertex());
        sideBottom = new Rectangle(topBottomLeft, topBottomRight, bottomBottomLeft, bottomBottomRight, normalVectors.YPos.getVertex());
        sideLeftBottom = new Rectangle(topCorner, topBottomLeft, bottomCorner, bottomBottomLeft, normalVectors.XNeg.getVertex());
        sideLeftMid = new Rectangle(topLeftMid, topCorner, bottomLeftMid, bottomCorner, normalVectors.YPos.getVertex());
        sideLeftTop = new Rectangle(topTopLeft, topLeftMid, bottomTopLeft, bottomLeftMid, normalVectors.XNeg.getVertex());

        return new Rectangle[][]{{topSmall, topBig, bottomSmall, bottomBig, sideTop, sideRight, sideBottom, sideLeftBottom, sideLeftMid, sideLeftTop}, {topSmall, topBig, bottomSmall, bottomBig, sideTop, sideRight, sideBottom, sideLeftBottom, sideLeftMid, sideLeftTop}};
    }
    private Rectangle[] calculateSideWallRectanglesWithGrooves(Wall wall){

        float wallLength = wall.getWallDimension().getLengthFloat();
        float wallHeight = wall.getWallDimension().getHeightFloat();
        float wallWidth = wall.getWallDimension().getWidthFloat();
        float wallLengthWithoutGroove = wallLength - wallWidth /2  - cabin.getErrorMargin().convertImperialToFloat() /2 ;
        float wallLengthWithoutGroove2 = wallLengthWithoutGroove - wallWidth / 2 - cabin.getErrorMargin().convertImperialToFloat() / 2;
        float wallWidthWithoutError = wall.getWallDimension().getWidthFloat() / 2 - cabin.getErrorMargin().convertImperialToFloat() / 2;

        Vertex topLeftFront, topRightFront, bottomLeftFront, bottomRightFront;
        Vertex topLeftGrooveLeft, topCornerGrooveLeft,topRightGrooveLeft, bottomLeftGrooveLeft, bottomCornerGrooveLeft, bottomRightGrooveLeft;
        Vertex topLeftGrooveRight, topCornerGrooveRight, topRightGrooveRight, bottomLeftGrooveRight, bottomCornerGrooveRight, bottomRightGrooveRight;
        Vertex topLeftGrooveFace, bottomLeftGrooveFace, topRightGrooveFace, bottomRightGrooveFace;

        Rectangle frontface, backFace, leftSideWallFace, leftInnerGrooveFace, leftSideGrooveFace;
        Rectangle rightSideWallFace, rightInnerGrooveFace, rightSideGrooveFace;
        Rectangle topFaceLeftGroove, topFaceRightGroove, topFace;
        Rectangle bottomFaceLeftGroove, bottomFaceRightGroove, bottomFace;

        topLeftFront = new Vertex(0, 0, wallHeight);
        topRightFront = new Vertex(wallLengthWithoutGroove, 0, wallHeight);
        bottomLeftFront = new Vertex(0, 0, 0);
        bottomRightFront = new Vertex(wallLengthWithoutGroove, 0, 0);

        topLeftGrooveLeft = new Vertex(0, wallWidthWithoutError, wallHeight);
        topCornerGrooveLeft = new Vertex(wallWidth / 2 + cabin.getErrorMargin().convertImperialToFloat() / 2 , wallWidthWithoutError, wallHeight);
        topRightGrooveLeft = new Vertex(wallWidth / 2 + cabin.getErrorMargin().convertImperialToFloat() / 2 , wallWidth, wallHeight);
        bottomLeftGrooveLeft = new Vertex(0, wallWidthWithoutError, 0);
        bottomCornerGrooveLeft = new Vertex(wallWidth / 2 + cabin.getErrorMargin().convertImperialToFloat() / 2 , wallWidthWithoutError, 0);
        bottomRightGrooveLeft = new Vertex(wallWidth / 2 + cabin.getErrorMargin().convertImperialToFloat() / 2 , wallWidth, 0);

        topLeftGrooveRight = new Vertex(wallLengthWithoutGroove2, wallWidth, wallHeight);
        topCornerGrooveRight = new Vertex(wallLengthWithoutGroove2, wallWidthWithoutError, wallHeight);
        topRightGrooveRight = new Vertex(wallLengthWithoutGroove, wallWidthWithoutError, wallHeight);
        bottomLeftGrooveRight = new Vertex(wallLengthWithoutGroove2, wallWidth, 0);
        bottomCornerGrooveRight = new Vertex(wallLengthWithoutGroove2, wallWidthWithoutError, 0);
        bottomRightGrooveRight = new Vertex(wallLengthWithoutGroove, wallWidthWithoutError, 0);

        topLeftGrooveFace = new Vertex(wallWidth / 2 + cabin.getErrorMargin().convertImperialToFloat() / 2, 0, wallHeight);
        bottomLeftGrooveFace = new Vertex(wallWidth / 2 + cabin.getErrorMargin().convertImperialToFloat() / 2, 0, 0);
        topRightGrooveFace = new Vertex(wallLengthWithoutGroove2, 0, wallHeight);
        bottomRightGrooveFace = new Vertex(wallLengthWithoutGroove2, 0, 0);

        frontface = new Rectangle(topLeftFront, topRightFront, bottomLeftFront, bottomRightFront, normalVectors.YNeg.getVertex());
        backFace = new Rectangle(topRightGrooveLeft, bottomRightGrooveLeft, topLeftGrooveRight, bottomLeftGrooveRight, normalVectors.YPos.getVertex());

        leftSideWallFace = new Rectangle(topLeftGrooveLeft, topLeftFront, bottomLeftGrooveLeft, bottomLeftFront, normalVectors.XPos.getVertex());
        leftInnerGrooveFace = new Rectangle(topCornerGrooveLeft,topLeftGrooveLeft,bottomCornerGrooveLeft, bottomLeftGrooveLeft, normalVectors.YPos.getVertex());
        leftSideGrooveFace = new Rectangle(topRightGrooveLeft, topCornerGrooveLeft, bottomRightGrooveLeft, bottomCornerGrooveLeft, normalVectors.XPos.getVertex());

        rightSideWallFace = new Rectangle(topRightFront, topRightGrooveRight, bottomRightFront, bottomRightGrooveRight, normalVectors.XNeg.getVertex());
        rightInnerGrooveFace = new Rectangle(topCornerGrooveRight, topRightGrooveRight, bottomCornerGrooveRight, bottomRightGrooveRight, normalVectors.YPos.getVertex());
        rightSideGrooveFace = new Rectangle(topCornerGrooveRight, topLeftGrooveRight, bottomCornerGrooveRight, bottomLeftGrooveRight, normalVectors.XNeg.getVertex());

        topFaceLeftGroove = new Rectangle(topLeftGrooveLeft, topCornerGrooveLeft, topLeftFront, topLeftGrooveFace, normalVectors.ZPos.getVertex());
        topFaceRightGroove = new Rectangle(topCornerGrooveRight, topRightGrooveFace, topRightGrooveRight, topRightFront, normalVectors.ZPos.getVertex());
        topFace = new Rectangle(topRightGrooveLeft, topLeftGrooveRight, topLeftGrooveFace, topRightGrooveFace,  normalVectors.ZPos.getVertex());

        bottomFaceLeftGroove = new Rectangle(bottomLeftGrooveLeft,bottomCornerGrooveLeft, bottomLeftFront, bottomLeftGrooveFace, normalVectors.ZNeg.getVertex());
        bottomFaceRightGroove = new Rectangle(bottomCornerGrooveRight, bottomRightGrooveFace, bottomRightGrooveRight, bottomRightFront, normalVectors.ZNeg.getVertex());
        bottomFace = new Rectangle(bottomRightGrooveLeft, bottomLeftGrooveRight, bottomLeftGrooveFace, bottomRightGrooveFace, normalVectors.ZNeg.getVertex());

        return new Rectangle[]{frontface, backFace, leftSideWallFace, leftInnerGrooveFace, leftSideGrooveFace, rightSideWallFace, rightInnerGrooveFace, rightSideGrooveFace,
                topFaceLeftGroove, topFaceRightGroove, topFace, bottomFaceLeftGroove, bottomFaceRightGroove, bottomFace};
    }

    private Rectangle[] calculateSideWallRectanglesWithGrooves(Wall wall, Rectangle[] frontFaceRectangles, Rectangle[] backFaceRectangles, Rectangle[] innerFaceRectangles){

        float wallLength = wall.getWallDimension().getLengthFloat();
        float wallHeight = wall.getWallDimension().getHeightFloat();
        float wallWidth = wall.getWallDimension().getWidthFloat();
        float wallLengthWithoutGroove = wallLength - wallWidth /2  - cabin.getErrorMargin().convertImperialToFloat() /2 ;
        float wallLengthWithoutGroove2 = wallLengthWithoutGroove - wallWidth / 2 + cabin.getErrorMargin().convertImperialToFloat() / 2;
        float wallWidthWithoutError = wall.getWallDimension().getWidthFloat() / 2 - cabin.getErrorMargin().convertImperialToFloat() / 2;

        Vertex topLeftFront, topRightFront, bottomLeftFront, bottomRightFront;
        Vertex topLeftGrooveLeft, topCornerGrooveLeft,topRightGrooveLeft, bottomLeftGrooveLeft, bottomCornerGrooveLeft, bottomRightGrooveLeft;
        Vertex topLeftGrooveRight, topCornerGrooveRight, topRightGrooveRight, bottomLeftGrooveRight, bottomCornerGrooveRight, bottomRightGrooveRight;
        Vertex topLeftGrooveFace, bottomLeftGrooveFace, topRightGrooveFace, bottomRightGrooveFace;

        Rectangle leftSideWallFace, leftInnerGrooveFace, leftSideGrooveFace;
        Rectangle rightSideWallFace, rightInnerGrooveFace, rightSideGrooveFace;
        Rectangle topFaceLeftGroove, topFaceRightGroove, topFace;
        Rectangle bottomFaceLeftGroove, bottomFaceRightGroove;

        topLeftFront = new Vertex(0, 0, wallHeight);
        topRightFront = new Vertex(wallLengthWithoutGroove, 0, wallHeight);
        bottomLeftFront = new Vertex(0, 0, 0);
        bottomRightFront = new Vertex(wallLengthWithoutGroove, 0, 0);

        topLeftGrooveLeft = new Vertex(0, wallWidthWithoutError, wallHeight);
        topCornerGrooveLeft = new Vertex(wallWidthWithoutError, wallWidthWithoutError, wallHeight);
        topRightGrooveLeft = new Vertex(wallWidthWithoutError, wallWidth, wallHeight);
        bottomLeftGrooveLeft = new Vertex(0, wallWidthWithoutError, 0);
        bottomCornerGrooveLeft = new Vertex(wallWidthWithoutError, wallWidthWithoutError, 0);
        bottomRightGrooveLeft = new Vertex(wallWidthWithoutError, wallWidth, 0);

        topLeftGrooveRight = new Vertex(wallLengthWithoutGroove2, wallWidth, wallHeight);
        topCornerGrooveRight = new Vertex(wallLengthWithoutGroove2, wallWidthWithoutError, wallHeight);
        topRightGrooveRight = new Vertex(wallLengthWithoutGroove, wallWidthWithoutError, wallHeight);
        bottomLeftGrooveRight = new Vertex(wallLengthWithoutGroove2, wallWidth, 0);
        bottomCornerGrooveRight = new Vertex(wallLengthWithoutGroove2, wallWidthWithoutError, 0);
        bottomRightGrooveRight = new Vertex(wallLengthWithoutGroove, wallWidthWithoutError, 0);

        topLeftGrooveFace = new Vertex(wallWidthWithoutError, 0, wallHeight);
        bottomLeftGrooveFace = new Vertex(wallWidthWithoutError, 0, 0);
        topRightGrooveFace = new Vertex(wallLengthWithoutGroove2, 0, wallHeight);
        bottomRightGrooveFace = new Vertex(wallLengthWithoutGroove2, 0, 0);

        leftSideWallFace = new Rectangle(topLeftGrooveLeft, topLeftFront, bottomLeftGrooveLeft, bottomLeftFront, normalVectors.XPos.getVertex());
        leftInnerGrooveFace = new Rectangle(topCornerGrooveLeft,topLeftGrooveLeft,bottomCornerGrooveLeft, bottomLeftGrooveLeft, normalVectors.YPos.getVertex());
        leftSideGrooveFace = new Rectangle(topRightGrooveLeft, topCornerGrooveLeft, bottomRightGrooveLeft, bottomCornerGrooveLeft, normalVectors.XPos.getVertex());

        rightSideWallFace = new Rectangle(topRightFront, topRightGrooveRight, bottomRightFront, bottomRightGrooveRight, normalVectors.XNeg.getVertex());
        rightInnerGrooveFace = new Rectangle(topCornerGrooveRight, topRightGrooveRight, bottomCornerGrooveRight, bottomRightGrooveRight, normalVectors.YPos.getVertex());
        rightSideGrooveFace = new Rectangle(topCornerGrooveRight, topLeftGrooveRight, bottomCornerGrooveRight, bottomLeftGrooveRight, normalVectors.XNeg.getVertex());

        topFaceLeftGroove = new Rectangle(topLeftGrooveLeft, topCornerGrooveLeft, topLeftFront, topLeftGrooveFace, normalVectors.ZPos.getVertex());
        topFaceRightGroove = new Rectangle(topCornerGrooveRight, topRightGrooveFace, topRightGrooveRight, topRightFront, normalVectors.ZPos.getVertex());
        topFace = new Rectangle(topRightGrooveLeft, topLeftGrooveRight, topLeftGrooveFace, topRightGrooveFace,  normalVectors.ZPos.getVertex());

        bottomFaceLeftGroove = new Rectangle(bottomLeftGrooveLeft,bottomCornerGrooveLeft, bottomLeftFront, bottomLeftGrooveFace, normalVectors.ZNeg.getVertex());
        bottomFaceRightGroove = new Rectangle(bottomCornerGrooveRight, bottomRightGrooveFace, bottomRightGrooveRight, bottomRightFront, normalVectors.ZNeg.getVertex());

        List<Rectangle> rectangleList = new ArrayList<>(Arrays.asList(
                leftSideWallFace, leftInnerGrooveFace, leftSideGrooveFace,
                rightSideWallFace, rightInnerGrooveFace, rightSideGrooveFace,
                topFaceLeftGroove, topFaceRightGroove, topFace,
                bottomFaceLeftGroove, bottomFaceRightGroove
        ));

        for (Rectangle rectangle : frontFaceRectangles){
            if(rectangle.getTopRight().getX() == wallLength){
                rectangle.setTopRight(new Vertex(wallLengthWithoutGroove, rectangle.getTopRight().getY(), rectangle.getTopRight().getZ()));
                rectangle.setBottomRight(new Vertex(wallLengthWithoutGroove, rectangle.getBottomRight().getY(), rectangle.getBottomRight().getZ()));
            }
            rectangleList.add(rectangle);
        }
        for (Rectangle rectangle : backFaceRectangles){

            if(rectangle.getTopRight().getX() == wallLength){
                rectangle.setTopRight(new Vertex(wallLengthWithoutGroove2, rectangle.getTopRight().getY(), rectangle.getTopRight().getZ()));
                rectangle.setBottomRight(new Vertex(wallLengthWithoutGroove2, rectangle.getBottomRight().getY(), rectangle.getBottomRight().getZ()));
            }
            if(rectangle.getTopLeft().getX() == 0){
                rectangle.setTopLeft(new Vertex(wallWidthWithoutError, rectangle.getTopLeft().getY(), rectangle.getTopLeft().getZ()));
                rectangle.setBottomLeft(new Vertex(wallWidthWithoutError, rectangle.getBottomLeft().getY(), rectangle.getBottomLeft().getZ()));
            }
            rectangleList.add(rectangle);
        }
        for (Rectangle rectangle : innerFaceRectangles){
            rectangleList.add(rectangle);
        }

        Rectangle bottomFace = new Rectangle(bottomRightGrooveLeft, bottomLeftGrooveRight, bottomLeftGrooveFace, bottomRightGrooveFace, normalVectors.ZNeg.getVertex());
        rectangleList.add(bottomFace);

        return rectangleList.toArray(new Rectangle[0]);
    }

    // Créer la liste des rectangles des accessoires
    private Rectangle[] wallToRectanglesAccessory(Wall wall, Accessory accessory) {
        Rectangle frontface, backface, leftface, rightface, bottomface, topface;
        if (wall.getAccessoryList().isEmpty()) {
            return new Rectangle[] {};
        }
        else {
            Vertex accessoryFrontTopLeft = new Vertex(0,0,accessory.getHeight().convertImperialToFloat());
            Vertex accessoryFrontTopRight = new Vertex(accessory.getLength().convertImperialToFloat(),0, accessory.getHeight().convertImperialToFloat());
            Vertex accessoryFrontBottomLeft = new Vertex(0,0,0);
            Vertex accessoryFrontBottomRight = new Vertex(accessory.getLength().convertImperialToFloat(),0,0);

            Vertex accessoryBackTopLeft = new Vertex(0,wall.getWallDimension().getWidthFloat(),accessory.getHeight().convertImperialToFloat());
            Vertex accessoryBackTopRight = new Vertex(accessory.getLength().convertImperialToFloat(),wall.getWallDimension().getWidthFloat(), accessory.getHeight().convertImperialToFloat());
            Vertex accessoryBackBottomLeft = new Vertex(0,wall.getWallDimension().getWidthFloat(),0);
            Vertex accessoryBackBottomRight = new Vertex(accessory.getLength().convertImperialToFloat(),wall.getWallDimension().getWidthFloat(),0);

            frontface = new Rectangle(accessoryFrontTopLeft,accessoryFrontTopRight,accessoryFrontBottomLeft,accessoryFrontBottomRight,normalVectors.YNeg.getVertex());
            backface = new Rectangle(accessoryBackTopLeft,accessoryBackTopRight,accessoryBackBottomLeft,accessoryBackBottomRight,normalVectors.YPos.getVertex());
            leftface = new Rectangle(accessoryBackTopLeft,accessoryFrontTopLeft,accessoryBackBottomLeft,accessoryFrontBottomLeft,normalVectors.XNeg.getVertex());
            rightface = new Rectangle(accessoryFrontTopRight,accessoryBackTopRight,accessoryFrontBottomRight,accessoryBackBottomRight,normalVectors.XPos.getVertex());
            bottomface = new Rectangle(accessoryFrontBottomLeft,accessoryFrontBottomRight,accessoryBackBottomLeft,accessoryBackBottomRight,normalVectors.ZNeg.getVertex());
            topface = new Rectangle(accessoryBackTopLeft,accessoryBackTopRight,accessoryFrontTopLeft,accessoryFrontTopRight,normalVectors.ZPos.getVertex());

            return new Rectangle[] {frontface, backface, leftface, rightface, bottomface, topface};
        }
    }
    private Rectangle[] calculateRelativePosition (Wall wall){
        Rectangle[] wallRectangles = new Rectangle[0];
        float wallLength = wall.getWallDimension().getLengthFloat();
        float wallWidth = wall.getWallDimension().getWidthFloat();
        float cabinWidth = cabin.getDimensionCabin().getWidthFloat();
        float cabinLength = cabin.getDimensionCabin().getLengthFloat();

        switch (wall.getWallType()){
            case FRONT:
                return calculateFacadeWallRectanglesWithGrooves(wall);
            case BACK:
                wallRectangles = calculateFacadeWallRectanglesWithGrooves(wall);
                for (Rectangle rectangle : wallRectangles){
                    rectangle.rotate90DegreesAroundZ();
                    rectangle.rotate90DegreesAroundZ();
                    rectangle.translate(wallLength, cabinWidth + wallWidth / 2  + cabin.getErrorMargin().convertImperialToFloat() / 2, 0);
                }
                break;
            case LEFT:
                wallRectangles = calculateSideWallRectanglesWithGrooves(wall);
                for (Rectangle rectangle : wallRectangles){
                    rectangle.rotate90DegreesAroundZ();
                    rectangle.rotate90DegreesAroundZ();
                    rectangle.rotate90DegreesAroundZ();
                    rectangle.translate(0, wallLength, 0);
                }
                break;
            case RIGHT:
                wallRectangles = calculateSideWallRectanglesWithGrooves(wall);
                for (Rectangle rectangle : wallRectangles){
                    rectangle.rotate90DegreesAroundZ();
                    rectangle.translate(cabinLength, wallWidth / 2 + cabin.getErrorMargin().convertImperialToFloat() / 2, 0);
                }
                break;
        }
        return wallRectangles;
    }

    private Rectangle[] calculateRelativePosition (Wall wall, Rectangle[] frontFaceRectangles, Rectangle[] backFaceRectangles, Rectangle[] innerFaceRectangles){
        Rectangle[] wallRectangles = new Rectangle[0];
        float wallLength = wall.getWallDimension().getLengthFloat();
        float wallWidth = wall.getWallDimension().getWidthFloat();
        float cabinWidth = cabin.getDimensionCabin().getWidthFloat();
        float cabinLength = cabin.getDimensionCabin().getLengthFloat();

        switch (wall.getWallType()){
            case FRONT:
                return calculateFacadeWallRectanglesWithGrooves(wall, frontFaceRectangles, backFaceRectangles, innerFaceRectangles);
            case BACK:
                wallRectangles = calculateFacadeWallRectanglesWithGrooves(wall, frontFaceRectangles, backFaceRectangles, innerFaceRectangles);
                for (Rectangle rectangle : wallRectangles){
                    rectangle.rotate90DegreesAroundZ();
                    rectangle.rotate90DegreesAroundZ();
                    rectangle.translate(wallLength, cabinWidth + wallWidth / 2, 0);
                }
                break;
            case LEFT:
                wallRectangles = calculateSideWallRectanglesWithGrooves(wall, frontFaceRectangles, backFaceRectangles, innerFaceRectangles);
                for (Rectangle rectangle : wallRectangles){
                    rectangle.rotate90DegreesAroundZ();
                    rectangle.rotate90DegreesAroundZ();
                    rectangle.rotate90DegreesAroundZ();
                    rectangle.translate(0, wallLength, 0);
                }
                break;
            case RIGHT:
                wallRectangles = calculateSideWallRectanglesWithGrooves(wall, frontFaceRectangles, backFaceRectangles, innerFaceRectangles);
                for (Rectangle rectangle : wallRectangles){
                    rectangle.rotate90DegreesAroundZ();
                    rectangle.translate(cabinLength, wallWidth / 2, 0);
                }
                break;
        }
        return wallRectangles;
    }
    private Rectangle[] calculateFinishedWallRectangles(Wall wall){
        float wallHeight = wall.getWallDimension().getHeightFloat();
        float wallLength = wall.getWallDimension().getLengthFloat();
        float wallWidth = wall.getWallDimension().getWidthFloat();

        List<Accessory> accessories = wall.getAccessoryList();

        if(accessories.isEmpty()){

            return calculateRelativePosition(wall);
        }


        List<Rectangle> allInnerfaces = new ArrayList<>();
        for(Accessory acc : accessories){
            Rectangle[][] innerfaces = getInnerfaces(acc, wallWidth, wallHeight);
            Rectangle[] innerFacesArray = innerfaces[0];
            Rectangle[] innerFacesToRemove = innerfaces[1];
            allInnerfaces.addAll(Arrays.asList(innerFacesArray));
            allInnerfaces.addAll(Arrays.asList(innerFacesToRemove));
        }


        List<Float> allLines = getAllLines(accessories, wallLength);
        List<Float> allColumns = getAllColumns(accessories,wallHeight);
        List<Rectangle> frontRectangles = getRectangleList(allLines, allColumns, accessories, wallHeight, 0);
        List<Rectangle> backRectangles = getRectangleList(allLines, allColumns, accessories, wallHeight, wallWidth);

        return calculateRelativePosition(wall, frontRectangles.toArray(new Rectangle[0]), backRectangles.toArray(new Rectangle[0]), allInnerfaces.toArray(new Rectangle[0]));
}

    private List<Rectangle> getRectangleList(List<Float> allLines, List<Float> allColumns,  List<Accessory> accessories, float WallHeight, float wallOffset) {
        List<Rectangle> rectangleList = new ArrayList<>();

        float prevLine = 0;
        for (float line : allLines) {
            float prevCol = 0;
            for (float columns : allColumns) {
                if (columns != 0) {

                    boolean overlap = false;
                    for(Accessory accessory: accessories){
                        if(isRectangleContainedOrSame(prevLine, prevCol, line, columns, accessory,WallHeight)){
                            overlap = true;
                            break;
                        }
                    }

                    if (!overlap) {
                        Vertex topLeft = new Vertex(prevLine, wallOffset, columns);
                        Vertex topRight = new Vertex(line, wallOffset, columns);
                        Vertex bottomLeft = new Vertex(prevLine, wallOffset, prevCol);
                        Vertex bottomRight = new Vertex(line, wallOffset, prevCol);
                        rectangleList.add(new Rectangle(topLeft, topRight, bottomLeft, bottomRight, normalVectors.YNeg.getVertex()));
                    }
                }
                prevCol = columns;
            }
            prevLine = line;
        }
        return rectangleList;
    }

    private Rectangle[][] getInnerfaces(Accessory accessory, float wallWidth, float wallHeight){
        List<Rectangle> innerFaces = new ArrayList<>();
        List<Rectangle> facesToRemove = new ArrayList<>();
        float xStart = accessory.getXPosition().convertImperialToFloat();
        float xFinish = xStart +  accessory.getLength().convertImperialToFloat();
        float zFinish = wallHeight - accessory.getYPosition().convertImperialToFloat();
        float zStart =  zFinish - accessory.getHeight().convertImperialToFloat();
        float yFinish = wallWidth;

        Rectangle topInnerFace = new Rectangle(new Vertex(xStart, yFinish, zFinish),new Vertex(xStart, 0, zFinish),new Vertex(xFinish, yFinish, zFinish),new Vertex(xFinish, 0, zFinish), normalVectors.ZNeg.getVertex());
        Rectangle leftInnerFace = new Rectangle(new Vertex(xStart, 0, zFinish),new Vertex(xStart, yFinish, zFinish),new Vertex(xStart, 0, zStart),new Vertex(xStart, yFinish, zStart), normalVectors.YPos.getVertex());
        Rectangle rightInnerFace = new Rectangle(new Vertex(xFinish, 0, zFinish),new Vertex(xFinish, yFinish, zFinish),new Vertex(xFinish, 0, zStart),new Vertex(xFinish, yFinish, zStart), normalVectors.YPos.getVertex());

        Rectangle bottomInnerFace =  new Rectangle(new Vertex(xStart, yFinish, zStart),new Vertex(xStart, 0, zStart),new Vertex(xFinish, yFinish, zStart),new Vertex(xFinish, 0, zStart), normalVectors.ZNeg.getVertex());

        if (accessory.getAccessoryType() == AccessoryType.DOOR){
            facesToRemove.add(bottomInnerFace);
        }
        else{
            innerFaces.add(bottomInnerFace);
        }


        innerFaces.add(leftInnerFace);
        innerFaces.add(topInnerFace);
        innerFaces.add(rightInnerFace);
        return new Rectangle[][]{innerFaces.toArray(new Rectangle[0]), facesToRemove.toArray(new Rectangle[0])};
    }

    private boolean isRectangleContainedOrSame(float x1, float y1, float x2, float y2, Accessory accessory, float WallHeight) {
        float xStart = accessory.getXPosition().convertImperialToFloat();
        float yEnd = WallHeight - accessory.getYPosition().convertImperialToFloat();
        float xEnd = xStart + accessory.getLength().convertImperialToFloat();
        float yStart = yEnd - accessory.getHeight().convertImperialToFloat();
        boolean xInRange = x1 >= xStart && x2 <= xEnd;
        boolean yInRange = y1 >= yStart && y2 <= yEnd;

        return xInRange && yInRange;
    }

    private List<Float> getAllLines(List<Accessory> accessories,float wallLength){
        List<Float> allLines = new ArrayList<>();
        for(Accessory acc : accessories){
            if(!acc.isValid()){
                continue;
            }
            float start = acc.getXPosition().convertImperialToFloat();
            allLines.add(start);
            allLines.add(start + acc.getLength().convertImperialToFloat());
        }
        allLines.add(wallLength);
        Collections.sort(allLines);
        return allLines;
    }

    private List<Float> getAllColumns(List<Accessory> accessories, float wallHeight){
        List<Float> allColumns = new ArrayList<>();
        for(Accessory acc : accessories){
            if(!acc.isValid()){
                continue;
            }
            float start = wallHeight - acc.getYPosition().convertImperialToFloat();
            allColumns.add(start);
            allColumns.add(start - acc.getHeight().convertImperialToFloat());
        }
        allColumns.add(wallHeight);
        Collections.sort(allColumns);
        return allColumns;
    }

    private Triangle[][] calculatePinionTriangles(Pinion pinion, OrientationType orientation) {

        float cabinLength = cabin.getDimensionCabin().getLengthFloat();

        float wallLength;

        if(orientation == OrientationType.FRONT || orientation == OrientationType.BACK){
            wallLength = cabin.getDimensionCabin().getWidthFloat();
        }
        else{
            wallLength = cabin.getDimensionCabin().getLengthFloat();
        }

        float wallHeight = cabin.getDimensionCabin().getHeightFloat();
        float wallWidth = cabin.getThickness().convertImperialToFloat();

        float wallLengthWithoutGroove = wallLength - wallWidth /2  - cabin.getErrorMargin().convertImperialToFloat() /2 ;
        float wallLengthWithoutGroove2 = wallLengthWithoutGroove - wallWidth / 2 - cabin.getErrorMargin().convertImperialToFloat() / 2;
        float wallWidthWithError = wallWidth / 2 + cabin.getErrorMargin().convertImperialToFloat() / 2;

        float roofAngle = pinion.getRoof().getPlankAngle();

        Roof roof = pinion.getRoof();
        VerticalExtension verticalExtension = roof.getVerticalExtension();

        float plankHeight = RoofUtils.heightCalculator(wallWidth / 2, roofAngle);

        Vertex topLeftFrontTriangle, topRightFrontTriangle, bottomLeftFrontTriangle, bottomRightFrontTriangle;
        Vertex topLeftBackTriangle, topRightBackTriangle, bottomLeftBackTriangle, bottomRightBackTriangle;

        bottomLeftFrontTriangle = new Vertex(0, 0, wallHeight);
        bottomRightFrontTriangle = new Vertex(wallLengthWithoutGroove, 0, wallHeight);
        topLeftFrontTriangle = new Vertex(0, 0, wallHeight + verticalExtension.getDimension().getHeightFloat() - plankHeight);
        topRightFrontTriangle = new Vertex(wallLengthWithoutGroove, 0, wallHeight + wallWidthWithError);

        bottomLeftBackTriangle = new Vertex(wallWidthWithError, wallWidth, wallHeight);
        bottomRightBackTriangle = new Vertex(wallLengthWithoutGroove2, wallWidth, wallHeight);
        topLeftBackTriangle = new Vertex(wallWidthWithError, wallWidth, wallHeight + verticalExtension.getDimension().getHeightFloat() - wallWidthWithError - plankHeight);
        topRightBackTriangle = new Vertex(wallLengthWithoutGroove2, wallWidth, wallHeight + wallWidthWithError - plankHeight);

        Rectangle frontFace = new Rectangle(topLeftFrontTriangle, topRightFrontTriangle, bottomLeftFrontTriangle, bottomRightFrontTriangle, normalVectors.XPos.getVertex());
        Rectangle backFace = new Rectangle(topLeftBackTriangle, topRightBackTriangle, bottomLeftBackTriangle, bottomRightBackTriangle, normalVectors.XPos.getVertex());
        Rectangle frontFaceBack = new Rectangle(topLeftFrontTriangle.translate(0, wallWidth / 2,0), topRightFrontTriangle.translate(0, wallWidth / 2,0), bottomLeftFrontTriangle.translate(0, wallWidth / 2,0), bottomRightFrontTriangle.translate(0, wallWidth / 2,0), normalVectors.XPos.getVertex());

        Rectangle topFaceOuter = new Rectangle(topLeftFrontTriangle, topLeftFrontTriangle.translate(0, wallWidth / 2,0), topRightFrontTriangle, topRightFrontTriangle.translate(0, wallWidth / 2,0), normalVectors.XPos.getVertex());
        Rectangle topFaceInner = new Rectangle(topLeftBackTriangle.translate(0, -wallWidth / 2, 0), topLeftBackTriangle, topRightBackTriangle.translate(0, -wallWidth/2, 0), topRightBackTriangle, normalVectors.XPos.getVertex());

        Rectangle bottomFaceOuter = new Rectangle(bottomLeftFrontTriangle, bottomLeftFrontTriangle.translate(0, wallWidth / 2,0), bottomRightFrontTriangle, bottomRightFrontTriangle.translate(0, wallWidth / 2,0), normalVectors.XPos.getVertex());
        Rectangle bottomFaceInner = new Rectangle(bottomLeftBackTriangle.translate(0, -wallWidth / 2, 0), bottomLeftBackTriangle, bottomRightBackTriangle.translate(0, -wallWidth/2, 0), bottomRightBackTriangle, normalVectors.XPos.getVertex());

        Rectangle leftFaceOuter = new Rectangle(topRightFrontTriangle, topRightFrontTriangle.translate(0, wallWidth / 2,0), bottomRightFrontTriangle, bottomRightFrontTriangle.translate(0, wallWidth / 2,0), normalVectors.XPos.getVertex());
        Rectangle leftFaceInner = new Rectangle(topRightBackTriangle.translate(0, -wallWidth / 2, 0), topRightBackTriangle, bottomRightBackTriangle.translate(0, -wallWidth/2, 0), bottomRightBackTriangle, normalVectors.XPos.getVertex());

        Rectangle rightFaceOuter = new Rectangle(topLeftFrontTriangle, topLeftFrontTriangle.translate(0, wallWidth / 2,0), bottomLeftFrontTriangle, bottomLeftFrontTriangle.translate(0, wallWidth / 2,0), normalVectors.XPos.getVertex());
        Rectangle rightFaceInner = new Rectangle(topLeftBackTriangle.translate(0, -wallWidth / 2, 0), topLeftBackTriangle, bottomLeftBackTriangle.translate(0, -wallWidth/2, 0), bottomLeftBackTriangle, normalVectors.XPos.getVertex());

        ArrayList<Rectangle> originalRectangles = new ArrayList<>(Arrays.asList(
                frontFace, backFace, frontFaceBack, topFaceOuter, topFaceInner,
                bottomFaceOuter, bottomFaceInner, leftFaceOuter, leftFaceInner,
                rightFaceOuter, rightFaceInner
        ));

        ArrayList<Rectangle> mirroredRectangles = new ArrayList<>();

        for (Rectangle rectangle : originalRectangles) {
            rectangle.rotate90DegreesAroundZ();
            rectangle.rotate90DegreesAroundZ();
            rectangle.rotate90DegreesAroundZ();
            rectangle.translate(0, wallLength, 0);

            Rectangle newRectangle = new Rectangle(rectangle);
            newRectangle.mirror180DegreesAroundx();
            newRectangle.translate(cabinLength, 0,0);
            mirroredRectangles.add(newRectangle);
        }

        // Convert ArrayLists to arrays
        Triangle[] originalRectanglesArray = rectanglesToTriangles(originalRectangles.toArray(new Rectangle[0]));
        Triangle[] mirroredRectanglesArray = rectanglesToTriangles(mirroredRectangles.toArray(new Rectangle[0]));

        // Return an array of arrays
        return new Triangle[][]{originalRectanglesArray, mirroredRectanglesArray};
    }

    private Triangle[] calculateVerticalExtensionTriangles(VerticalExtension verticalExtension) {

        float cabinLength = cabin.getDimensionCabin().getLengthFloat();

        float wallLength;
        Roof roof = verticalExtension.getRoof();
        OrientationType orientation = verticalExtension.getRoof().getOrientation();

        if(orientation == OrientationType.FRONT || orientation == OrientationType.BACK){
            wallLength = cabin.getDimensionCabin().getWidthFloat();
        }
        else{
            wallLength = cabin.getDimensionCabin().getLengthFloat();
        }

        float wallHeight = cabin.getDimensionCabin().getHeightFloat();
        float wallWidth = cabin.getThickness().convertImperialToFloat();
        float verticalExtensionHeight = verticalExtension.getDimension().getHeightFloat();

        float wallLengthWithoutGroove = wallLength - wallWidth /2  - cabin.getErrorMargin().convertImperialToFloat() /2 ;
        float wallWidthWithError = wallWidth / 2 + cabin.getErrorMargin().convertImperialToFloat() / 2;

        float plankHeight = RoofUtils.heightCalculator(wallWidth / 2, roof.getPlankAngle());

        Vertex topLeftBackFace, topRightBackFace, bottomLeftBackFace, bottomRightBackFace;

        topLeftBackFace = new Vertex(0, 0, wallHeight + verticalExtensionHeight);
        topRightBackFace = new Vertex(cabinLength, 0,wallHeight + verticalExtensionHeight);
        bottomLeftBackFace = new Vertex(0, 0, wallHeight);
        bottomRightBackFace = new Vertex(cabinLength, 0, wallHeight);

        Rectangle backFace = new Rectangle(topLeftBackFace, topRightBackFace, bottomLeftBackFace, bottomRightBackFace, normalVectors.XPos.getVertex());

        Vertex topLeftBackFaceFront, topRightBackFaceFront, bottomLeftBackFaceFront, bottomRightBackFaceFront;

        topLeftBackFaceFront = new Vertex(0, wallWidth / 2, wallHeight + verticalExtensionHeight - plankHeight);
        topRightBackFaceFront = new Vertex(cabinLength, wallWidth / 2, wallHeight + verticalExtensionHeight - plankHeight);
        bottomLeftBackFaceFront = new Vertex(0, wallWidth / 2, wallHeight);
        bottomRightBackFaceFront = new Vertex(cabinLength, wallWidth / 2, wallHeight);

        Rectangle backFaceInner = new Rectangle(topLeftBackFaceFront, topRightBackFaceFront, bottomLeftBackFaceFront, bottomRightBackFaceFront, normalVectors.XPos.getVertex());

        Rectangle backFaceBackBottom = new Rectangle(bottomLeftBackFace, bottomRightBackFace, bottomLeftBackFaceFront, bottomRightBackFaceFront, normalVectors.XPos.getVertex());
        Rectangle backFaceBackLeft = new Rectangle(topRightBackFace, topRightBackFaceFront, bottomRightBackFace, bottomRightBackFaceFront, normalVectors.XPos.getVertex());
        Rectangle backFaceBackRight = new Rectangle(topLeftBackFace, topLeftBackFaceFront, bottomLeftBackFace, bottomLeftBackFaceFront, normalVectors.XPos.getVertex());
        Rectangle backFaceBackTop = new Rectangle(topLeftBackFace, topRightBackFace, topLeftBackFaceFront, topRightBackFaceFront, normalVectors.XPos.getVertex());

        Vertex topLeftFrontFaceBack, topRightFrontFaceBack, bottomLeftFrontFaceBack, bottomRightFrontFaceBack;

        topLeftFrontFaceBack = new Vertex(wallWidthWithError, wallWidth / 2, wallHeight + verticalExtensionHeight - wallWidthWithError);
        topRightFrontFaceBack = new Vertex(cabinLength - wallWidth /2 -cabin.getErrorMargin().convertImperialToFloat()/2, wallWidth / 2,wallHeight + verticalExtensionHeight - wallWidthWithError);
        bottomLeftFrontFaceBack = new Vertex(wallWidthWithError, wallWidth / 2, wallHeight);
        bottomRightFrontFaceBack = new Vertex(cabinLength - wallWidth /2 -cabin.getErrorMargin().convertImperialToFloat()/2, wallWidth / 2, wallHeight);

        Vertex topLeftFrontFace, topRightFrontFace, bottomLeftFrontFace, bottomRightFrontFace;

        topLeftFrontFace = new Vertex(wallWidthWithError, wallWidth, wallHeight + verticalExtensionHeight - wallWidthWithError - plankHeight);
        topRightFrontFace = new Vertex(cabinLength - wallWidth /2 -cabin.getErrorMargin().convertImperialToFloat()/2, wallWidth,wallHeight + verticalExtensionHeight - wallWidthWithError - plankHeight);
        bottomLeftFrontFace = new Vertex(wallWidthWithError, wallWidth, wallHeight);
        bottomRightFrontFace = new Vertex(cabinLength - wallWidth /2 -cabin.getErrorMargin().convertImperialToFloat()/2, wallWidth, wallHeight);

        Rectangle frontFace = new Rectangle(topLeftFrontFace, topRightFrontFace, bottomLeftFrontFace, bottomRightFrontFace, normalVectors.XPos.getVertex());

        Rectangle frontFaceBottom = new Rectangle(bottomRightFrontFaceBack, bottomLeftFrontFaceBack, bottomRightFrontFace,bottomLeftFrontFace, normalVectors.XPos.getVertex());
        Rectangle frontFaceLeft = new Rectangle(topRightFrontFaceBack, topRightFrontFace, bottomRightFrontFaceBack, bottomRightFrontFace, normalVectors.XPos.getVertex());
        Rectangle frontFaceRight = new Rectangle(topLeftFrontFaceBack, topLeftFrontFace, bottomLeftFrontFaceBack, bottomLeftFrontFace, normalVectors.XPos.getVertex());
        Rectangle frontFaceTop = new Rectangle(topRightFrontFaceBack, topLeftFrontFaceBack, topRightFrontFace, topLeftFrontFace, normalVectors.XPos.getVertex());

        Rectangle[] rectangles = new Rectangle[] {backFace, backFaceInner, backFaceBackTop, backFaceBackBottom,
                backFaceBackLeft, backFaceBackRight, frontFace, frontFaceBottom, frontFaceLeft, frontFaceRight, frontFaceTop};

        for (Rectangle rectangle : rectangles){
            rectangle.rotate90DegreesAroundZ();
            rectangle.rotate90DegreesAroundZ();
            rectangle.translate(cabinLength, wallLength + wallWidth / 2 + cabin.getErrorMargin().convertImperialToFloat() / 2,0);
        }

        return rectanglesToTriangles(rectangles);
    }
    private Triangle[] calculateInclinedPlankTriangles(InclinedPlank inclinedPlank) {

        float cabinWidth = cabin.getDimensionCabin().getWidthFloat();
        float cabinLength = cabin.getDimensionCabin().getLengthFloat();

        float wallLength;
        Roof roof = inclinedPlank.getRoof();

        OrientationType orientation = roof.getOrientation();

        if(orientation == OrientationType.FRONT || orientation == OrientationType.BACK){
            wallLength = cabin.getDimensionCabin().getWidthFloat();
        }
        else{
            wallLength = cabin.getDimensionCabin().getLengthFloat();
        }


        float wallHeight = cabin.getDimensionCabin().getHeightFloat();
        float wallWidth = cabin.getThickness().convertImperialToFloat();

        float wallLengthWithoutGroove = wallLength - wallWidth /2  - cabin.getErrorMargin().convertImperialToFloat() /2 ;
        float wallWidthWithError = wallWidth / 2 + cabin.getErrorMargin().convertImperialToFloat() / 2;

        float plankHeight = RoofUtils.heightCalculator(wallWidth / 2, roof.getPlankAngle());
        VerticalExtension verticalExtension = roof.getVerticalExtension();
        float verticalExtensionHeight = verticalExtension.getDimension().getHeightFloat();

        Vertex topLeftFront, topRightFrontFace, bottomLeftFrontFace, bottomRightFrontFace;

        topLeftFront = new Vertex(0, 0, wallHeight + wallWidthWithError + (wallWidth - plankHeight));
        topRightFrontFace = new Vertex(cabinLength, 0,wallHeight + wallWidthWithError + (wallWidth - plankHeight));
        bottomLeftFrontFace = new Vertex(0, 0, wallHeight + wallWidthWithError);
        bottomRightFrontFace = new Vertex(cabinLength, 0, wallHeight + wallWidthWithError);

        Rectangle frontFace = new Rectangle(topLeftFront, topRightFrontFace, bottomLeftFrontFace, bottomRightFrontFace, normalVectors.XPos.getVertex());

        Vertex topLeftBackFace, topRightBackFace, bottomLeftBackFace, bottomRightBackFace;

        topLeftBackFace = new Vertex(0, wallLength + wallWidthWithError,  wallHeight+ (wallWidth - plankHeight) + verticalExtensionHeight);
        topRightBackFace = new Vertex(cabinLength, wallLength + wallWidthWithError,wallHeight+ (wallWidth - plankHeight) + verticalExtensionHeight);
        bottomLeftBackFace = new Vertex(0, wallLength + wallWidthWithError, wallHeight + verticalExtensionHeight);
        bottomRightBackFace = new Vertex(cabinLength,wallLength+ wallWidthWithError, wallHeight +verticalExtensionHeight);

        Rectangle backFace = new Rectangle(topLeftBackFace, topRightBackFace, bottomLeftBackFace, bottomRightBackFace, normalVectors.XPos.getVertex());
        Rectangle topFace = new Rectangle(topLeftBackFace, topRightBackFace, topLeftFront, topRightFrontFace, normalVectors.XPos.getVertex());

        Vertex topLeftGrooveBackFace, topRightGrooveBackFace, bottomLeftGrooveBackFace, bottomRightGrooveBackFace;

        topLeftGrooveBackFace = new Vertex(wallWidthWithError, wallWidth / 2, wallHeight + verticalExtensionHeight - plankHeight).rotate90DegreesAroundZ().rotate90DegreesAroundZ().translate(cabinLength, wallLength + wallWidth / 2 + cabin.getErrorMargin().convertImperialToFloat() / 2,0);
        topRightGrooveBackFace = new Vertex(wallLengthWithoutGroove, wallWidth / 2, wallHeight + verticalExtensionHeight - plankHeight).rotate90DegreesAroundZ().rotate90DegreesAroundZ().translate(wallLength, wallLength + wallWidth / 2 + cabin.getErrorMargin().convertImperialToFloat() / 2,0);
        bottomLeftGrooveBackFace = new Vertex(wallWidthWithError, wallWidth / 2, wallHeight + verticalExtensionHeight - wallWidthWithError).rotate90DegreesAroundZ().rotate90DegreesAroundZ().translate(cabinLength, wallLength + wallWidth / 2 + cabin.getErrorMargin().convertImperialToFloat() / 2,0);
        bottomRightGrooveBackFace = new Vertex(wallLengthWithoutGroove, wallWidth / 2,wallHeight + verticalExtensionHeight - wallWidthWithError).rotate90DegreesAroundZ().rotate90DegreesAroundZ().translate(wallLength, wallLength + wallWidth / 2 + cabin.getErrorMargin().convertImperialToFloat() / 2,0);

        Rectangle backGrooveFace = new Rectangle(topLeftGrooveBackFace, topRightGrooveBackFace, bottomLeftGrooveBackFace, bottomRightGrooveBackFace, normalVectors.XPos.getVertex());

        Vertex bottomLeftBottomFrontFace, bottomRightBottomFrontFace;

        bottomLeftBottomFrontFace = new Vertex(0, wallWidth / 2 , wallHeight + wallWidthWithError);
        bottomRightBottomFrontFace = new Vertex(cabinLength, wallWidth / 2, wallHeight + wallWidthWithError);

        Rectangle bottomFrontFace = new Rectangle(bottomLeftFrontFace, bottomRightFrontFace, bottomLeftBottomFrontFace, bottomRightBottomFrontFace, normalVectors.XPos.getVertex());

        Vertex topLeftBottomFaceFrontGroove, topRightBottomFaceFrontGroove, bottomLeftBottomFrontGroove, bottomRightBottomFrontGroove;

        topLeftBottomFaceFrontGroove = new Vertex(wallWidthWithError, wallWidth / 2, wallHeight + wallWidthWithError);
        topRightBottomFaceFrontGroove = new Vertex(cabinLength - wallWidth /2 -cabin.getErrorMargin().convertImperialToFloat()/2, wallWidth / 2, wallHeight + wallWidthWithError);
        bottomLeftBottomFrontGroove = new Vertex(wallWidthWithError, wallWidth, wallHeight + wallWidthWithError);
        bottomRightBottomFrontGroove = new Vertex(cabinLength - wallWidth /2 -cabin.getErrorMargin().convertImperialToFloat()/2,wallWidth, wallHeight + wallWidthWithError);

        Rectangle bottomGrooveFrontFace = new Rectangle( topLeftBottomFaceFrontGroove,topRightBottomFaceFrontGroove, bottomLeftBottomFrontGroove, bottomRightBottomFrontGroove, normalVectors.XPos.getVertex());

        Rectangle bottomFace = new Rectangle( bottomLeftGrooveBackFace,bottomRightGrooveBackFace, bottomRightBottomFrontGroove,  bottomLeftBottomFrontGroove,normalVectors.XPos.getVertex());

        Vertex topLeftGrooveBottomFace, topRightGrooveBottomFace;

        topLeftGrooveBottomFace = new Vertex(wallWidthWithError, wallLength + wallWidthWithError, wallHeight + verticalExtensionHeight);
        topRightGrooveBottomFace = new Vertex(cabinLength - wallWidth /2 -cabin.getErrorMargin().convertImperialToFloat()/2,wallLength + wallWidthWithError, wallHeight +verticalExtensionHeight);

        Rectangle bottomGrooveBackFace = new Rectangle(topRightGrooveBackFace, topLeftGrooveBackFace, topLeftGrooveBottomFace,  topRightGrooveBottomFace,normalVectors.XPos.getVertex());

        Rectangle middleFace = new Rectangle(bottomLeftBackFace, bottomRightBackFace, bottomLeftBottomFrontFace, bottomRightBottomFrontFace, normalVectors.XPos.getVertex());


        Rectangle leftGrooveFace = new Rectangle(topRightGrooveBackFace, topLeftBottomFaceFrontGroove, bottomRightGrooveBackFace, bottomLeftBottomFrontGroove, normalVectors.XPos.getVertex());
        Rectangle rightGrooveFace = new Rectangle(topLeftGrooveBackFace, topRightBottomFaceFrontGroove, bottomLeftGrooveBackFace, bottomRightBottomFrontGroove, normalVectors.XPos.getVertex());

        Rectangle[] rectangles = new Rectangle[] {frontFace, backFace, topFace,backGrooveFace,bottomFrontFace, bottomGrooveFrontFace,
                bottomFace, bottomGrooveBackFace, middleFace, leftGrooveFace, rightGrooveFace};
        Triangle[] triangles = rectanglesToTriangles(rectangles);

        List<Triangle> triangleList = new ArrayList<>(Arrays.asList(triangles));

        Triangle leftFace1 = new Triangle(topLeftBackFace,bottomLeftBackFace,bottomLeftBottomFrontFace, normalVectors.XPos.getVertex());
        Triangle leftFace2 = new Triangle(topLeftBackFace,bottomLeftFrontFace,bottomLeftBottomFrontFace, normalVectors.XPos.getVertex());
        Triangle leftFace3 = new Triangle(topLeftBackFace,bottomLeftFrontFace,topLeftFront, normalVectors.XPos.getVertex());
        Triangle rightFace1 = new Triangle(topRightBackFace,bottomRightBackFace,bottomRightBottomFrontFace, normalVectors.XPos.getVertex());
        Triangle rightFace2 = new Triangle(topRightBackFace,bottomRightFrontFace,bottomRightBottomFrontFace, normalVectors.XPos.getVertex());
        Triangle rightFace3 = new Triangle(topRightBackFace,bottomRightFrontFace,topRightFrontFace, normalVectors.XPos.getVertex());


        triangleList.add(leftFace1);
        triangleList.add(leftFace2);
        triangleList.add(leftFace3);
        triangleList.add(rightFace1);
        triangleList.add(rightFace2);
        triangleList.add(rightFace3);

        Triangle[] allTriangles = new Triangle[triangleList.size()];
        allTriangles = triangleList.toArray(allTriangles);

        return allTriangles;
    }
    private Triangle[][] calculateRoofTriangles(Roof roof) {

        Triangle[][] pinionTriangles = calculatePinionTriangles(roof.getPinionLeft(), roof.getOrientation());
        Triangle[] pinionLeftTriangles = pinionTriangles[0];
        Triangle[] pinionRightTriangles = pinionTriangles[1];
        Triangle[] verticalExtensionTriangles = calculateVerticalExtensionTriangles(roof.getVerticalExtension());
        Triangle[] inclinedPlankTriangles = calculateInclinedPlankTriangles(roof.getInclinedPlank());

        return new Triangle[][] {
                pinionLeftTriangles,
                pinionRightTriangles,
                inclinedPlankTriangles,
                verticalExtensionTriangles

        };
    }

    private Triangle[][] calculateRoofTrianglesBruts(Roof roof) {

        Triangle[] pinionLeft = calculatePinionLeftTrianglesBruts(roof.getPinionLeft());
        Triangle[] pinionRight = calculatePinionRightTrianglesBruts(roof.getPinionRight());
        Triangle[] verticalExtensionTrianglesBruts = calculateVerticalExtensionTrianglesBrut(roof.getVerticalExtension());
        Triangle[] inclinedPlankTrianglesBruts = calculateInclinedPlankTrianglesBrut(roof.getInclinedPlank());
        return new Triangle[][] {
                pinionLeft,
                pinionRight,
                verticalExtensionTrianglesBruts,
                inclinedPlankTrianglesBruts
        };
    }

    private Triangle[] calculateVerticalExtensionTrianglesBrut(VerticalExtension verticalExtension) {

        float wallLength;
        OrientationType orientation = verticalExtension.getRoof().getOrientation();

        if(orientation == OrientationType.FRONT || orientation == OrientationType.BACK){
            wallLength = cabin.getDimensionCabin().getLengthFloat();
        }
        else{
            wallLength = cabin.getDimensionCabin().getWidthFloat();
        }

        float wallWidth = cabin.getThickness().convertImperialToFloat();
        float verticalExtensionHeight = verticalExtension.getDimension().getHeightFloat();

        float angle = 90 - (180 - ((360 - (2 * (90 - cabin.getRoof().getPlankAngle()))) / 2));

        float haut = RoofUtils.heightCalculator(wallWidth, angle);

        Vertex topLeftBackFace, topRightBackFace, bottomLeftBackFace, bottomRightBackFace;

        topLeftBackFace = new Vertex(0, wallWidth, verticalExtensionHeight + cabin.getDimensionCabin().getHeight().convertImperialToFloat());
        topRightBackFace = new Vertex(wallLength, wallWidth, verticalExtensionHeight + cabin.getDimensionCabin().getHeight().convertImperialToFloat());
        bottomLeftBackFace = new Vertex(0, wallWidth, cabin.getDimensionCabin().getHeight().convertImperialToFloat());
        bottomRightBackFace = new Vertex(wallLength, wallWidth, cabin.getDimensionCabin().getHeight().convertImperialToFloat());

        Vertex topLeftFrontFace, topRightFrontFace, bottomLeftFrontFace, bottomRightFrontFace;

        topLeftFrontFace = new Vertex(0, 0, verticalExtensionHeight - haut + cabin.getDimensionCabin().getHeight().convertImperialToFloat());
        topRightFrontFace = new Vertex(wallLength, 0, verticalExtensionHeight - haut + cabin.getDimensionCabin().getHeight().convertImperialToFloat());
        bottomLeftFrontFace = new Vertex(0, 0, cabin.getDimensionCabin().getHeight().convertImperialToFloat());
        bottomRightFrontFace = new Vertex(wallLength, 0, cabin.getDimensionCabin().getHeight().convertImperialToFloat());

        Rectangle Front = new Rectangle(topLeftFrontFace, topRightFrontFace, bottomLeftFrontFace, bottomRightFrontFace, normalVectors.XPos.getVertex());
        Rectangle Top = new Rectangle(topLeftBackFace, topRightBackFace, topLeftFrontFace,topRightFrontFace, normalVectors.XPos.getVertex());
        Rectangle Left = new Rectangle(topLeftBackFace, topLeftFrontFace, bottomLeftBackFace, bottomLeftFrontFace, normalVectors.XPos.getVertex());
        Rectangle Right = new Rectangle(topRightFrontFace, topRightBackFace, bottomRightFrontFace, bottomRightBackFace, normalVectors.XPos.getVertex());
        Rectangle Back = new Rectangle(topLeftBackFace, topRightBackFace, bottomLeftBackFace, bottomRightBackFace, normalVectors.XPos.getVertex());
        Rectangle Bottom = new Rectangle(bottomLeftBackFace, bottomRightBackFace, bottomLeftFrontFace, bottomRightFrontFace, normalVectors.XPos.getVertex());

        Rectangle[] rectangles = new Rectangle[] {Front, Back, Top, Bottom,
                Left, Right};

        for (Rectangle rectangle : rectangles){

            rectangle.translate(0, cabin.getDimensionCabin().getWidthFloat() - wallWidth,0);
        }

        return rectanglesToTriangles(rectangles);
    }

    private Triangle[] calculateInclinedPlankTrianglesBrut(InclinedPlank inclinedPlank) {

        float wallLength;
        Roof roof = inclinedPlank.getRoof();

        OrientationType orientation = roof.getOrientation();

        if(orientation == OrientationType.FRONT || orientation == OrientationType.BACK){
            wallLength = cabin.getDimensionCabin().getLengthFloat();
        }
        else{
            wallLength = cabin.getDimensionCabin().getWidthFloat();
        }

        float wallWidth = cabin.getThickness().convertImperialToFloat();

        float angle = 90 - (180 - (90 + roof.getPlankAngle()));

        float angleBas = RoofUtils.hypotenuseCalculator(wallWidth, angle);

        float wallsWidth = cabin.getDimensionCabin().getWidthFloat();

        VerticalExtension verticalExtension = roof.getVerticalExtension();
        float verticalExtensionHeight = verticalExtension.getDimension().getHeightFloat();

        Vertex topLeftFront, topRightFrontFace, bottomLeftFrontFace, bottomRightFrontFace;

        topLeftFront = new Vertex(0, 0, angleBas + cabin.getDimensionCabin().getHeight().convertImperialToFloat());
        topRightFrontFace = new Vertex(wallLength, 0, angleBas + cabin.getDimensionCabin().getHeight().convertImperialToFloat());
        bottomLeftFrontFace = new Vertex(0, 0, cabin.getDimensionCabin().getHeight().convertImperialToFloat());
        bottomRightFrontFace = new Vertex(wallLength, 0, cabin.getDimensionCabin().getHeight().convertImperialToFloat());

        Vertex topLeftBackFace, topRightBackFace, bottomLeftBackFace, bottomRightBackFace;

        topLeftBackFace = new Vertex(0, wallsWidth,  verticalExtensionHeight +  angleBas + cabin.getDimensionCabin().getHeight().convertImperialToFloat());
        topRightBackFace = new Vertex(wallLength, wallsWidth, verticalExtensionHeight + angleBas + cabin.getDimensionCabin().getHeight().convertImperialToFloat());
        bottomLeftBackFace = new Vertex(0, wallsWidth, verticalExtensionHeight + cabin.getDimensionCabin().getHeight().convertImperialToFloat());
        bottomRightBackFace = new Vertex(wallLength,wallsWidth, verticalExtensionHeight + cabin.getDimensionCabin().getHeight().convertImperialToFloat());

        Rectangle Front = new Rectangle(topLeftFront, topRightFrontFace, bottomLeftFrontFace, bottomRightFrontFace, normalVectors.XPos.getVertex());
        Rectangle Back = new Rectangle(topLeftBackFace, topRightBackFace, bottomLeftBackFace, bottomRightBackFace, normalVectors.XPos.getVertex());
        Rectangle Top = new Rectangle(topLeftBackFace, topRightBackFace, topLeftFront, topRightFrontFace, normalVectors.XPos.getVertex());
        Rectangle Bottom = new Rectangle(bottomLeftBackFace, bottomRightBackFace, bottomLeftFrontFace, bottomRightFrontFace, normalVectors.XPos.getVertex());
        Rectangle Left = new Rectangle(topLeftBackFace, topLeftFront, bottomLeftBackFace, bottomLeftFrontFace, normalVectors.XPos.getVertex());
        Rectangle Right = new Rectangle(topRightBackFace, topRightFrontFace, bottomRightBackFace, bottomRightFrontFace, normalVectors.XPos.getVertex());

        Rectangle[] rectangles = new Rectangle[] {Front, Back, Top, Bottom, Left, Right};
        Triangle[] triangles = rectanglesToTriangles(rectangles);

        return triangles;
    }

    private Triangle[] calculatePinionLeftTrianglesBruts(Pinion pinion) {

        float wallLength;
        Roof roof = pinion.getRoof();

        OrientationType orientation = roof.getOrientation();

        if(orientation == OrientationType.FRONT || orientation == OrientationType.BACK){
            wallLength = cabin.getDimensionCabin().getLengthFloat();
        }
        else{
            wallLength = cabin.getDimensionCabin().getWidthFloat();
        }

        float wallWidth = cabin.getThickness().convertImperialToFloat();

        float wallsWidth = cabin.getDimensionCabin().getWidthFloat();

        VerticalExtension verticalExtension = roof.getVerticalExtension();
        float verticalExtensionHeight = verticalExtension.getDimension().getHeightFloat();

        Vertex topLeftFront, bottomLeftFrontFace, bottomRightFrontFace;

        topLeftFront = new Vertex(0, wallsWidth, verticalExtensionHeight + cabin.getDimensionCabin().getHeight().convertImperialToFloat());
        bottomLeftFrontFace = new Vertex(0, wallsWidth, cabin.getDimensionCabin().getHeight().convertImperialToFloat());
        bottomRightFrontFace = new Vertex(0, 0, cabin.getDimensionCabin().getHeight().convertImperialToFloat());

        Vertex topLeftBackFace, bottomLeftBackFace, bottomRightBackFace;

        topLeftBackFace = new Vertex(wallWidth, wallsWidth,  verticalExtensionHeight + cabin.getDimensionCabin().getHeight().convertImperialToFloat());
        bottomLeftBackFace = new Vertex(wallWidth, wallsWidth, cabin.getDimensionCabin().getHeight().convertImperialToFloat());
        bottomRightBackFace = new Vertex(wallWidth,0, cabin.getDimensionCabin().getHeight().convertImperialToFloat());

        Triangle Front = new Triangle(topLeftFront, bottomLeftFrontFace, bottomRightFrontFace, normalVectors.XPos.getVertex());
        Triangle Back = new Triangle(topLeftBackFace, bottomLeftBackFace, bottomRightBackFace, normalVectors.XPos.getVertex());
        Rectangle Top = new Rectangle(topLeftFront, topLeftBackFace, bottomRightFrontFace, bottomRightBackFace, normalVectors.XPos.getVertex());
        Rectangle Bottom = new Rectangle(bottomLeftBackFace, bottomLeftFrontFace, bottomRightBackFace, bottomRightFrontFace, normalVectors.XPos.getVertex());
        Rectangle Side = new Rectangle(topLeftBackFace, topLeftFront, bottomLeftBackFace, bottomLeftFrontFace, normalVectors.XPos.getVertex());

        Rectangle[] rectangles = new Rectangle[] {Top, Bottom, Side};

        ArrayList<Triangle> triangles = new ArrayList<>(Arrays.asList(
                Front, Back
        ));

        Triangle[] trianglesRec = rectanglesToTriangles(rectangles);

        for (Triangle triangle : trianglesRec) {
            triangles.add(triangle);
        }

        Triangle[] trianglesList = triangles.toArray(new Triangle[0]);

        return trianglesList;
    }

    private Triangle[] calculatePinionRightTrianglesBruts(Pinion pinion) {

        float wallLength;
        Roof roof = pinion.getRoof();

        OrientationType orientation = roof.getOrientation();

        if(orientation == OrientationType.FRONT || orientation == OrientationType.BACK){
            wallLength = cabin.getDimensionCabin().getLengthFloat();
        }
        else{
            wallLength = cabin.getDimensionCabin().getWidthFloat();
        }

        float wallWidth = cabin.getThickness().convertImperialToFloat();

        float wallsWidth = cabin.getDimensionCabin().getWidthFloat();

        VerticalExtension verticalExtension = roof.getVerticalExtension();
        float verticalExtensionHeight = verticalExtension.getDimension().getHeightFloat();

        Vertex topLeftFront, bottomLeftFrontFace, bottomRightFrontFace;

        topLeftFront = new Vertex(wallLength - wallWidth, wallsWidth, verticalExtensionHeight + cabin.getDimensionCabin().getHeight().convertImperialToFloat());
        bottomLeftFrontFace = new Vertex(wallLength - wallWidth, wallsWidth, cabin.getDimensionCabin().getHeight().convertImperialToFloat());
        bottomRightFrontFace = new Vertex(wallLength - wallWidth, 0, cabin.getDimensionCabin().getHeight().convertImperialToFloat());

        Vertex topLeftBackFace, bottomLeftBackFace, bottomRightBackFace;

        topLeftBackFace = new Vertex(wallLength, wallsWidth,  verticalExtensionHeight + cabin.getDimensionCabin().getHeight().convertImperialToFloat());
        bottomLeftBackFace = new Vertex(wallLength, wallsWidth, cabin.getDimensionCabin().getHeight().convertImperialToFloat());
        bottomRightBackFace = new Vertex(wallLength,0, cabin.getDimensionCabin().getHeight().convertImperialToFloat());

        Triangle Front = new Triangle(topLeftFront, bottomLeftFrontFace, bottomRightFrontFace, normalVectors.XPos.getVertex());
        Triangle Back = new Triangle(topLeftBackFace, bottomLeftBackFace, bottomRightBackFace, normalVectors.XPos.getVertex());
        Rectangle Top = new Rectangle(topLeftFront, topLeftBackFace, bottomRightFrontFace, bottomRightBackFace, normalVectors.XPos.getVertex());
        Rectangle Bottom = new Rectangle(bottomLeftBackFace, bottomLeftFrontFace, bottomRightBackFace, bottomRightFrontFace, normalVectors.XPos.getVertex());
        Rectangle Side = new Rectangle(topLeftBackFace, topLeftFront, bottomLeftBackFace, bottomLeftFrontFace, normalVectors.XPos.getVertex());

        Rectangle[] rectangles = new Rectangle[] {Top, Bottom, Side};

        ArrayList<Triangle> triangles = new ArrayList<>(Arrays.asList(
                Front, Back
        ));

        Triangle[] trianglesRec = rectanglesToTriangles(rectangles);

        for (Triangle triangle : trianglesRec) {
            triangles.add(triangle);
        }

        Triangle[] trianglesList = triangles.toArray(new Triangle[0]);

        return trianglesList;
    }
}
