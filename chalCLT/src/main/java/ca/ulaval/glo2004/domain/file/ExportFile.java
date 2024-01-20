package ca.ulaval.glo2004.domain.file;

import ca.ulaval.glo2004.domain.cabinComposition.Cabin;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ExportFile {

    public static void serializeCabin(Cabin cabin, String filePath) throws IOException {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(Files.newOutputStream(Paths.get(filePath)))) {
            outputStream.writeObject(cabin);
        }
    }

    public static Cabin deserializeCabin(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(Paths.get(filePath)))) {
            return (Cabin) inputStream.readObject();
        }
    }

}
