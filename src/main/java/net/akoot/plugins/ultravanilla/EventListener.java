package net.akoot.plugins.ultravanilla;

import net.akoot.plugins.ultravanilla.util.Channel;
import net.akoot.plugins.ultravanilla.util.Colors;
import net.akoot.plugins.ultravanilla.util.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {

    private final UltraVanilla plugin;

    public EventListener(UltraVanilla instance) {
        plugin = instance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Create player and variable
        Player player = event.getPlayer();

        // Register the user config in the Users.users Map
        plugin.getUsers().add(new User(player));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        // Create player variable
        Player player = event.getPlayer();
        User user = plugin.getUser(player);

        if (user != null) {
            // Set the last position
            user.setLastLocation(player.getLocation());

            plugin.getUsers().remove(user);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = Colors.translate(event.getMessage());
        User user = plugin.getUser(event.getPlayer());
        Channel channel = plugin.getChannel(user.getPrimaryChannelId());
        for (Player recipient : event.getRecipients()) {
            if ((channel.isPublic() || channel.isMember(recipient)) && (!user.isChannelHidden(channel) && !plugin.getUser(recipient).isChannelHidden(channel))) {
                channel.chat(event.getPlayer(), recipient, message);
            }
        }
        event.setCancelled(true);
    }
}
