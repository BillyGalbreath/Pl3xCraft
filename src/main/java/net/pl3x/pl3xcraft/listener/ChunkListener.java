package net.pl3x.pl3xcraft.listener;

import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.purpur.event.ChunkTooLargeEvent;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

public class ChunkListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onChunkTooLarge(ChunkTooLargeEvent event) {
        new BukkitRunnable() {
            public void run() {
                String message = "&7[&4WARNING&7] &cDetected a " + (event.isSaving() ? "save" : "load") + " of an over-sized chunk! Possible duplication attempt!";

                Location loc = event.getLocation();
                if (loc != null) {
                    message += "\n&cNear coords&3: &e" + loc.getWorld().getName() + " " + loc.getX() + "&7,&e" + loc.getY() + "&7,&e" + loc.getZ();

                    List<String> nearby = loc.getNearbyPlayers(32).stream()
                            .map(HumanEntity::getName)
                            .collect(Collectors.toList());
                    message += "\n&cPlayers nearby&3: &e" + (String.join("&7, &e", nearby));
                } else {
                    message += "\n&cNear coords&e: &e" + (event.getChunkX() << 4) + "&7,&e?&e,&e" + (event.getChunkZ() << 4);
                }

                Lang.broadcast(message);
            }
        }.runTaskLater(Pl3xCraft.getInstance(), 20L);
    }
}
