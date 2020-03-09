package net.akoot.plugins.ultravanilla.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RawMessage {

    private List<RawComponent> components;

    public RawMessage() {
        components = new ArrayList<>();
    }

    /**
     * Get a JSON string of the raw message
     *
     * @return JSON string
     */
    public String getJSON() {
        StringBuilder json = new StringBuilder();

        if (!components.isEmpty()) {
            json = new StringBuilder("[");
            for (RawComponent component : components) {
                json.append("{\"text\":\"").append(component.getContent()).append("\"");

                if (!(component.getHoverText() == null || component.getHoverText().isEmpty())) {
                    json.append(",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"").append(component.getHoverText()).append("\"}");
                }

                if (!(component.getSuggestion() == null || component.getSuggestion().isEmpty())) {
                    json.append(",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"").append(component.getSuggestion()).append("\"}");
                } else if (!(component.getCommand() == null || component.getCommand().isEmpty())) {
                    json.append(",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"").append(component.getCommand()).append("\"}");
                } else if (!(component.getLink() == null || component.getLink().isEmpty())) {
                    json.append(",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"").append(component.getLink()).append("\"}");
                }

                json.append("},");
            }
            json = new StringBuilder(json.substring(0, json.length() - 1));
            json.append("]");
        }
        return json.toString();
    }

    /**
     * Add a component to the raw message
     *
     * @param component The component to add
     */
    public void addComponent(RawComponent component) {
        components.add(component);
    }

    /**
     * Send the raw message to a player
     *
     * @param player The player to send the raw message to
     */
    public void send(Player player) {
        player.getServer().dispatchCommand(Bukkit.getConsoleSender(), String.format("tellraw %s %s", player.getName(), getJSON()));
    }

    /**
     * Trim the last component by amount number of characters
     *
     * @param amount The number of characters to trim the last component
     */
    public void trimLast(int amount) {
        int i = components.size() - 1;

        if (i < 0) {
            return;
        }

        RawComponent component = components.get(i);
        String content = component.getContent();
        component.setContent(content.substring(0, content.length() - (amount + 1)));
        components.set(i, component);
    }
}
