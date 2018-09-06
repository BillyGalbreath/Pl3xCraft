package net.pl3x.pl3xcraft.listener;

import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.configuration.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.util.CachedServerIcon;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

public class MOTDListener implements Listener {
    private final Pl3xCraft plugin;

    public MOTDListener(Pl3xCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerPing(ServerListPingEvent event) {
        String motd = Data.getInstance().getRandomMOTD();
        if (motd != null && !motd.isEmpty()) {
            event.setMotd(ChatColor.translateAlternateColorCodes('&', motd));
        }

        File dir = new File(plugin.getDataFolder(), "server-icon");
        if (!dir.exists()) {
            // noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
            return;
        }

        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".png"));
        if (files == null || files.length < 1) {
            return;
        }

        try {
            CachedServerIcon icon = Bukkit.loadServerIcon(files[ThreadLocalRandom.current().nextInt(files.length)]);
            if (icon != null) {
                event.setServerIcon(icon);
            }
        } catch (Exception ignore) {
        }
    }
}
