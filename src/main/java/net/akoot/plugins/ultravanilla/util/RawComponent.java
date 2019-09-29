package net.akoot.plugins.ultravanilla.util;

public class RawComponent {

    private String content;
    private String link;
    private String suggestion;
    private String command;
    private String hoverText;

    /**
     * Construct an empty RawComponent without any display text (null)
     */
    public RawComponent() {
    }

    /**
     * Construct a basic RawComponent with just display text
     *
     * @param content The display text
     */
    public RawComponent(String content) {
        this.content = content;
    }

    /**
     * Generate a RawComponent with a command and hover text
     *
     * @param content   The component text
     * @param command   The command
     * @param hoverText The text to be shown when hovering with a mouse
     * @return The RawComponent with a command and display text
     */
    public static RawComponent command(String content, String command, String hoverText) {
        RawComponent component = new RawComponent(content);
        component.command = command;
        component.hoverText = hoverText;
        return component;
    }

    /**
     * Generate a RawComponent with a command
     *
     * @param content The component text
     * @param command The command
     * @return The RawComponent with a command and display text
     */
    public static RawComponent command(String content, String command) {
        RawComponent component = new RawComponent(content);
        component.command = command;
        return component;
    }

    /**
     * Generate a RawComponent with a suggested command and hover text
     *
     * @param content    The component text
     * @param suggestion The suggestion
     * @param hoverText  The text to be shown when hovering with a mouse
     * @return The RawComponent with a suggestion, display text, and hover text
     */
    public static RawComponent suggestion(String content, String suggestion, String hoverText) {
        RawComponent component = new RawComponent(content);
        component.suggestion = suggestion;
        component.hoverText = hoverText;
        return component;
    }

    /**
     * Generate a RawComponent with a suggested command
     *
     * @param content    The component text
     * @param suggestion The suggestion
     * @return The RawComponent with a suggestion and display text
     */
    public static RawComponent suggestion(String content, String suggestion) {
        RawComponent component = new RawComponent(content);
        component.suggestion = suggestion;
        return component;
    }

    /**
     * Generate a RawComponent with a link and hover text
     *
     * @param content   The component text
     * @param link      The link
     * @param hoverText The text to be shown when hovering with a mouse
     * @return The RawComponent with a link, display text, and hover text
     */
    public static RawComponent link(String content, String link, String hoverText) {
        RawComponent component = new RawComponent(content);
        component.link = link;
        component.hoverText = hoverText;
        return component;
    }

    /**
     * Generate a RawComponent with a link
     *
     * @param content The component text
     * @param link    The link
     * @return The RawComponent with a link and display text
     */
    public static RawComponent link(String content, String link) {
        RawComponent component = new RawComponent(content);
        component.link = link;
        return component;
    }

    /**
     * Get the visible text which will make up the component.
     *
     * @return The visible text
     */
    public String getContent() {
        return content;
    }

    /**
     * Set the visible text which will make up the component.
     *
     * @param content The visible text
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Get the URL that will open up after clicking on the component text.
     *
     * @return The URL
     */
    public String getLink() {
        return link;
    }

    /**
     * Set the URL that will open up after clicking on the component text.
     *
     * @param link The URL
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Get the suggested command that is placed in the player's chat box after clicking the component text.
     *
     * @return The suggested command
     */
    public String getSuggestion() {
        return suggestion;
    }

    /**
     * Set the suggested command that is placed in the player's chat box after clicking the component text.
     *
     * @param suggestion The suggested command
     */
    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    /**
     * Get the command that is executed after clicking the component text.
     *
     * @return The command that is executed
     */
    public String getCommand() {
        return command;
    }

    /**
     * Set the command to execute by the player after clicking on the component text.
     *
     * @param command The command to execute
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * Get the text that is shown when your mouse is placed over the component text.
     *
     * @return The component's hover text
     */
    public String getHoverText() {
        return hoverText;
    }

    /**
     * Set the text to show when your mouse is placed over the component text.
     *
     * @param hoverText The component's hover text
     */
    public void setHoverText(String hoverText) {
        this.hoverText = hoverText;
    }
}
