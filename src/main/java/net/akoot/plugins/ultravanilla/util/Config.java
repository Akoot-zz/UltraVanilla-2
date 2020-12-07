package net.akoot.plugins.ultravanilla.util;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Config {

    protected String path;
    protected YamlConfiguration config;
    protected String id;
    protected File file;
    protected Class root;
    protected JavaPlugin plugin;

    public Config(JavaPlugin plugin, Class root, String path, String id) {

        // Set the plugin
        this.plugin = plugin;

        // Set the root class to search for internal jar files
        this.root = root;

        // Create a YamlConfiguration instance
        config = new YamlConfiguration();

        // Set the path
        this.path = path;

        // Create a strings File reference
        file = new File(plugin.getDataFolder(), path);

        // Set the id
        this.id = id;

        // Load the configuration from file. Copy defaults from the jar if needed
        reload();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
     * Reload the strings object with the info written on file. Copy defaults from the jar if needed
     */
    public void reload() {
        try {
            if (!file.exists()) {
                IOUtil.copyDefaults(plugin.getDataFolder(), path, root);
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

    /**
     * Checks if the configuration contains the specified key.
     *
     * @param key The key to search for
     * @return Whether or not the configuration has the specified key
     */
    public boolean hasKey(String key) {
        return config.contains(key);
    }
}
