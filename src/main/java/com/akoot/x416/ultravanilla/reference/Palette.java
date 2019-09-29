package com.akoot.x416.ultravanilla.reference;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Palette {

    public static final char[] RAINBOW_SEQUENCE = {'a', '3', '9', '5', 'd', 'c', '6', 'e'};
    public static final ChatColor NOUN = ChatColor.LIGHT_PURPLE;
    public static final ChatColor VERB = ChatColor.ITALIC;
    public static final ChatColor NUMBER = ChatColor.GOLD;
    public static final ChatColor OBJECT = ChatColor.AQUA;
    public static final ChatColor WRONG = ChatColor.RED;
    public static final ChatColor RIGHT = ChatColor.GREEN;
    public static final ChatColor TRUE = ChatColor.DARK_AQUA;
    public static final ChatColor FALSE = ChatColor.DARK_RED;
    private static Random random = new Random();

    public static String BOOLEAN(boolean bool) {
        return (bool ? Palette.TRUE : Palette.FALSE) + "" + bool;
    }

    /**
     * Convert text with all applicable color codes into color text.
     *
     * @param text The text to colorize
     * @return Colored text
     */
    public static String translate(String text) {

        // Rainbow text
        if (text.contains("&x")) {
            String[] toColor = getRegex("&x[^&]*", text);
            for (String s : toColor) {
                text = text.replace(s, rainbow(s.substring(2)));
            }
        }

        // Random color text
        if (text.contains("&h")) {
            text = text.replace("&h", "" + ChatColor.values()[random.nextInt(ChatColor.values().length)]);
        }

        //TODO: read from config
        text = text
                .replace("&@", NOUN + "")
                .replace("&>", VERB + "")
                .replace("&#", NUMBER + "")
                .replace("&.", OBJECT + "")
                .replace("&wrong", WRONG + "")
                .replace("&right", RIGHT + "")
                .replace("&true", TRUE + "")
                .replace("&false", FALSE + "")
        ;

        // Convert & to
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * Colorize a string into a rainbow.
     *
     * @param msg The message to colorize
     * @return The colorized message
     */
    public static String rainbow(String msg) {
        String rainbow = "";
        int i = random.nextInt(RAINBOW_SEQUENCE.length);
        for (char c : msg.toCharArray()) {
            if (i >= RAINBOW_SEQUENCE.length) {
                i = 0;
            }

            String ch = String.valueOf(c);
            if (c != ' ') {
                ch = "&" + RAINBOW_SEQUENCE[i] + ch;
                i++;
            }
            rainbow += ch;
        }
        return rainbow;
    }

    /**
     * Gets the first object from a mather group for the specified regex
     *
     * @param regex The regular expression
     * @param data  The data to search
     * @return The (first) object that matches that regular expression
     */
    public static String[] getRegex(String regex, String data) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);
        List<String> found = new ArrayList<>();
        while (matcher.find()) {
            found.add(matcher.group());
        }
        return found.toArray(new String[0]);
    }
}
