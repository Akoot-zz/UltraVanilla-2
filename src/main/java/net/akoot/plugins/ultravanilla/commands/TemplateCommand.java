package net.akoot.plugins.ultravanilla.commands;

import net.akoot.plugins.ultravanilla.Strings;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class TemplateCommand extends UltraCommand implements CommandExecutor, TabExecutor {

    public TemplateCommand(JavaPlugin instance, Strings strings) {
        super(instance, strings, ChatColor.WHITE);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        this.command = command;

        

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
