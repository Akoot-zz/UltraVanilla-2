package net.akoot.plugins.ultravanilla.util;

import net.akoot.plugins.ultravanilla.UltraVanilla;
import net.md_5.bungee.api.ChatColor;
import org.json.JSONObject;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Colors {

    public static final String MIX_SYMBOL = "+";
    public static final char[] rainbow = {'a', '3', '9', '5', 'd', 'c', '6', 'e'};
    private static String nameMatch;
    private static String classMatch;
    private static String gradientMatch;
    private static JSONObject palette;
    private static JSONObject classes;
    private static JSONObject chat;

    public static void init() {
        JSONObject colors = ((JsonConfig) UltraVanilla.getInstance().getConfig("colors")).getJsonObject();
        palette = colors.getJSONObject("palette");
        classes = colors.getJSONObject("classes");
        chat = colors.getJSONObject("chat");
        nameMatch = String.join("|", palette.keySet());
        classMatch = String.join("|", classes.keySet());
        gradientMatch = "(" + nameMatch + "|#[0-9a-fA-F]{6}|[a-f0-9])";
    }

    public static ChatColor getChatColor(String key) {
        return ChatColor.of(chat.getString(key));
    }

    public static String getHex(String name) {
        return palette.getString(name);
    }

    public static String getClassHex(String className) {
        return classes.getString(className);
    }

    public static ChatColor of(String name) {
        return ChatColor.of(getHex(name));
    }

    public static ChatColor ofClass(String className) {
        return ChatColor.of(getClassHex(className));
    }

    public static String translate(String str) {

        // Rainbow
        if (str.contains("&x")) {
            Pattern p = Pattern.compile("&x([^&$]+)");
            Matcher m = p.matcher(str);
            while (m.find()) {
                str = str.replace(m.group(), rainbowGradient(m.group(1)));
            }
        }

        // Color from RGB
        if (str.contains("&#")) {
            Pattern p = Pattern.compile("&(#[0-9a-fA-F]{6})");
            Matcher m = p.matcher(str);
            while (m.find()) {
                str = str.replace(m.group(), ChatColor.of(m.group(1)) + "");
            }
        }

        // Gradients
        if (str.contains("&>")) {
            Pattern p = Pattern.compile("&>" + gradientMatch + "\\" + MIX_SYMBOL + gradientMatch + "([^&]+|$)");
            Matcher m = p.matcher(str);
            while (m.find()) {
                str = str.replace(m.group(), gradient(m.group(3), m.group(1), m.group(2)));
            }
        }

        // Names
        if (str.contains("&$")) {
            Pattern p = Pattern.compile("&\\$(" + nameMatch + ")");
            Matcher m = p.matcher(str);
            while (m.find()) {
                str = str.replace(m.group(), Colors.of(m.group(1)) + "");
            }
        }

        // Classes
        if (str.contains("&.")) {
            Pattern p = Pattern.compile("&\\.(" + classMatch + ")");
            Matcher m = p.matcher(str);
            while (m.find()) {
                str = str.replace(m.group(), Colors.of(m.group(1)) + "");
            }
        }

        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static Color from(String str) {
        str = str.toLowerCase();
        Color color;
        if (str.startsWith("#")) {
            color = Color.decode(str);
        } else if (str.length() == 1) {
            color = ChatColor.getByChar(str.toCharArray()[0]).getColor();
        } else if (str.startsWith(".")) {
            color = ofClass(str.substring(1)).getColor();
        } else {
            color = of(str).getColor();
        }
        return color;
    }

    public static String gradient(String str, ChatColor color1, ChatColor color2) {
        return gradient(str, color1.getColor(), color2.getColor());
    }

    public static String gradient(String str, String color1, String color2) {
        return gradient(str, from(color1), from(color2));
    }

    public static String rainbow(String string) {
        char[] chars = string.toCharArray();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            str.append("&").append(rainbow[i % rainbow.length]).append(chars[i]);
        }
        return str.toString();
    }

    public static String rainbowGradient(String string) {
        int length = string.length();
        if (length >= 8) {
            int eight = length / 8;
            int quarter = length / 4;
            int half = length / 2;
            String[] sectionStrings = new String[8];
            sectionStrings[0] = "&>a+3" + string.substring(0, eight);
            sectionStrings[1] = "&>3+9" + string.substring(eight, quarter);
            sectionStrings[2] = "&>9+5" + string.substring(quarter, quarter + eight);
            sectionStrings[3] = "&>5+d" + string.substring(quarter + eight, half);
            sectionStrings[4] = "&>d+c" + string.substring(half, half + eight);
            sectionStrings[5] = "&>c+6" + string.substring(half + eight, half + quarter);
            sectionStrings[6] = "&>6+e" + string.substring(half + quarter, half + quarter + eight);
            sectionStrings[7] = "&>e+a" + string.substring(half + quarter + eight, length);
            return String.join("", sectionStrings);
        } else {
            return rainbow(string);
        }
    }

    // implementation by lordpipe
    public static String gradient(String str, Color from, Color to) {
        StringBuilder sb = new StringBuilder();

        ColorSpace cie = ColorSpace.getInstance(ColorSpace.CS_CIEXYZ);
        ColorSpace srgb = ColorSpace.getInstance(ColorSpace.CS_sRGB);

        float[] cieFrom = cie.fromRGB(from.getRGBColorComponents(null));
        float[] cieTo = cie.fromRGB(to.getRGBColorComponents(null));

        for (int i = 0, l = str.length(); i < l; i++) {
            // do interpolation in CIE space
            float[] interpolatedCie = new float[]{
                    cieFrom[0] + (i * (1.0F / l)) * (cieTo[0] - cieFrom[0]),
                    cieFrom[1] + (i * (1.0F / l)) * (cieTo[1] - cieFrom[1]),
                    cieFrom[2] + (i * (1.0F / l)) * (cieTo[2] - cieFrom[2])
            };

            // we could just pass the CIE value directly into `new Color`, but it seems the ChatColor API expects the
            // conversion to sRGB to be pre-computed, so it fails
            float[] interpolatedSrgb = srgb.fromCIEXYZ(interpolatedCie);
            sb.append(ChatColor.of(new Color(interpolatedSrgb[0], interpolatedSrgb[1], interpolatedSrgb[2])));
            sb.append(str.charAt(i));
        }
        return sb.toString();
    }

}
