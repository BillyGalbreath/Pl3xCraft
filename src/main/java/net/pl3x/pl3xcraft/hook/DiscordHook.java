package net.pl3x.pl3xcraft.hook;

import net.pl3x.bukkit.discord4bukkit.D4BPlugin;
import org.bukkit.entity.Player;

public class DiscordHook {
    public void sendToDiscord(String message) {
        D4BPlugin.getInstance().getBot().sendMessageToDiscord(message);
    }

    public void sendToDiscord(String message, boolean blocking) {
        D4BPlugin.getInstance().getBot().sendMessageToDiscord(message, blocking);
    }

    public void sendToDiscord(Player player, String message) {
        D4BPlugin.getInstance().getBot().sendMessageToDiscord(player, message);
    }
}
