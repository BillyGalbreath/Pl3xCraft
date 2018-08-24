package net.pl3x.pl3xcraft.configuration;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Lang {
    public static String COMMAND_NO_PERMISSION;
    public static String PLAYER_COMMAND;
    public static String PLAYER_NOT_FOUND;
    public static String HOME_DOES_NOT_EXIST;
    public static String INVALID_HOME_NAME;
    public static String SPECIFY_HOME;
    public static String HOME_NOT_SET;
    public static String HOME_DELETED;
    public static String HOME_LIST;
    public static String HOME;
    public static String PLEASE_DELETE_HOMES;
    public static String HOME_SET;
    public static String HOME_SET_MAX;
    public static String BED_SPAWN_SET;
    public static String HOME_EXEMPT;
    public static String HOME_DELETE_EXEMPT;
    public static String HOME_LIST_EXEMPT;
    public static String HOME_SET_EXEMPT;

    public static void reload(JavaPlugin plugin) {
        String langFile = Config.LANGUAGE_FILE;
        File configFile = new File(plugin.getDataFolder(), langFile);
        if (!configFile.exists()) {
            plugin.saveResource(Config.LANGUAGE_FILE, false);
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        COMMAND_NO_PERMISSION = config.getString("command-no-permission", "&4You do not have permission for that command!");
        PLAYER_COMMAND = config.getString("player-command", "&4This command is only available to players!");
        PLAYER_NOT_FOUND = config.getString("player-not-found", "&4That player does not exist!");

        HOME_DOES_NOT_EXIST = config.getString("home-does-not-exist", "&4That home does not exist!");
        INVALID_HOME_NAME = config.getString("invalid-home-name", "&4Invalid home name!");
        SPECIFY_HOME = config.getString("specify-home", "&4Please specify a home!\n&7{home-list}");
        HOME_NOT_SET = config.getString("home-not-set", "&4You have not set a home!");

        HOME_DELETED = config.getString("home-deleted", "&dThe home &7{home} &dhas been deleted.");

        HOME_LIST = config.getString("home-list", "&dHomes&e: &7{home-list}");

        HOME = config.getString("home", "&dGoing to home &7{home}&d.");
        PLEASE_DELETE_HOMES = config.getString("please-delete-homes", "&4You currently exceed your homes limit! ({count}>{limit})\n&4To use homes please delete some until you are below your allowed limit.");

        HOME_SET = config.getString("home-set", "&dHome &7{home} &dset.");
        HOME_SET_MAX = config.getString("home-set-max", "&4Max number of homes reached! &e(&7{limit}&e)");

        BED_SPAWN_SET = config.getString("bed-spawn-set", "&dBed spawn set");

        HOME_EXEMPT = config.getString("home-exempt", "&4You cannot go to that player's home!");
        HOME_DELETE_EXEMPT = config.getString("home-delete-exempt", "&4You may not delete that player's home!");
        HOME_LIST_EXEMPT = config.getString("home-list-exempt", "&4You may not list that player's homes!");
        HOME_SET_EXEMPT = config.getString("home-set-exempt", "&4Cannot set that player's home!");
    }

    public static void send(CommandSender recipient, String message) {
        if (message == null) {
            return;
        }
        message = ChatColor.translateAlternateColorCodes('&', message);
        if (ChatColor.stripColor(message).isEmpty()) {
            return; // do not send blank messages
        }

        for (String part : message.split("\n")) {
            recipient.sendMessage(part);
        }
    }
}
