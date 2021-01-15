package net.akoot.plugins.ultravanilla.commands;

import net.akoot.plugins.ultravanilla.UltraPlugin;
import net.akoot.plugins.ultravanilla.UltraVanilla;
import net.akoot.plugins.ultravanilla.util.Channel;
import net.akoot.plugins.ultravanilla.util.Colors;
import net.akoot.plugins.ultravanilla.util.User;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChannelCommand extends UltraCommand {

    private User user;

    public ChannelCommand(UltraPlugin plugin) {
        super(plugin, ChatColor.of("#ff0000"));
    }

    private boolean channelMod() {
        if (args.length == 2) {
            String id = args[0];
            Channel channel = uv.getChannel(id);
            if (channel != null) {
                String playerName = args[1];
                OfflinePlayer offlinePlayer = uv.getServer().getOfflinePlayer(playerName);
                if (offlinePlayer.hasPlayedBefore()) {
                    String uuid = offlinePlayer.getUniqueId().toString();
                    if (!channel.getMods().contains(uuid)) {
                        channel.getMods().add(uuid);
                        uv.saveChannels();
                        user.getPlayer().sendMessage("Gave " + offlinePlayer.getName() + " mod in channel \"" + id + "\"");
                    } else {
                        user.getPlayer().sendMessage(offlinePlayer.getName() + " is already a mod in channel \"" + id + "\"");
                    }
                } else {
                    playerInvalid(playerName);
                }
            } else {
                user.getPlayer().sendMessage("The channel \"" + id + "\" does not exist!");
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean editChannel() {
        if (args.length >= 2) {
            Channel channel = uv.getChannel(args[0]);
            if (channel != null) {
                String mode = args[1];
                String value = getArg(args, 2);
                switch (mode) {
                    case "id":
                        String id = args[2];
                        if (args.length == 3 && id.matches("[a-z0-9-_]{3,20}")) {
                            if (!uv.hasChannel(id)) {
                                channel.setId(args[2]);
                                uv.saveChannels();
                                user.getPlayer().sendMessage("Channel id changed to \"" + id + "\"");
                            } else {
                                user.getPlayer().sendMessage("A channel with the id \"" + id + "\" already exists!");
                            }
                        } else {
                            user.getPlayer().sendMessage("This channel id does not meet one or more of the criteria:\n" +
                                    "- Must be alphanumeric (a-z, 0-9), underscores and dashes (_, -) allowed.\n" +
                                    "- Must be between 3 and 20 characters long.\n" +
                                    "- Must be lowercase.");
                        }
                        break;
                    case "name":
                        channel.setName(value);
                        uv.saveChannels();
                        user.getPlayer().sendMessage("Channel name changed to \"" + value + "\"");
                        break;
                    case "description":
                        channel.setDescription(value);
                        user.getPlayer().sendMessage("Channel description changed to \"" + value + "\"");
                        break;
                    case "color":
                        String color = Colors.getHex(args[2]);
                        if (args.length == 3 && color != null) {
                            channel.setColor(color);
                            uv.saveChannels();
                            user.getPlayer().sendMessage("Channel color set to \"" + color + "\"");
                        } else {
                            user.getPlayer().sendMessage("The color you specified is not in the right format! Type in a hex code such as #ff00ff");
                        }
                        break;
                    case "public":
                        String isPublic = args[2];
                        if (isPublic.matches("true|false")) {
                            boolean bool = Boolean.parseBoolean(args[2]);
                            channel.setPublic(bool);
                            uv.saveChannels();
                            user.getPlayer().sendMessage("Channel is now " + (bool ? "public" : "private"));
                        } else {
                            user.getPlayer().sendMessage("You must specify either \"true\" or \"false\"");
                        }
                        break;
                    default:
                        return false;
                }
            }
        }
        return true;
    }

    private boolean leaveChannel() {
        if (args.length == 1) {
            String id = args[0];
            Channel channel = uv.getChannel(id);
            if (channel != null) {
                if (!channel.isPublic()) {
                    if (channel.isMember(user.getPlayer())) {
                        channel.removeMember(user.getPlayer());
                        user.getPlayer().sendMessage("You have left the channel \"" + id + "\"");
                        if (channel.isMod(user.getPlayer())) {
                            channel.removeMod(user.getPlayer());
                            if (channel.getMods().size() == 0) {
                                if (channel.getMembers().size() > 0) {
                                    String member = channel.getMembers().get(0);
                                    channel.getMods().add(member);
                                    channel.getMembers().remove(member);
                                    user.getPlayer().sendMessage("Because you were the last mod in this channel, " + uv.getServer().getOfflinePlayer(UUID.fromString(member)).getName() + " has been promoted to mod in your place.");
                                } else {
                                    uv.getChannels().remove(channel);
                                    user.getPlayer().sendMessage("Because you were the last member in this channel, it has been deleted. RIP.");
                                }
                            }
                        }
                    } else {
                        user.getPlayer().sendMessage("You are not part of the channel \"" + id + "\"!");
                    }
                } else {
                    user.getPlayer().sendMessage("You cannot leave public channels!");
                }
            } else {
                user.getPlayer().sendMessage("The channel \"" + id + "\" does not exist!");
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean uninvite() {
        if (args.length == 2) {
            String id = args[1];
            Channel channel = uv.getChannel(id);
            if (channel != null) {
                String playerName = args[0];
                OfflinePlayer offlinePlayer = uv.getServer().getOfflinePlayer(playerName);
                if (offlinePlayer.hasPlayedBefore()) {
                    String uuid = offlinePlayer.getUniqueId().toString();
                    if (channel.getMembers().contains(uuid)) {
                        channel.getMembers().remove(uuid);
                        uv.saveChannels();
                        user.getPlayer().sendMessage("Removed " + offlinePlayer.getName() + " from channel \"" + id + "\"");
                    } else {
                        user.getPlayer().sendMessage(offlinePlayer.getName() + " is not a member of channel \"" + id + "\"");
                    }
                } else {
                    playerInvalid(playerName);
                }
            } else {
                user.getPlayer().sendMessage("The channel \"" + id + "\" does not exist!");
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean invite() {
        if (args.length == 2) {
            String id = args[1];
            Channel channel = uv.getChannel(id);
            if (channel != null) {
                String playerName = args[0];
                OfflinePlayer offlinePlayer = uv.getServer().getOfflinePlayer(playerName);
                if (isValid(offlinePlayer)) {
                    String uuid = offlinePlayer.getUniqueId().toString();
                    if (!channel.isMember(offlinePlayer)) {
                        channel.getMembers().add(uuid);
                        uv.saveChannels();
                        user.getPlayer().sendMessage("Invited " + offlinePlayer.getName() + " to channel \"" + id + "\"");
                        if (offlinePlayer.isOnline()) {
                            offlinePlayer.getPlayer().sendMessage("You've been added to the channel \"" + id + "\"");
                        }
                    } else {
                        user.getPlayer().sendMessage(offlinePlayer.getName() + " is already a member in channel \"" + id + "\"");
                    }
                } else {
                    playerInvalid(playerName);
                }
            } else {
                user.getPlayer().sendMessage("The channel \"" + id + "\" does not exist!");
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean createChannel() {
        if (args.length == 1) {
            String id = args[0];
            Channel channel = new Channel(id);
            if (!uv.hasChannel(id)) {
                channel.addMod(user.getPlayer());
                uv.getChannels().add(channel);
                uv.saveChannels();
                user.getPlayer().sendMessage("Created a channel with id \"" + id + "\"");
            } else {
                user.getPlayer().sendMessage("A channel with that id already exists!");
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean deleteChannel() {
        if (args.length == 1) {
            String id = args[0];
            if (uv.hasChannel(id)) {
                uv.removeChannel(id);
                uv.saveChannels();
                user.getPlayer().sendMessage("Deleted a channel with id \"" + id + "\"");
            } else {
                user.getPlayer().sendMessage("A channel with that id already exists!");
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean listChannels() {
        if (args.length == 0) {
            user.getPlayer().sendMessage("Available channels:");
            for (Channel channel : uv.getChannels()) {
                if (channel.isMember(user.getPlayer())) {
                    user.getPlayer().sendMessage("- " + channel.getId());
                }
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean channel() {
        if (args.length == 1) {
            String id = args[0];
            Channel channel = uv.getChannel(id);
            if (channel != null) {
                if (channel.isMember(user.getPlayer())) {
                    if (user.getPrimaryChannelId().equals(id)) {
                        user.getPlayer().sendMessage("Your primary channel is already \"" + id + "\"");
                    } else {
                        user.setPrimaryChannel(id);
                        user.getPlayer().sendMessage("Set your primary channel to \"" + id + "\"");
                    }
                } else {
                    user.getPlayer().sendMessage("You do not belong to a channel called \"" + id + "\"");
                }
            } else {
                user.getPlayer().sendMessage("The channel \"" + id + "\" does not exist!");
            }
            user.saveConfig();
        } else {
            return false;
        }
        return true;
    }

    @Override
    protected boolean onCommand() {
        if (sender instanceof Player) {
            // Set player
            user = uv.getUser((Player) sender);

            // Handle each command
            switch (command.getName()) {
                case "channelmod":
                    return channelMod();
                case "editchannel":
                    return editChannel();
                case "leavechannel":
                    return leaveChannel();
                case "uninvite":
                    return uninvite();
                case "invite":
                    return invite();
                case "createchannel":
                    return createChannel();
                case "deletechannel":
                    return deleteChannel();
                case "listchannels":
                    return listChannels();
                case "channel":
                    return channel();
            }
        } else {
            playerOnly("handle channels");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete() {
        return null;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (command.getName().equals("channel")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    return UltraVanilla.getInstance().getValidChannelIds(player);
                }
            }
        } else if (command.getName().equals("editchannel")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    return UltraVanilla.getInstance().getValidChannelIds(player);
                }
            } else if (args.length == 1) {
                suggestions.add("name");
                suggestions.add("id");
                suggestions.add("color");
                suggestions.add("description");
            } else if (args.length == 2) {
                if (args[1].equals("color")) {
                    return Colors.getAllPaletteNames();
                }
            }
        }
        return getSuggestions(suggestions, args);
    }
}
