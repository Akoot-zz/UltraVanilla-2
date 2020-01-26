package net.akoot.plugins.ultravanilla;

import net.akoot.plugins.ultravanilla.reference.UltraPaths;
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
            config.set(UltraPaths.User.FIRST_JOIN, System.currentTimeMillis());
            config.set(UltraPaths.User.PAST_NAMES, Collections.singletonList(name));
        }

        // If the user has changed their username, add it to their past-names list
        List<String> pastNames = config.getStringList(UltraPaths.User.PAST_NAMES);
        if (!pastNames.contains(name)) {
            pastNames.add(name);
            config.set(UltraPaths.User.PAST_NAMES, pastNames);
        }

        // Set the last version of each hook
        for (UltraPlugin hook : UltraVanilla.getHooks()) {
            String lastVersionPath = UltraPaths.User.LAST_VERSION + "." + hook.getDescription().getName();
            String currentVersion = hook.getDescription().getVersion();
            if (!config.get(lastVersionPath, "").equals(currentVersion)) {
                player.performCommand("uv changelog " + hook.getDescription().getName().toLowerCase());
                config.set(UltraPaths.User.LAST_VERSION + "." + hook.getDescription().getName(), hook.getDescription().getVersion());
            }
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
        long difference = System.currentTimeMillis() - config.getLong(UltraPaths.User.LAST_LEAVE, System.currentTimeMillis());
        config.set(UltraPaths.User.PLAYTIME, config.getLong(UltraPaths.User.PLAYTIME, 0L) + difference);

        // Set the last-leave time
        config.set(UltraPaths.User.LAST_LEAVE, System.currentTimeMillis());

        // Set the last position
        config.set(UltraPaths.User.LAST_POSITION, new Position(player.getLocation()));

        // Register the user config in the Users.users Map
        Users.unregisterUser(player);
    }
}
