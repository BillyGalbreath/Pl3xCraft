package net.pl3x.pl3xcraft.listener;

import com.destroystokyo.paper.event.entity.PreCreatureSpawnEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class EntitySpawnListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onEntitySpawn(PreCreatureSpawnEvent event) {
        if (event.getReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {
            switch (event.getType()) {
                case COD:
                case PUFFERFISH:
                case SALMON:
                case TROPICAL_FISH:
                    event.setCancelled(true);
                    event.setShouldAbortSpawn(true);
            }
        }
    }
}
