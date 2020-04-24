package net.akoot.plugins.ultravanilla;

import net.akoot.plugins.ultravanilla.commands.UltraCommand;
import net.akoot.plugins.ultravanilla.util.IOUtil;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class UltraPlugin extends JavaPlugin {

    protected UltraVanilla uv;
    protected Strings strings;

    private Config changelog;

    public Config getChangelog() {
        return changelog;
    }

    protected Set<Config> configs;

    @Override
    public void onEnable() {

        uv = getClass().equals(UltraVanilla.class) ? (UltraVanilla) this : (UltraVanilla) getServer().getPluginManager().getPlugin("UltraVanilla");
        if (uv != null) {
            getLogger().info("Hooked to " + uv.getDescription().getFullName());
            UltraVanilla.hook(this);
        } else {
            getLogger().severe("Could not hook to UltraVanilla, disabling.");
            getServer().getPluginManager().disablePlugin(this);
        }

        getDataFolder().mkdirs();

        strings = new Strings(this, getClass());
        changelog = new Config(this, getClass(), "changelog.yml", "changelog");

        copyDefaults("changelog.yml");

        configs = new HashSet<>();

        registerConfig(changelog);

        start();
    }

    protected void start() {
    }

    protected void registerCommand(String name, UltraCommand command) {
        PluginCommand cmd = getCommand(name);
        if (cmd != null) {
            cmd.setExecutor(command);
        }
    }

    protected void registerConfig(Config config) {
        configs.add(config);
    }

    protected void serialize(Class<? extends ConfigurationSerializable> c, String alias) {
        ConfigurationSerialization.registerClass(c, alias);
    }

    protected void registerEvents(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    public void reload() {
        getDataFolder().mkdirs();
        strings.reload();
        for (Config config : configs) {
            config.reload();
        }
        reloadConfig();
    }

    public Strings getStrings() {
        return strings;
    }

    protected void copyDefaults(String file) {
        IOUtil.copyDefaults(getDataFolder(), file, getClass(), true);
    }
}
