package org.lwjglb.engine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils {

    private Utils() {
        // Utility class
    }

    public static String readFile(String filePath) {
        String str;
        System.out.println("sd");
        try {
            str = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException excp) {
            throw new RuntimeException("Error reading file [" + filePath + "]", excp);
        }
        return str;
    }
}