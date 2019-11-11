package net.akoot.plugins.ultravanilla;

import net.akoot.plugins.ultravanilla.reference.UserPaths;
import net.akoot.plugins.ultravanilla.serializable.Position;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collections;
import java.util.List;

public class EventListener implements Listener {

    private UltraVanilla plugin;

    public EventListener(UltraVanilla instance) {
        plugin = instance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        // Create player and variable
        Player player = event.getPlayer();
        String name = player.getName();

        // Register the user config in the Users.users Map
        Users.registerUser(player);

        // Get the user config
        YamlConfiguration config = Users.getUser(player);

        // Set the first-join time if they joined for the first time
        if (!event.getPlayer().hasPlayedBefore()) {
            config.set(UserPaths.FIRST_JOIN, System.currentTimeMillis());
            config.set(UserPaths.PAST_NAMES, Collections.singletonList(name));
        }

        // If the user has changed their username, add it to their past-names list
        List<String> pastNames = config.getStringList(UserPaths.PAST_NAMES);
        if (!pastNames.contains(name)) {
            pastNames.add(name);
            config.set(UserPaths.PAST_NAMES, pastNames);
        }

        // Save the config
        Users.saveUser(player);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {

        // Create player variable
        Player player = event.getPlayer();

        // Get the user config
        YamlConfiguration config = Users.getUser(player);

        // Set the playtime
        long difference = System.currentTimeMillis() - config.getLong(UserPaths.LAST_LEAVE);
        config.set(UserPaths.PLAYTIME, config.getLong(UserPaths.PLAYTIME, 0L) + difference);

        // Set the last-leave time
        config.set(UserPaths.LAST_LEAVE, System.currentTimeMillis());

        // Set the last position
        config.set(UserPaths.LAST_POSITION, new Position(player.getLocation()));

        // Register the user config in the Users.users Map
        Users.unregisterUser(player);
    }
}
