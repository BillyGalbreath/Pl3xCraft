package net.pl3x.pl3xcraft.configuration;

import net.pl3x.pl3xcraft.Logger;
import net.pl3x.pl3xcraft.Pl3xCraft;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class Config {
    public static boolean COLOR_LOGS;
    public static boolean DEBUG_MODE;
    public static String LANGUAGE_FILE;

    public static int TELEPORT_REQUEST_TIMEOUT;
    public static boolean BACK_ON_DEATH;
    public static boolean USE_TELEPORT_SOUNDS;

    public static Sound SOUND_TO;
    public static Sound SOUND_FROM;

    public static boolean UNSAFE_ENCHANTMENTS;

    public static boolean ALLOW_REPAIR;
    public static boolean REPAIR_MAIN_HAND;

    public static Map<String, String> CANNED_RESPONSES = new HashMap<>();

    public static void reload(Pl3xCraft plugin) {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        COLOR_LOGS = config.getBoolean("color-logs", true);
        DEBUG_MODE = config.getBoolean("debug-mode", false);
        LANGUAGE_FILE = config.getString("language-file", "lang-en.yml");

        TELEPORT_REQUEST_TIMEOUT = config.getInt("teleport-request-timeout", 30);
        BACK_ON_DEATH = config.getBoolean("back-on-death", true);
        USE_TELEPORT_SOUNDS = config.getBoolean("use-teleport-sounds", true);

        try {
            SOUND_TO = Sound.valueOf(config.getString("sound-to", "ENTITY_ENDERMAN_TELEPORT"));
        } catch (IllegalArgumentException e) {
            Logger.warn("Invalid \"sound-to\" config value. Using \"ENTITY_ENDERMAN_TELEPORT\" instead.");
            SOUND_TO = Sound.ENTITY_ENDERMAN_TELEPORT;
        }
        try {
            SOUND_FROM = Sound.valueOf(config.getString("sound-from", "ENTITY_ENDERMAN_TELEPORT"));
        } catch (IllegalArgumentException e) {
            Logger.warn("Invalid \"sound-from\" config value. Using \"ENTITY_ENDERMAN_TELEPORT\" instead.");
            SOUND_FROM = Sound.ENTITY_ENDERMAN_TELEPORT;
        }

        UNSAFE_ENCHANTMENTS = config.getBoolean("unsafe-enchantments", false);

        ALLOW_REPAIR = config.getBoolean("allow-repair", true);
        REPAIR_MAIN_HAND = config.getBoolean("repair-main-hand", false);

        CANNED_RESPONSES.clear();
        ConfigurationSection responses = config.getConfigurationSection("canned-responses");
        if (responses != null) {
            for (String command : responses.getKeys(false)) {
                String response = responses.getString(command, "");
                Map<String, Command> knownCommands = Bukkit.getCommandMap().getKnownCommands();
                if (!knownCommands.containsKey(command)) {
                    Logger.debug("Registering canned response '" + command + "'");
                    Bukkit.getCommandMap().getKnownCommands().put(command, new Command(command) {
                        @Override
                        public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                            Lang.send(sender, response);
                            return false;
                        }
                    });
                } else {
                    Logger.warn("Cannot register canned-response '" + command + "' (command is already registered) Using backup command listener to handle.");
                    CANNED_RESPONSES.put(command, response);
                }
            }
        }
    }
}
