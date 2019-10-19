package net.akoot.plugins.ultravanilla;

import net.akoot.plugins.ultravanilla.reference.Palette;
import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;

public class Strings extends Config {

    public Strings(JavaPlugin plugin, Class root) {
        super(plugin, root, "strings.yml");
    }

    /**
     * Get a formatted string from config.yml in the 'strings' section replacing the placeholder(s) with the value(s)
     * specified.
     *
     * @param key    The name of the key after 'strings.'
     * @param format The placeholder(s) and replacement(s)
     * @return The formatted string
     */
    public String getString(String key, String... format) {
        return getFormattedString(key, format);
    }

    /**
     * Get a non-formatted string from config.yml in the 'strings' section.
     *
     * @param key The name of the key after 'strings.'
     * @return The string
     */
    public String getString(String key) {
        String string = config.getString(key);
        if (string != null) {
            return Palette.translate(string);
        }
        return key;
    }

    /**
     * Get a formatted string from config.yml replacing the placeholder(s) with the value(s) specified.
     *
     * @param key    The name of the key
     * @param format The placeholder(s) and replacement(s)
     * @return The formatted string
     */
    public String getFormattedString(String key, String... format) {
        String string = config.getString(key);
        if (string != null) {
            for (int i = 0; i < format.length; i += 2) {
                string = string.replace(format[i], format[i + 1]);
            }
            return Palette.translate(string);
        }
        return key;
    }

    /**
     * Get a non-formatted string from config.yml in the 'command' section.
     *
     * @param command The command
     * @param key     The name of the key after 'strings.command.{command}'
     * @return The string
     */
    public String getCommandString(Command command, String key) {
        return config.getString("command." + command.getName() + "." + key);
    }

    /**
     * Get a formatted string from config.yml replacing the placeholder(s) with the value(s) specified.
     *
     * @param command The command
     * @param key     The name of the key after 'strings.command.{command}'
     * @param format  The placeholder(s) and replacement(s)
     * @return The formatted String
     */
    public String getFormattedCommandString(Command command, String key, String... format) {
        return getFormattedString("command." + command.getName() + "." + key, format);
    }
}
