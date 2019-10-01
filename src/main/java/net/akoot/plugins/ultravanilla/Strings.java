package net.akoot.plugins.ultravanilla;

import net.akoot.plugins.ultravanilla.reference.Palette;
import net.akoot.plugins.ultravanilla.util.IOUtil;
import org.bukkit.command.Command;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Strings {

    private YamlConfiguration strings;
    private File stringsFile;

    public Strings(JavaPlugin plugin, Class root) {

        // Create a YamlConfiguration instance
        strings = new YamlConfiguration();

        // Create a stringsFile reference
        stringsFile = new File(plugin.getDataFolder(), "strings.yml");

        // Copy defaults from the jar for strings.yml if needed
        IOUtil.copyDefaults(stringsFile, root);

        // Load the configuration from file
        reload();
    }

    /**
     * Reload the strings object with the info written on file
     */
    public void reload() {
        try {
            strings.load(stringsFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
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
        String string = strings.getString(key);
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
        String string = strings.getString(key);
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
        return strings.getString("command." + command.getName() + "." + key);
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
