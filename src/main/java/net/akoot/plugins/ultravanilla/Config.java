package net.akoot.plugins.ultravanilla;

import net.akoot.plugins.ultravanilla.util.IOUtil;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Config {

    protected YamlConfiguration config;
    protected File file;
    protected Class root;

    public Config(JavaPlugin plugin, Class root, String path) {

        // Set the root class to search for internal jar files
        this.root = root;

        // Create a YamlConfiguration instance
        config = new YamlConfiguration();

        // Create a stringsFile reference
        file = new File(plugin.getDataFolder(), path);

        // Copy defaults from the jar for strings.yml if needed
        IOUtil.copyDefaults(file, root);

        // Load the configuration from file
        reload();
    }

    /**
     * Get the YamlConfiguration instance
     *
     * @return The YamlConfiguration instance
     */
    public YamlConfiguration getConfig() {
        return config;
    }

    /**
     * Reload the strings object with the info written on file
     */
    public void reload() {
        try {
            if (!file.exists()) {
                IOUtil.copyDefaults(file, root);
            }
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save the YamlConfiguration to the file
     */
    public void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
