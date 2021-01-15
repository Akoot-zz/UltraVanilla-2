package net.akoot.plugins.ultravanilla.util;

import net.akoot.plugins.ultravanilla.UltraVanilla;
import net.akoot.plugins.ultravanilla.serializable.PositionLite;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class OfflineUser {

    private final File configFile;
    protected OfflinePlayer offlinePlayer;
    protected YamlConfiguration config;
    protected File usersDir;

    public boolean isChannelHidden(String id) {
        return getHiddenChannelIds().contains(id);
    }

    public boolean isChannelHidden(Channel channel) {
        return getHiddenChannels().contains(channel);
    }

    public List<String> getHiddenChannelIds() {
        return config.getStringList(Map.HIDDEN_CHANNELS);
    }

    public OfflineUser(OfflinePlayer offlinePlayer) {
        this.offlinePlayer = offlinePlayer;
        usersDir = new File(UltraVanilla.getInstance().getDataFolder().getParentFile().getParentFile(), "users");
        configFile = new File(usersDir, offlinePlayer.getUniqueId().toString() + ".yml");
        loadConfig();
    }

    public List<Channel> getHiddenChannels() {
        List<Channel> channels = new ArrayList<>();
        for (String id : getHiddenChannelIds()) {
            Channel channel = UltraVanilla.getInstance().getChannel(id);
            if (channel != null) {
                channels.add(channel);
            }
        }
        return channels;
    }

    public void hideChannel(String id) {
        addString(Map.HIDDEN_CHANNELS, id, false);
    }

    /**
     * Get a user's primary channel, aka the one they are actively speaking in
     *
     * @return The user's primary channel
     */
    public String getPrimaryChannelId() {
        return config.getString(Map.PRIMARY_CHANNEL, "global");
    }

    /**
     * Set a value to a user with the specified UUID
     *
     * @param uid   The UUID of the user
     * @param path  The path to set a new value to
     * @param value The value to set to the path specified
     */
    public static void set(UUID uid, String path, Object value) {
        try {
            YamlConfiguration config = getConfig(uid);
            if (config != null) {
                config.set(path, value);
                config.save(getUserFile(uid));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a user's config file reference
     *
     * @param uid The UUID of the user
     * @return The config file reference of the specified user
     */
    public static File getUserFile(UUID uid) {
        return new File(UltraVanilla.USERS_DIR, uid.toString() + ".yml");
    }

    /**
     * Get a YamlConfiguration of a user with the specified UUID
     *
     * @param uid The UUID of the user
     * @return A YamlConfiguration instance
     */
    public static YamlConfiguration getConfig(UUID uid) {
        try {
            YamlConfiguration config = new YamlConfiguration();
            config.load(getUserFile(uid));
            return config;
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Load the user's YamlConfiguration from users/${uuid}.yml, create one if needed
     */
    public void loadConfig() {
        config = new YamlConfiguration();
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save the user's YamlConfiguration to file (users/{uuid}.yml)
     */
    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the user's config instance
     *
     * @return The user's YamlConfiguration
     */
    public YamlConfiguration getConfig() {
        return config;
    }

    /**
     * Get a user's nickname
     *
     * @return A user's nickname, null if they don't have one
     */
    public String getNickName() {
        return config.getString(Map.NICKNAME);
    }

    /**
     * Get a list of a user's homes
     *
     * @return A list of a user's homes
     */
    public List<PositionLite> getHomes() {
        return (List<PositionLite>) config.getList(Map.HOMES, new ArrayList<PositionLite>());
    }

    /**
     * Set the primary channel of this user to one with the ID specified
     *
     * @param id The ID of the channel
     */
    public void setPrimaryChannel(String id) {
        set(Map.PRIMARY_CHANNEL, id);
    }

    /**
     * Get the OfflinePlayer instance of this user
     *
     * @return The OfflinePlayer instance of this user
     */
    public OfflinePlayer getOfflinePlayer() {
        return offlinePlayer;
    }

    /**
     * Change a channel's visibility for a player. If a channel is invisible, the user will not see any incoming
     * messages from it.
     *
     * @param id      The ID of the channel to change visibility of
     * @param visible Whether or not to hide the channel
     */
    public void setChannelVisible(String id, boolean visible) {
        List<String> visibleChannels = config.getStringList(Map.HIDDEN_CHANNELS);
        if (visible) {
            visibleChannels.add(id);
        } else {
            visibleChannels.remove(id);
        }
        set(Map.HIDDEN_CHANNELS, visibleChannels);
    }

    /**
     * Set the home of a user. If the home exists, the location will be overwritten
     *
     * @param name     The name of the home to set
     * @param location The location of the home to set
     */
    public void setHome(String name, Location location) {
        List<PositionLite> homes = getHomes();
        for (PositionLite home : homes) {
            if (home.getName().equalsIgnoreCase(name)) {
                home.setLocation(location);
                set(Map.HOMES, homes);
                return;
            }
        }
        homes.add(new PositionLite(name, location));
        set(Map.HOMES, homes);
    }

    /**
     * Set a value in this user's config to the value specified, then save it to their file
     *
     * @param path  The path in the YamlConfiguration to set to the value specified
     * @param value The value to set to the path specified
     */
    public void set(String path, Object value) {
        config.set(path, value);
        saveConfig();
    }

    /**
     * Add a string to a path in the user's YamlConfiguration. This automatically saves their config to file
     *
     * @param path            The path in the YamlConfiguration to add a string to
     * @param value           The value to add to the string list
     * @param allowDuplicates Whether or not to allow duplicate values
     * @return Whether or not the value was added to the list
     */
    public boolean addString(String path, String value, boolean allowDuplicates) {
        List<String> list = config.getStringList(path);
        if (allowDuplicates || !list.contains(value)) {
            list.add(value);
            set(path, list);
            return true;
        }
        return false;
    }

    /**
     * Remove a string from a list in a user's YamlConfiguration
     *
     * @param path  The path in the YamlConfiguration to remove a string from
     * @param value The value to remove from the string list
     */
    public boolean removeString(String path, String value) {
        List<String> list = config.getStringList(path);
        if (list.contains(value)) {
            list.remove(value);
            set(path, list);
            return true;
        }
        return false;
    }

    /**
     * Check if a user has a home with the specified name (case insensitive)
     *
     * @param name The name of the home you want to check
     * @return Whether or not a user has a home with that name
     */
    public boolean hasHome(String name) {
        for (PositionLite home : getHomes()) {
            if (home.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Remove a home from a user
     *
     * @param name The name of the home to remove
     * @return Whether or not a home exists and was removed
     */
    public boolean removeHome(String name) {
        List<PositionLite> homes = getHomes();
        for (PositionLite home : homes) {
            if (home.getName().equalsIgnoreCase(name)) {
                homes.remove(home);
                set(Map.HOMES, homes);
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether a user is known as a certain alias. This will check their username, nickname, and aliases
     *
     * @param alias      The alias to compare to the items listed above
     * @param ignoreCase Whether or not case matters while comparing the alias
     * @return Whether or not the alias matches the items listed above
     */
    public boolean isKnownAs(String alias, boolean ignoreCase) {
        List<String> aliases = config.getStringList(Map.ALIASES);
        String nickName = getNickName();
        String regex = (nickName != null ? nickName + "|" : "") + (aliases.isEmpty() ? String.join("|", aliases) : "") + "|" + offlinePlayer.getName();
        if (!ignoreCase) {
            return alias.matches(regex);
        } else {
            return alias.toLowerCase(Locale.ROOT).matches(regex.toLowerCase(Locale.ROOT));
        }
    }

    public Location getLastLocation() {
        PositionLite position = config.getSerializable(Map.LAST_POSITION, PositionLite.class);
        if (position != null) {
            return position.getLocation();
        }
        return null;
    }

    /**
     * Get a user's display name when they talk in chat.
     *
     * @return A user's display name. Returns their nickname if applicable, otherwise their username
     */
    private String getDisplayName() {
        String nickName = getNickName();
        if (nickName == null) {
            return offlinePlayer.getName();
        } else {
            return nickName;
        }
    }

    public void setLastLocation(Location location) {
        set(Map.LAST_POSITION, new PositionLite(location));
    }

    public void showChannel(String id) {
        removeString(Map.HIDDEN_CHANNELS, id);
    }

    public static class Map {
        public static final String HIDDEN_CHANNELS = "channels.hidden";
        public static final String LAST_POSITION = "data.logout-location";
        public static final String NICKNAME = "profile.nickname";
        public static final String ALIASES = "profile.aliases";
        public static final String HOMES = "data.homes";
        public static final String PRIMARY_CHANNEL = "channels.primary";
        public static final String AFK = "data.afk";
    }
}
