package net.pl3x.pl3xcraft.configuration;

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/*
 *
 * TODO: Global config setting
 * TODO: Add cooldown to all applicable commands
 *
 * */

public class LangCooldown {

    public static String COOLDOWN_TIMER_LEFT;

    public static void reload(JavaPlugin plugin){
        String langCooldownFile = Config.LANGUAGE_COOLDOWN_FILE;
        File configFile = new File(plugin.getDataFolder(), langCooldownFile);
        if (!configFile.exists()){
            plugin.saveResource(Config.LANGUAGE_COOLDOWN_FILE, false);
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        COOLDOWN_TIMER_LEFT = config.getString("cooldown-timer-left", "&dYou must wait &7{getCooldownSeconds}&d seconds before using &7{getCommand}&d again.");
    }
}
