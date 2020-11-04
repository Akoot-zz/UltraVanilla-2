import net.md_5.bungee.api.ChatColor;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;

public class ColorTests {

    public static String getHex(String name) {
        return getColors().getJSONObject("palette").getString(name);
    }

    public static String getClassHex(String className) {
        return getColors().getJSONObject("classes").getString(className);
    }

    public static ChatColor of(String name) {
        return ChatColor.of(getHex(name));
    }

    public static ChatColor ofClass(String className) {
        return ChatColor.of(getClassHex(className));
    }

    public static JSONObject getColors() {
        try {
            return new JSONObject(new String(Files.readAllBytes(new File("colors.json").toPath())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void jsonClassTest() {
        String expected = "#3772ff";
        assertEquals(expected, getClassHex("noun"));
    }

    @Test
    public void jsonNameTest() {
        String expected = "#fc7e00";
        assertEquals(expected, getHex("pumpkin"));
    }

    @Test
    public void jsonLegacyTest() {
        ChatColor expected = ChatColor.RED;
        assertEquals(expected.getColor(), of("red").getColor());
    }
}
