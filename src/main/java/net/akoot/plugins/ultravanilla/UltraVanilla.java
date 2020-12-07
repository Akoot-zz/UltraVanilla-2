package net.akoot.plugins.ultravanilla;

import net.akoot.plugins.ultravanilla.commands.UltravanillaCommand;
import net.akoot.plugins.ultravanilla.serializable.Position;
import net.akoot.plugins.ultravanilla.serializable.PositionLite;
import net.akoot.plugins.ultravanilla.util.Colors;
import net.akoot.plugins.ultravanilla.util.JsonConfig;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class UltraVanilla extends UltraPlugin {

    private static UltraVanilla instance;
    private static final Set<UltraPlugin> hooks = new HashSet<>();

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

    /**
     * Get the name of all the hooks
     *
     * @return The name of all the hooks
     */
    public static List<String> getHookNames() {
        List<String> hookNames = new ArrayList<>();
        for (UltraPlugin hook : hooks) {
            hookNames.add(hook.getName());
        }
        return hookNames;
    }

    @Override
    public void start() {

        instance = uv;

        // Register serializable classes
        serialize(Position.class, "Position");
        serialize(PositionLite.class, "Pos");

        // Create directories
        Users.DIR.mkdir();

        // Copy defaults from the jar if needed
        copyDefaults("config.yml");

        // Read colors from JSON
        registerConfig(new JsonConfig(this, getClass(), "colors.json", "colors"));

        // Read palettes from JSON
        registerConfig(new JsonConfig(this, getClass(), "palettes.json", "palettes"));

        // Initiate Colors class
        Colors.init();

        // Register /ultravanilla command
        registerCommand("ultravanilla", new UltravanillaCommand(this));

        // Register events
        registerEvents(new EventListener(this));
    }
}
