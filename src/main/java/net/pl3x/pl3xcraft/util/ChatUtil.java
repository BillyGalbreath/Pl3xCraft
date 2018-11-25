package net.pl3x.pl3xcraft.util;

import net.pl3x.pl3xcraft.hook.Vault;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

public class ChatUtil {
    public static String checkColorPerms(OfflinePlayer player, String str) {
        if (str != null) {
            str = ChatColor.translateAlternateColorCodes('&', str);
            if (!Vault.hasPermission(player, "command.nick.color")) {
                str = str.replaceAll("(?i)\u00A7[0-9a-f]", "");
            }
            if (!Vault.hasPermission(player, "command.nick.style")) {
                str = str.replaceAll("(?i)\u00A7[l-o]", "");
            }
            if (!Vault.hasPermission(player, "command.nick.magic")) {
                str = str.replaceAll("(?i)\u00A7[k]", "");
            }
        }
        return str;
    }
}
