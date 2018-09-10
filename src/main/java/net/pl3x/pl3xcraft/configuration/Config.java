package net.pl3x.pl3xcraft.configuration;

import net.pl3x.pl3xcraft.Logger;
import net.pl3x.pl3xcraft.Pl3xCraft;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;

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
    }
}
