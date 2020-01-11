package net.akoot.plugins.ultravanilla;

import net.akoot.plugins.ultravanilla.commands.UltravanillaCommand;
import net.akoot.plugins.ultravanilla.serializable.Position;

import java.util.HashSet;
import java.util.Set;

public final class UltraVanilla extends UltraPlugin {

    private static UltraVanilla instance;
    private Set<UltraPlugin> hooks;

    public static UltraVanilla getInstance() {
        return instance;
    }

    public Set<UltraPlugin> getHooks() {
        return hooks;
    }

    @Override
    public void start() {

        instance = this;

        // Register serializable classes
        serialize(Position.class, "Position");

        // Create directories
        Users.DIR.mkdir();

        // Copy defaults from the jar for config.yml if needed
        copyDefaults("config.yml");

        // Register /ultravanilla command
        registerCommand("ultravanilla", new UltravanillaCommand(this));

        // Register events
        registerEvents(new EventListener(this));

        // Initialize hooks
        hooks = new HashSet<>();
    }

    @Override
    public void onDisable() {
    }

    public void hook(UltraPlugin plugin) {
        hooks.add(plugin);
    }

}
