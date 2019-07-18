package net.pl3x.pl3xcraft.listener;

import net.pl3x.pl3xcraft.configuration.Config;
import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.purpur.event.ExecuteCommandEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class CannedResponseListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCommand(ExecuteCommandEvent event) {
        if (event.getArgs().length == 0 && !event.getLabel().contains(":")) {
            String response = Config.CANNED_RESPONSES.get(event.getCommand().getLabel().toLowerCase());
            if (response != null && !response.isEmpty()) {
                Lang.send(event.getSender(), response);
                event.setCancelled(true);
            }
        }
    }
}
