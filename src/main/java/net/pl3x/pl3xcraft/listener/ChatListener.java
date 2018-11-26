package net.pl3x.pl3xcraft.listener;

import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.configuration.PlayerConfig;
import net.pl3x.pl3xcraft.hook.Vault;
import net.pl3x.pl3xcraft.util.ChatUtil;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player sender = event.getPlayer();
        PlayerConfig config = PlayerConfig.getConfig(sender);

        if (config.isMuted()) {
            Lang.send(sender, Lang.YOU_ARE_MUTED);
            event.setCancelled(true);
            return;
        }

        event.setFormat(ChatColor.translateAlternateColorCodes('&',
                Lang.CHAT_FORMAT
                        .replace("{sender}", "%1$s")
                        .replace("{message}", "%2$s")
                        .replace("{prefix}", Vault.getPrefix(sender))
                        .replace("{suffix}", Vault.getSuffix(sender))
                        .replace("{group}", WordUtils.capitalizeFully(Vault.getPrimaryGroup(sender)))
                        .replace("{world}", sender.getWorld().getName())
                )
        );

        event.setMessage(ChatUtil.checkColorPerms(sender, "chat", event.getMessage()));
    }
}
