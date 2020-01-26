package net.akoot.plugins.ultravanilla.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtil {

    /**
     * If the file doesn't exist, copy the original file in the .jar into the new file.
     *
     * @param file The file to check
     * @param root A class to be used for reference
     */
    public static void copyDefaults(File file, Class root) {
        copyDefaults(file, root, false);
    }

    /**
     * If the file doesn't exist, copy the original file in the .jar into the new file.
     *
     * @param file      The file to check
     * @param root      A class to be used for reference
     * @param overwrite Whether or not to overwrite overwrite
     */
    public static void copyDefaults(File file, Class root, boolean overwrite) {
        boolean exists = file.exists();
        if (overwrite || !exists) {
            if (!exists) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            InputStream fis = root.getResourceAsStream("/" + file.getName());
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
}
