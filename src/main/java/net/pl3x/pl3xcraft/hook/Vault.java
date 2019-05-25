package net.pl3x.pl3xcraft.hook;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Vault {
    private static Permission permission = null;

    public static boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServicesManager().getRegistration(Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return permission != null;
    }

    public static boolean hasPermission(OfflinePlayer target, String node) {
        return target.isOnline() ? target.getPlayer().hasPermission(node) : permission.playerHas(null, target, node);
    }
}
