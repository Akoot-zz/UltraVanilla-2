package net.akoot.plugins.ultravanilla;

import net.akoot.plugins.ultravanilla.commands.UltravanillaCommand;
import net.akoot.plugins.ultravanilla.serializable.Position;
import net.akoot.plugins.ultravanilla.util.IOUtil;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class UltraVanilla extends JavaPlugin {

    private static UltraVanilla instance;
    private Strings strings;

    /**
     * Get the plugin instance.
     *
     * @return The plugin instance
     */
    public static UltraVanilla getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        // Set the instance for getInstance()
        UltraVanilla.instance = this;

        // Register serializable classes
        ConfigurationSerialization.registerClass(Position.class, "Position");

        // Create directories
        getDataFolder().mkdir();
        Users.DIR.mkdir();

        // Register strings instance for this plugin
        strings = new Strings(instance, getClass());

        // Copy defaults from the jar for config.yml if needed
        IOUtil.copyDefaults(new File(getDataFolder(), "config.yml"), getClass());

        // Register /ultravanilla command
        getCommand("ultravanilla").setExecutor(new UltravanillaCommand(instance, strings));

        // Register events
        getServer().getPluginManager().registerEvents(new EventListener(instance), instance);
    }

    public Strings getStrings() {
        return strings;
    }

    @Override
    public void onDisable() {
    }

}
