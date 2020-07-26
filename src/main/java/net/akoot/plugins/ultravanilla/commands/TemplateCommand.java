package net.akoot.plugins.ultravanilla.commands;

import net.akoot.plugins.ultravanilla.UltraPlugin;
import net.md_5.bungee.api.ChatColor;

import java.util.List;

public class TemplateCommand extends UltraCommand {

    public TemplateCommand(UltraPlugin instance) {
        super(instance, ChatColor.WHITE);
    }

    @Override
    public boolean onCommand() {
        return false;
    }

    @Override
    public List<String> onTabComplete() {
        return null;
    }
}
