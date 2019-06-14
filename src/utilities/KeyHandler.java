package utilities;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class KeyHandler {

    private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String getJarLocation() {
        URL jarLocationUrl = KeyHandler.class.getProtectionDomain().getCodeSource().getLocation();
        String jarLocation = new File(jarLocationUrl.toString()).getParent().substring(6);

        return  jarLocation;
    }

    public static void createKeyFile(String location) {
        String key = generateKey(16);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(location + File.separator + "KEY.txt"));
            writer.write(key);

            writer.close();

            System.out.println(key);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public static String generateKey(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
}
