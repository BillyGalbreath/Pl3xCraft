package net.pl3x.pl3xcraft.hook;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.util.DiscordUtil;

public class DiscordSRVHook {
    public void sendToDiscord(String message) {
        DiscordUtil.sendMessage(DiscordSRV.getPlugin().getMainTextChannel(), DiscordUtil.strip(message));
    }
}
