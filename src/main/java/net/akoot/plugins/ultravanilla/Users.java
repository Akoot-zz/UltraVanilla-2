package net.akoot.plugins.ultravanilla;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Users {

    public static final File DIR = new File(UltraVanilla.getInstance().getDataFolder().getParentFile().getParentFile(), "users");
    private static Map<UUID, YamlConfiguration> users = new HashMap<>();

    public static void registerUser(Player player) {
        users.put(player.getUniqueId(), Users.getOfflineConfig(player));
    }

    public static void unregisterUser(Player player) {
        saveUser(player);
        users.remove(player.getUniqueId());
    }

    public static void saveUser(Player player) {
        try {
            getUser(player).save(getUserFile(player));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static YamlConfiguration getUser(Player player) {
        return users.containsKey(player.getUniqueId()) ? users.get(player.getUniqueId()) : new YamlConfiguration();
    }

    /**
     * Retrieve a YamlConfiguration object
     *
     * @param player
     * @return
     */
    public static YamlConfiguration getOfflineConfig(OfflinePlayer player) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(getUserFile(player));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return config;
    }

    /**
     * Get the file object of a user configuration file
     *
     * @param player The player
     * @return The file object of a user configuration file
     */
    public static File getUserFile(OfflinePlayer player) {
        File file = new File(Users.DIR, player.getUniqueId().toString() + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * Set a value in a player's config
     *
     * @param player The player
     * @param key    The key
     * @param value  The desired value
     */
    public static void set(OfflinePlayer player, String key, String value) {
        YamlConfiguration config = getOfflineConfig(player);
        config.set(key, value);
        try {
            config.save(getUserFile(player));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
