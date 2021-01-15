package net.akoot.plugins.ultravanilla.util;

import org.bukkit.entity.Player;

public class User extends OfflineUser {

    public User(Player player) {
        super(player);
    }

    public Player getPlayer() {
        return offlinePlayer.getPlayer();
    }

    /**
     * Checks whether a player is flagged as AFK
     *
     * @return Whether or not a player is flagged as AFK
     */
    public boolean isAfk() {
        return config.getBoolean(Map.AFK, false);
    }

    /**
     * Set a user's AFK status in their YamlConfiguration. This saves to their file
     *
     * @param afk Whether or not a user should be AFK
     */
    public void setAfk(boolean afk) {
        set(Map.AFK, afk);
    }
}
