package net.pl3x.pl3xcraft.hook;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import net.pl3x.pl3xcraft.Logger;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Vault {
    private static Permission permission = null;
    private static Chat chat = null;

    public static boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServicesManager().getRegistration(Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return permission != null;
    }

    public static boolean setupChat() {
        RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        } else {
            Logger.error("Chat provider is equal to null!");
            return false;
        }
        return chat != null;
    }

    public static boolean hasPermission(OfflinePlayer target, String node) {
        return target.isOnline() ? target.getPlayer().hasPermission(node) : permission.playerHas(null, target, node);
    }

    public static String getPrefix(Player player) {
        StringBuilder prefixes = new StringBuilder();
        for (String groupName : permission.getPlayerGroups(player)) {
            prefixes.append(chat.getGroupPrefix(player.getWorld(), groupName));
        }
        return prefixes.toString();
    }

    public static String getSuffix(Player player) {
        StringBuilder suffixes = new StringBuilder();
        for (String groupName : permission.getPlayerGroups(player)) {
            suffixes.append(chat.getGroupSuffix(player.getWorld(), groupName));
        }
        return suffixes.toString();
    }

    public static String getPrimaryGroup(Player player) {
        return permission.getPrimaryGroup(player);
    }
}
