package net.pl3x.pl3xcraft.listener;

import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.purpur.event.ChunkTooLargeEvent;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ChunkListener implements Listener {
    private static final int RADIUS = 32;

    private final Map<String, Location> RECENTLY_LOGGED_OFF = new HashMap<>();
    private final Map<String, Location> RECENTLY_TELEPORTED_FROM = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChunkTooLarge(ChunkTooLargeEvent event) {
        new BukkitRunnable() {
            public void run() {
                String message = "&7[&4WARNING&7]\n&cDetected a " + (event.isSaving() ? "save" : "load") + " of an over-sized chunk!\n&cPossible duplication attempt!";

                Location loc = event.getLocation();
                if (loc != null) {
                    message += "\n&cNear coords&3: &e" + loc.getWorld().getName() + " " + loc.getX() + "&7,&e" + loc.getY() + "&7,&e" + loc.getZ();

                    Set<String> nearby = loc.getNearbyPlayers(RADIUS).stream()
                            .map(HumanEntity::getName)
                            .collect(Collectors.toSet());

                    nearby.addAll(RECENTLY_LOGGED_OFF.entrySet().stream()
                            .filter(entry -> loc.distanceSquared(entry.getValue()) < RADIUS * RADIUS)
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toSet()));

                    nearby.addAll(RECENTLY_TELEPORTED_FROM.entrySet().stream()
                            .filter(entry -> loc.distanceSquared(entry.getValue()) < RADIUS * RADIUS)
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toSet()));

                    message += "\n&cPlayers recently nearby&3: &e" + (String.join("&7, &e", nearby));
                } else {
                    message += "\n&cNear coords&e: &e" + (event.getChunkX() << 4) + "&7,&e?&e,&e" + (event.getChunkZ() << 4);
                }

                Lang.broadcast(message);
            }
        }.runTaskLater(Pl3xCraft.getInstance(), 20L);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        RECENTLY_LOGGED_OFF.put(event.getPlayer().getName(), event.getPlayer().getLocation());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        RECENTLY_TELEPORTED_FROM.put(event.getPlayer().getName(), event.getFrom());
    }
}
