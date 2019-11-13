package net.akoot.plugins.ultravanilla.commands;

import net.akoot.plugins.ultravanilla.Strings;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class UserCommand extends UltraCommand implements CommandExecutor, TabCompleter {

    public UserCommand(JavaPlugin plugin, Strings strings) {
        super(plugin, strings, ChatColor.DARK_GREEN);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        this.command = command;

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
