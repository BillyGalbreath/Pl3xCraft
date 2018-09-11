package net.pl3x.pl3xcraft.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class ItemUtil {
    public static String getItemName(@Nonnull ItemStack itemStack) {
        return getItemName(itemStack.getType());
    }

    public static String getItemName(@Nonnull Material material) {
        return material.name().toLowerCase().replace("_", " ");
    }
}
