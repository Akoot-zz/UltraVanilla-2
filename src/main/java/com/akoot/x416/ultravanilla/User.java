package com.akoot.x416.ultravanilla;

import com.akoot.x416.ultravanilla.reference.UserPaths;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class User {

    private static final File DIR = new File(UltraVanilla.getInstance().getDataFolder().getParentFile(), "users");

    /**
     * Retrieve a YamlConfiguration object
     *
     * @param player
     * @return
     */
    public static YamlConfiguration getConfig(OfflinePlayer player) {
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
        return new File(User.DIR, player.getUniqueId().toString() + ".yml");
    }

    /**
     * Set a value in a player's config
     *
     * @param player The player
     * @param key    The key
     * @param value  The desired value
     */
    public static void set(OfflinePlayer player, String key, String value) {
        YamlConfiguration config = getConfig(player);
        config.set(key, value);
        try {
            config.save(getUserFile(player));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a list of a player's previous known usernames
     *
     * @param player The player
     * @return
     */
    public static Set<String> getNames(OfflinePlayer player) {
        return (Set<String>) getConfig(player).getList(UserPaths.PAST_NAMES);
    }

    /**
     * Set a player's nickname (Using '&' for color coding).
     * If the player is online, apply the displayname
     *
     * @param player   The player
     * @param nickname The player's new custom name
     */
    public static void setNickname(OfflinePlayer player, String nickname) {
        set(player, UserPaths.NICKNAME, nickname);
        if (player instanceof Player) {
            ((Player) player).setDisplayName(nickname + ChatColor.RESET);
        }
    }
}
