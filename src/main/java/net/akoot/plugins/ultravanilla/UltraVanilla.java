package net.akoot.plugins.ultravanilla;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.akoot.plugins.ultravanilla.commands.ChannelCommand;
import net.akoot.plugins.ultravanilla.commands.UltravanillaCommand;
import net.akoot.plugins.ultravanilla.serializable.Position;
import net.akoot.plugins.ultravanilla.serializable.PositionLite;
import net.akoot.plugins.ultravanilla.util.*;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;

public final class UltraVanilla extends UltraPlugin {

    private static UltraVanilla instance;
    private static final Set<UltraPlugin> hooks = new HashSet<>();
    public static final String USERS_DIR = "users";

    public static UltraVanilla getInstance() {
        return instance;
    }

    public static Set<UltraPlugin> getHooks() {
        return hooks;
    }

    public static void hook(UltraPlugin plugin) {
        hooks.add(plugin);
    }

    private static Gson gson;
    private static Random random;

    private List<Channel> channels;
    private File channelsFile;
    private List<User> users;
    private List<OfflineUser> offlineUsers;

    public static Random getRandom() {
        return random;
    }

    public List<User> getUsers() {
        return users;
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

    public static Gson getGson() {
        return gson;
    }

    public List<OfflineUser> getOfflineUsers() {
        return offlineUsers;
    }

    @Override
    public void start() {

        // Set instance to uv
        instance = uv;

        // Gson instance
        gson = new GsonBuilder().setPrettyPrinting().create();

        // Random instance
        random = new Random();

        // Register serializable classes
        serialize(Position.class, "Position");
        serialize(PositionLite.class, "Pos");

        // Create directories
        new File(USERS_DIR).mkdir();

        // Generate users and offline users
        offlineUsers = new ArrayList<>();
        users = new ArrayList<>();

        // Copy defaults from the jar if needed
        copyDefaults("config.yml");

        // Read colors from JSON
        registerConfig(new JsonConfig(this, getClass(), "colors.json", "colors"));

        // Initiate Colors class
        Colors.init();

        // Get chat channels
        channelsFile = new File(getDataFolder(), "channels.json");
        loadChannels();

        // Register /ultravanilla command
        registerCommand("ultravanilla", new UltravanillaCommand(uv));

        // Register /channel command
        ChannelCommand channelCommand = new ChannelCommand(uv);
        registerCommand("channelmod", channelCommand);
        registerCommand("editchannel", channelCommand);
        registerCommand("leavechannel", channelCommand);
        registerCommand("uninvite", channelCommand);
        registerCommand("invite", channelCommand);
        registerCommand("deletechannel", channelCommand);
        registerCommand("createchannel", channelCommand);
        registerCommand("listchannels", channelCommand);
        registerCommand("channel", channelCommand);

        // Register events
        registerEvents(new EventListener(uv));
    }

    public Channel getChannel(String id) {
        for (Channel channel : channels) {
            if (channel.getId().equals(id)) {
                return channel;
            }
        }
        return null;
    }

    public boolean hasChannel(String id) {
        return getChannel(id) != null;
    }

    public void removeChannel(String id) {
        for (Channel channel : channels) {
            if (channel.getId().equals(id)) {
                for (String uuid : channel.getAllMembers()) {
                    User.set(UUID.fromString(uuid), "primary-channel", null);
                }
                channels.remove(channel);
                return;
            }
        }
    }

    public void loadChannels() {

        if (!channelsFile.exists()) {
            try {
                channelsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            channels = gson.fromJson(Files.readString(channelsFile.toPath()), new TypeToken<List<Channel>>() {
            }.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (channels == null) {
            channels = new ArrayList<>();
        }

        if (!hasChannel("global")) {
            Channel globalChannel = new Channel("global");
            globalChannel.setName("Global Chat");
            globalChannel.setDescription("Chat everyone can see.");
            globalChannel.setColor("#ffffff");
            globalChannel.setPublic(true);
            channels.add(globalChannel);
        }
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void saveChannels() {
        try {
            Files.writeString(channelsFile.toPath(), gson.toJson(channels), StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User getUser(Player player) {
        for (User user : users) {
            if (user.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                return user;
            }
        }
        return null;
    }

    public List<String> getValidChannelIds(Player player) {
        List<String> validChannels = new ArrayList<>();
        for (Channel channel : UltraVanilla.getInstance().getChannels()) {
            if (channel.isMember(player)) {
                validChannels.add(channel.getId());
            }
        }
        return validChannels;
    }
}
