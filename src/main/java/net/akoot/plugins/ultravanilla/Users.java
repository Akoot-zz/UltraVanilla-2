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

    /**
     * Registers the player's config to memory
     *
     * @param player The player who's config you wish to save to memory
     */
    public static void registerUser(Player player) {
        users.put(player.getUniqueId(), Users.getOfflineConfig(player));
    }

    /**
     * Unregisters the player's config from memory
     * @param player The player who's config you want to remove from memory
     */
    public static void unregisterUser(Player player) {
        saveUser(player);
        users.remove(player.getUniqueId());
    }

    /**
     * Save a player configuration file with the stuff in memory
     *
     * @param player The player who's data you wish to save
     */
    public static void saveUser(OfflinePlayer player) {
        try {
            getUser(player).save(getUserFile(player));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the configuration file for a player
     *
     * @param player The player (online or offline)
     * @return The configuration for the player
     */
    public static YamlConfiguration getUser(OfflinePlayer player) {
        if (player.isOnline()) {
            return users.containsKey(player.getUniqueId()) ? users.get(player.getUniqueId()) : new YamlConfiguration();
        } else {
            return getOfflineConfig(player);
        }
    }

    /**
     * Retrieve a YamlConfiguration object for a player who is offline (reading file rather than memory)
     *
     * @param player The player who is offline
     * @return The configuration for the offline player
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
    public static void set(OfflinePlayer player, String key, Object value) {
        YamlConfiguration config = getOfflineConfig(player);
        config.set(key, value);
        try {
            config.save(getUserFile(player));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
