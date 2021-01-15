package net.akoot.plugins.ultravanilla.util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Channel {

    private String color;
    private String id;
    private String name;
    private String description;
    private List<String> mods;
    private transient ChatColor chatColor;
    private List<String> members;
    private boolean isPublic;

    public Channel(String id) {
        this.id = id;
        this.color = Colors.getHexString(Colors.randomColor());
        this.members = new ArrayList<>();
        this.mods = new ArrayList<>();
        this.name = id.substring(0, 1).toUpperCase() + id.substring(1);
        this.chatColor = ChatColor.of(color);
    }

    public void setChatColor(ChatColor chatColor) {
        this.chatColor = chatColor;
    }

    public boolean isMod(OfflinePlayer player) {
        return mods.contains(player.getUniqueId().toString());
    }

    public List<String> getMods() {
        return mods;
    }

    public void setMods(List<String> mods) {
        this.mods = mods;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getAllMembers() {
        List<String> allMembers = members;
        members.addAll(mods);
        return allMembers;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public void addMember(OfflinePlayer player) {
        members.add(player.getUniqueId().toString());
    }

    public void removeMember(OfflinePlayer player) {
        members.remove(player.getUniqueId().toString());
    }

    public boolean isMember(OfflinePlayer player) {
        return isPublic || members.contains(player.getUniqueId().toString()) || mods.contains(player.getUniqueId().toString());
    }

    public void removeMod(Player player) {
        mods.remove(player.getUniqueId().toString());
    }

    public void addMod(Player player) {
        mods.add(player.getUniqueId().toString());
    }

    public void chat(Player player, Player recipient, String message) {
        ChatColor chatColor = ChatColor.of(color);
        recipient.sendMessage(chatColor + "[" + name + "]" + ChatColor.RESET + " " + player.getDisplayName() + chatColor + "> " + ChatColor.RESET + message);
    }
}
