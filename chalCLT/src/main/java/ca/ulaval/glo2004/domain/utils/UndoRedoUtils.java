package ca.ulaval.glo2004.domain.utils;

import ca.ulaval.glo2004.domain.cabinComposition.Cabin;

import java.io.*;

public class UndoRedoUtils {
    public static byte[] serializeToByte(Cabin cabin) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(cabin);
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Cabin deserializeFromByte(byte[] bytes) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream in = new ObjectInputStream(bis)) {
            return (Cabin) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
