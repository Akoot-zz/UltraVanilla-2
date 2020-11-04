package net.akoot.plugins.ultravanilla.util;

import net.akoot.plugins.ultravanilla.reference.Palette;
import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Strings {

    private static final String COMMANDS_PATH = "strings/commands";
    private final List<Config> commandConfigs;
    private final Config messages;
    private final Config variables;

    public Strings(JavaPlugin plugin, Class root) {
        File commandsFolder = new File(plugin.getDataFolder(), COMMANDS_PATH);
        commandsFolder.mkdirs();
        messages = new Config(plugin, root, "strings/messages.yml", "messages");
        variables = new Config(plugin, root, "strings/variables.yml", "variables");
        commandConfigs = new ArrayList<>();
        for (String name : plugin.getDescription().getCommands().keySet()) {
            commandConfigs.add(new Config(plugin, root, COMMANDS_PATH + "/" + name + ".yml", name));
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
    public String getFormattedMessage(String key, String... format) {
        return getFormattedString(messages, key, format);
    }

    /**
     * Get a non-formatted string from strings/variables.yml.
     *
     * @param key The key
     * @return The string
     */
    public String getVariable(String key) {
        String string = variables.getConfig().getString(key);
        if (string != null) {
            return Palette.translate(string);
        }
        return key;
    }

    /**
     * Get a non-formatted string from strings/messages.yml.
     *
     * @param key The name of the key after 'strings.'
     * @return The string
     */
    public String getMessage(String key) {
        String string = messages.getConfig().getString(key);
        if (string != null) {
            return Palette.translate(string);
        }
        return key;
    }

    /**
     * Get a formatted string from config.yml replacing the placeholder(s) with the value(s) specified.
     *
     * @param config The config
     * @param key    The name of the key
     * @param format The placeholder(s) and replacement(s)
     * @return The formatted string
     */
    public String getFormattedString(Config config, String key, String... format) {
        Object value = config.getConfig().get(key);
        String string;
        if (value instanceof ArrayList) {
            string = StringUtil.pickRandom((ArrayList<String>) value);
        } else {
            string = config.getConfig().getString(key);
        }
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
        return getCommandConfig(command).getConfig().getString(key);
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
        return getFormattedString(getCommandConfig(command), key, format);
    }

    /**
     * Get the corresponding config for the command.
     *
     * @param command The command
     * @return The corresponding config for the command
     */
    public Config getCommandConfig(Command command) {
        for (Config config : commandConfigs) {
            if (config.getId().equals(command.getName())) {
                return config;
            }
        }
        return null;
    }

    /**
     * Get whether or not the command config has a certain key (i.e. somecommand.yml contains key "somekey"?)
     *
     * @param command The command
     * @param key     The key
     * @return Whether or not the command config has a certain key
     */
    public boolean hasCommandKey(Command command, String key) {
        return getCommandConfig(command).getConfig().contains(key);
    }

    /**
     * Reload all of the configs containing strings
     */
    public void reload() {
        for (Config config : commandConfigs) {
            config.reload();
        }
        messages.reload();
        variables.reload();
    }
}
