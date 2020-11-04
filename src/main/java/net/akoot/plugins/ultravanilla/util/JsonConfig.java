package net.akoot.plugins.ultravanilla.util;

import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class JsonConfig extends Config {

    private JSONObject jsonObject;

    public JsonConfig(JavaPlugin plugin, Class root, String path, String id) {
        super(plugin, root, path, id);
        config = null;
        reload();
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    @Override
    public void reload() {
        try {
            if (!file.exists()) {
                IOUtil.copyDefaults(plugin.getDataFolder(), path, root);
            }
            jsonObject = new JSONObject(new String(Files.readAllBytes(file.toPath())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveConfig() {
        try {
            Files.write(file.toPath(), jsonObject.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasKey(String key) {
        return jsonObject.has(key);
    }
}
