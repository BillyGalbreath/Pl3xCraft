package net.pl3x.pl3xcraft.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemUtil {
    public static String getItemName(ItemStack itemStack) {
        return getItemName(itemStack.getType());
    }

    public static String getItemName(Material material) {
        return material.name().toLowerCase().replace("_", " ");
    }
}
