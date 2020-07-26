package net.akoot.plugins.ultravanilla.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    /**
     * Check if a string is alphanumeric
     *
     * @param s The string
     * @return Whether or not a string is alphanumeric
     */
    public static boolean isAlphaNumeric(String s) {
        return s != null && s.matches("^[a-zA-Z0-9_-]*$");
    }

    /**
     * Pick a random value from the list
     *
     * @param strings A list of strings to choose from
     * @return A random value from the list
     */
    public static String pickRandom(List<String> strings) {
        return strings.get((int) (Math.random() * strings.size()));
    }

    public static String replaceIf(String string, String word, boolean replace) {

        String regex = "%\\?" + word + "\\{(.[^}]+)}";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        String replacement = "";
        if (matcher.find() && replace) {
            replacement = matcher.group(1);
        }
        return string.replaceAll(regex, replacement);
    }

    /**
     * Combines arguments separated by a space into one single argument using double quotes or single quotes.
     * Use '/' as a delimiter.
     *
     * @param str The string to convert to arguments
     * @return Arguments with spaces
     */
    public static String[] toArgs(String str) {
        Pattern pattern = Pattern.compile("\"([^\"\\\\]*(\\\\.[^\"\\\\]*)*)\"|'([^'\\\\]*(\\\\.[^'\\\\]*)*)'|[\\S]+");
        Matcher matcher = pattern.matcher(str);
        List<String> refined = new ArrayList<>();
        while (matcher.find()) {
            String group = matcher.group();
            if (group.matches("^\".+\"$")) {
                refined.add(matcher.group(1).replaceAll("\\\\\"", "\""));
            } else if (group.matches("^'.+'$")) {
                refined.add(matcher.group(3));
            } else {
                refined.add(group);
            }
        }
        return refined.toArray(new String[0]);
    }
}
