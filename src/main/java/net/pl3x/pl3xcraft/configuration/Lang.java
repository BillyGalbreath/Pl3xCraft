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
    public static String NO_PLAYER_SPECIFIED;
    public static String PLAYER_NOT_ONLINE;
    public static String GAMEMODE_SET;
    public static String GAMEMODE_SET_OTHER;
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
    public static String NO_PENDING_REQUESTS;
    public static String TARGET_HAS_PENDING_REQUEST;
    public static String TELEPORT_TOGGLED_OFF;
    public static String REQUEST_TIMED_OUT;
    public static String NO_TARGET_BLOCK;
    public static String BACK_DEATH_HINT;
    public static String NO_BACK_LOCATION;
    public static String TELEPORTING_BACK;
    public static String TELEPORTING_JUMP;
    public static String TELEPORTING_TOP;
    public static String TELEPORT_ACCEPT_REQUESTER;
    public static String TELEPORT_ACCEPT_TARGET;
    public static String TELEPORT_DENIED_REQUESTER;
    public static String TELEPORT_DENIED_TARGET;
    public static String TELEPORT_REQUEST_REQUESTER;
    public static String TELEPORT_REQUEST_TARGET;
    public static String TELEPORT_REQUESTHERE_REQUESTER;
    public static String TELEPORT_REQUESTHERE_TARGET;
    public static String TELEPORT_REQUESTALL_REQUESTER;
    public static String TELEPORT_TOGGLE_SET;
    public static String NOTHING_IN_HAND_TO_REMOVE;
    public static String COMMANDS_REMOVED_FROM_ITEM;
    public static String NOTHING_IN_HAND_TO_COMMAND;
    public static String COMMAND_ADDED_TO_ITEM;
    public static String NO_ASSIGN_COMMANDS_TO_ITEM;
    public static String ONLY_ITEMS_CAN_ASSIGN;
    public static String PLAYER_CANT_BURN;
    public static String PLAYER_HAS_BURNED_YOU;
    public static String YOU_BURNED_PLAYER;

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
        NO_PLAYER_SPECIFIED = config.getString("no-player-specified", "&4You must specify a player name!");
        PLAYER_NOT_ONLINE = config.getString("player-not-online", "&4That player is not online right now!");

        GAMEMODE_SET = config.getString("gamemode-set", "&dGamemode set to {gamemode}");
        GAMEMODE_SET_OTHER = config.getString("gamemode-set-other", "&dGamemode for {target} set to {gamemode}");

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

        NO_PENDING_REQUESTS = config.getString("no-pending-requests", "&4You do not have any pending teleport requests.");
        TARGET_HAS_PENDING_REQUEST = config.getString("target-has-pending-request", "&4{target} has pending request!");
        TELEPORT_TOGGLED_OFF = config.getString("target-toggled-off", "&4{target} has teleports toggled off!");
        REQUEST_TIMED_OUT = config.getString("request-timed-out", "&4{type} request has timed out!");
        NO_TARGET_BLOCK = config.getString("no-target-block", "&No block in line of sight, or too far away.");

        BACK_DEATH_HINT = config.getString("back-death-hint", "&dType &7/back &dto go back to where you died.");
        NO_BACK_LOCATION = config.getString("no-back-location", "&4No back location found!");

        TELEPORTING_BACK = config.getString("teleporting-back", "&dTeleporting back to previous location.");
        TELEPORTING_JUMP = config.getString("teleporting-jump", "&dJumped to block.");
        TELEPORTING_TOP = config.getString("teleporting-top", "&dTeleporting to highest block.");

        TELEPORT_ACCEPT_REQUESTER = config.getString("teleport-accept-requester", "&d{target} has accepted your teleport request.");
        TELEPORT_ACCEPT_TARGET = config.getString("teleport-accept-target", "&dYou have accepted the teleport request from {requester}.");

        TELEPORT_DENIED_REQUESTER = config.getString("teleport-denied-requester", "&d{target} has denied your teleport request.");
        TELEPORT_DENIED_TARGET = config.getString("teleport-denied-target", "&dYou have denied the teleport request from {requester}.");

        TELEPORT_REQUEST_REQUESTER = config.getString("teleport-request-requester", "&dTeleport request sent to &7{target}&d.");
        TELEPORT_REQUEST_TARGET = config.getString("teleport-request-target", "&7{requester} &dhas requested to teleport to you.\n&dType &7/tpaccept &dor &7/tpdeny&d.");

        TELEPORT_REQUESTHERE_REQUESTER = config.getString("teleport-requesthere-requester", "&dTeleport here request sent to &7{target}&d.");
        TELEPORT_REQUESTHERE_TARGET = config.getString("teleport-requesthere-target", "&7{requester} &dhas requested you to teleport to them.\n&dType &7/tpaccept &dor &7/tpdeny&d.");

        TELEPORT_REQUESTALL_REQUESTER = config.getString("teleport-requestall-requester", "&dTeleport here request sent to &7all players&d.");

        TELEPORT_TOGGLE_SET = config.getString("teleport-toggle-set", "&dPersonal teleports toggled &7{toggle}&d.");

        NOTHING_IN_HAND_TO_REMOVE = config.getString("nothing-in-hand-to-remove", "&dYou can not remove commands from air!");
        COMMANDS_REMOVED_FROM_ITEM = config.getString("commands-removed-from-item", "&dAll commands removed from &7{item}");
        NOTHING_IN_HAND_TO_COMMAND = config.getString("nothing-in-hand-to-command", "&You can not assign commands to air!");
        COMMAND_ADDED_TO_ITEM = config.getString("command-added-to-item", "&&d Added command &7/{commandInput} &dto that item.");
        NO_ASSIGN_COMMANDS_TO_ITEM = config.getString("no-assign-commands-to-item", "&dYou have no assigned commands to this item!");
        ONLY_ITEMS_CAN_ASSIGN = config.getString("only-items-can-assign", "&dYou can only assign a command to items, not blocks!");

        PLAYER_CANT_BURN = config.getString("player-cant-burn","&dYou cannot burn that player.");
        PLAYER_HAS_BURNED_YOU = config.getString("player-has-burned-you","&dYou have just been burned by {playerBurner}.");
        YOU_BURNED_PLAYER = config.getString("you-burned-player","&dYou just burned {burnedPlayer}.");
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
