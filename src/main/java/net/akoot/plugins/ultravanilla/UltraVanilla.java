package net.akoot.plugins.ultravanilla;

import net.akoot.plugins.ultravanilla.commands.UltravanillaCommand;
import net.akoot.plugins.ultravanilla.serializable.Position;
import net.akoot.plugins.ultravanilla.serializable.PositionLite;

import java.util.HashSet;
import java.util.Set;

public final class UltraVanilla extends UltraPlugin {

    private static UltraVanilla instance;
    private static Set<UltraPlugin> hooks = new HashSet<>();

    public static UltraVanilla getInstance() {
        return instance;
    }

    public static Set<UltraPlugin> getHooks() {
        return hooks;
    }

    public static void hook(UltraPlugin plugin) {
        hooks.add(plugin);
    }

    public static UltraPlugin getHook(String name) {
        for (UltraPlugin hook : hooks) {
            if (hook.getDescription().getName().equalsIgnoreCase(name)) {
                return hook;
            }
        }
        return null;
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void start() {

        instance = uv;

        // Register serializable classes
        serialize(Position.class, "Position");
        serialize(PositionLite.class, "Pos");

        // Create directories
        Users.DIR.mkdir();

        // Copy defaults from the jar for config.yml if needed
        copyDefaults("config.yml");

        // Register /ultravanilla command
        registerCommand("ultravanilla", new UltravanillaCommand(this));

        // Register events
        registerEvents(new EventListener(this));
    }

}
