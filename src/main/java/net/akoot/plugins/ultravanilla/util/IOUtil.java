package net.akoot.plugins.ultravanilla.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtil {

    /**
     * If the file doesn't exist, copy the original file in the .jar into the new file.
     *
     * @param dataFolder The plugin's data folder
     * @param path       The file to check
     * @param root       A class to be used for reference
     */
    public static void copyDefaults(File dataFolder, String path, Class root) {
        copyDefaults(dataFolder, path, root, false);
    }

    /**
     * If the file doesn't exist, copy the original file in the .jar into the new file.
     *
     * @param dataFolder The plugin's data folder
     * @param path       The file to check
     * @param root       A class to be used for reference
     * @param overwrite  Whether or not to overwrite overwrite
     */
    public static void copyDefaults(File dataFolder, String path, Class root, boolean overwrite) {
        File file = new File(dataFolder, path);
        boolean exists = file.exists();
        if (overwrite || !exists) {
            if (!exists) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            InputStream fis = root.getResourceAsStream("/" + path);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int i;
                while ((i = fis.read(buf)) != -1) {
                    fos.write(buf, 0, i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Recursively deletes a directory even if it has files
     *
     * @param file The directory to delete
     */
    public static void deleteDirectory(File file) {
        if (file.isDirectory()) {
            if (file.length() == 0) {
                file.delete();
            } else {
                deleteDirectory(file);
            }
        } else {
            file.delete();
        }
    }
}
