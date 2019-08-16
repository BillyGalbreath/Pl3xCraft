package net.pl3x.pl3xcraft.listener;

import de.myzelyam.api.vanish.VanishAPI;
import net.pl3x.purpur.event.PlayerAFKEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SuperVanishListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onAFKEvent(PlayerAFKEvent event) {
        if (VanishAPI.isInvisible(event.getPlayer())) {
            event.setBroadcastMsg(null);
        }
    }
}
