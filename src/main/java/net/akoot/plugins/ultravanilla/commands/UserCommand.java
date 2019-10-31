package net.akoot.plugins.ultravanilla.commands;

import net.akoot.plugins.ultravanilla.Strings;
import net.akoot.plugins.ultravanilla.Users;
import net.akoot.plugins.ultravanilla.reference.UserPaths;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class UserCommand extends UltraCommand implements CommandExecutor, TabCompleter {

    public UserCommand(JavaPlugin plugin, Strings strings) {
        super(plugin, strings, ChatColor.DARK_GREEN);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        this.command = command;

        // Tests
        Player player = (Player) sender;
        YamlConfiguration config = Users.getUser(player);
        sender.sendMessage(message("get.single", "%p", player.getName(), "%k", UserPaths.FIRST_JOIN, "%v", Users.getUser(player).getLong(UserPaths.FIRST_JOIN) + ""));
        sender.sendMessage(message("get.single", "%p", player.getName(), "%k", UserPaths.LAST_LEAVE, "%v", Users.getUser(player).getLong(UserPaths.LAST_LEAVE) + ""));
        sender.sendMessage(list("get.list", config.getStringList(UserPaths.PAST_NAMES), "%p", player.getName(), "%k", UserPaths.PAST_NAMES));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
