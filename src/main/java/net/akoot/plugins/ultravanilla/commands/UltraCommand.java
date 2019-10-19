package net.akoot.plugins.ultravanilla.commands;

import net.akoot.plugins.ultravanilla.Strings;
import net.akoot.plugins.ultravanilla.UltraVanilla;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UltraCommand {

    public ChatColor color;
    protected JavaPlugin plugin;
    protected Random random;
    protected Strings strings;
    protected Strings uvStrings;

    public UltraCommand(JavaPlugin plugin, Strings strings, ChatColor color) {
        this.plugin = plugin;
        this.strings = strings;
        this.random = new Random();
        this.color = color;
        this.uvStrings = UltraVanilla.getInstance().getStrings();
    }

    public UltraCommand(JavaPlugin plugin, Strings strings) {
        this(plugin, strings, ChatColor.WHITE);
    }

    /**
     * Adds an ' or an 's depending if someone's name ends with S or not.
     *
     * @param item A name
     * @return A name with 's or ' depending if it ends with S i.e. notch's, ness'
     */
    protected String posessive(String item) {
        return item + (item.endsWith("s") ? "'" : "'s");
    }

    /**
     * Combines arguments separated by a space into one single argument using double quotes '"'.
     *
     * @param args All of the arguments
     * @return Arguments with spaces
     */
    protected String[] refinedArgs(String[] args) {
        Pattern pattern = Pattern.compile("\"[^\"]+\"|[-\\w]+");
        Matcher matcher = pattern.matcher(String.join(" ", args));
        List<String> refined = new ArrayList<>();
        while (matcher.find()) {
            refined.add(matcher.group().replace("\"", ""));
        }
        return refined.toArray(new String[0]);
    }

    /**
     * Retrieves an argument value AFTER the argument specified.
     *
     * @param args All of the arguments
     * @param arg  The argument BEFORE the desired argument value
     * @return The desired argument value
     */
    protected String getArgFor(String[] args, String arg) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(arg)) {
                if (i + 1 < args.length) {
                    return args[i + 1];
                }
            }
        }
        return null;
    }

    /**
     * Converts the arguments into one single argument.
     *
     * @param args All of the arguments
     * @return The combined arguments as a string separated by a space " "
     */
    protected String getArg(String[] args) {
        String arg = String.join(" ", args);
        return arg.trim();
    }

    /**
     * Retrieve every argument after the specified index as an entire String.
     *
     * @param args  All of the arguments
     * @param index The index to start the argument with
     * @return The combined arguments after the index as a String
     */
    protected String getArg(String[] args, int index) {
        String message = "";
        for (int i = index - 1; i < args.length; i++) {
            message += args[i] + " ";
        }
        return message.trim();
    }

    /**
     * Convert a list of players into a String.
     *
     * @param players The list of players
     * @return A string containing all of the players' username(s) separated with a comma and with the final username
     * with an 'and' before it
     */
    protected String playerList(List<Player> players) {
        String list = "";
        if (players.size() == 1) {
            return players.get(0).getName();
        }
        for (int i = 0; i < players.size(); i++) {
            if (i == players.size() - 1) {
                list += "and " + players.get(i).getName();
            } else {
                list += players.get(i).getName() + ", ";
            }
        }
        return list;
    }

    /**
     * Get a list of players from the specified argument. This handles stuff like @a and @r, as well as allowing to
     * target specific players with commas.
     *
     * @param arg The argument. Could be username(s), nickname(s), @a and @r
     * @return A list of players which have been selected using the argument
     */
    protected List<Player> getPlayers(String arg) {

        // Create the list of players to return
        List<Player> players = new ArrayList<>();

        // Handle @a selector
        if (arg.equals("@a")) {
            players.addAll(plugin.getServer().getOnlinePlayers());
        }

        // Handle @r selector
        else if (arg.equalsIgnoreCase("@r")) {

            // Temporarily fill 'players' list with all online players
            players.addAll(plugin.getServer().getOnlinePlayers());

            // If there is at least 1 player online, clear 'players' list and add one random player from the list
            if (players.size() > 0) {
                Player player = players.get(random.nextInt(players.size()));
                players = new ArrayList<>();
                players.add(player);
            }
        }

        // Handle commas
        else if (arg.contains(",")) {

            // Split each username with a comma and for add all valid players to the 'players' list
            for (String name : arg.split(",")) {

                // Get the player with the username
                Player player = plugin.getServer().getPlayer(name);

                // If the player is online, add them to the 'players' list
                if (player != null) {
                    players.add(player);
                }
            }
        }

        // Anything else will be treated as a regular username, will return an empty list if no player was found
        else {
            Player player = plugin.getServer().getPlayer(arg);
            if (player != null) {
                players.add(player);
            }
        }
        return players;
    }

    /**
     * Get a non-formatted string from config.yml in the 'command' section.
     *
     * @param command The command
     * @param key     The name of the key after 'strings.'
     * @return The formatted string
     */
    protected String format(Command command, String key) {
        String string = strings.getCommandString(command, key);
        return color + string.replace("&:", color.toString());
    }

    /**
     * Get a formatted string from config.yml in the 'command' section.
     *
     * @param command The command
     * @param key     The name of the key after 'strings.'
     * @param format  The placeholder(s) and replacement(s)
     * @return The formatted string
     */
    protected String format(Command command, String key, String... format) {
        String string = strings.getFormattedCommandString(command, key, format);
        return color + string.replace("&:", color.toString());
    }

    /**
     * Get a formatted message for the specified command.
     *
     * @param command The command
     * @param key     The name of the key after 'strings.'
     * @param format  The placeholder(s) and replacement(s)
     * @return The formatted string
     */
    protected String message(Command command, String key, String... format) {
        return format(command, "message." + key, format);
    }

    /**
     * Get a formatted error message for the specified command.
     *
     * @param command The command
     * @param key     The name of the key after 'strings.'
     * @param format  The placeholder(s) and replacement(s)
     * @return The formatted error string
     */
    protected String error(Command command, String key, String... format) {
        return format(command, "error." + key, format);
    }


    /**
     * Get an integer from an argument or -1 if it's not a number.
     *
     * @param sender The sender to send the error message to if it's not a number
     * @param arg    The argument to retrieve the integer from
     * @return The integer gotten from the argument, -1 if it's not a number
     */
    protected int getInt(CommandSender sender, String arg) {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            sender.sendMessage(strings.getString("not-a-number", "{number}", arg));
            return -1;
        }
    }

    /**
     * Get a formatted list for the specified command.
     *
     * @param command The command
     * @param key     The key
     * @param values  The items to display as lists
     * @param format  The global format for this list
     * @return The formatted list string
     */
    protected String list(Command command, String key, List<String> values, String... format) {

        String list = "";
        String title = message(command, key + ".title", format);
        list += title;
        if (values.isEmpty()) {
            return list + uvStrings.getString("misc.none");
        }
        for (int i = 0; i < values.size(); i++) {
            String item = message(command, key + ".item", "%v", values.get(i));
            if (i == values.size() - 1) {
                if (item.endsWith("%, ")) {
                    item = item.substring(0, item.length() - 3);
                }
            }
            list += item.replaceAll("%,", ",");
        }
        return list;
    }

    /**
     * Return whether the sender has permission to issue a command.
     *
     * @param sender     The sender
     * @param permission The permission node
     * @return Whether or not the sender has the permission node
     */
    protected boolean hasPermission(CommandSender sender, Command command, String permission) {
        return sender.hasPermission(String.format("%s.command.%s.%s", plugin.getName().toLowerCase(), command.getName(), permission));
    }

    /**
     * Check if a player has joined before or is online.
     *
     * @param player The player
     * @return Whether or not a player is valid
     */
    protected boolean isValid(OfflinePlayer player) {
        return (player.hasPlayedBefore() || player.isOnline());
    }

    /**
     * Get a list of usernames from all the players who have joined the game once.
     *
     * @return A list of usernames from all the players who have joined the game once
     */
    protected List<String> getOfflinePlayerNames() {
        List<String> offlinePlayerNames = new ArrayList<>();
        for (OfflinePlayer player : plugin.getServer().getOfflinePlayers()) {
            offlinePlayerNames.add(player.getName());
        }
        return offlinePlayerNames;
    }

    /**
     * Get suggestions based on what the user has typed, like if they start typing the beginning of a suggestion then
     * only show the suggestions that start with that argument.
     *
     * @param suggestions All of the possible suggestions based on the argument count
     * @param args        The arguments the user has passed
     * @return The list of suggestions which are relevant to the query
     */
    protected List<String> getSuggestions(List<String> suggestions, String[] args) {
        List<String> realSuggestions = new ArrayList<>();
        for (String s : suggestions) {
            if (args[args.length - 1].length() < args.length || s.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
                realSuggestions.add(s);
            }
        }
        return realSuggestions;
    }
}
