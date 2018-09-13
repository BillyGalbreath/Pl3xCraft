package net.pl3x.pl3xcraft.util;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class ItemUtil {
    public static String getItemName(@Nonnull ItemStack itemStack) {
        return getItemName(itemStack.getType());
    }

    public static String getItemName(@Nonnull Material material) {
        return material.name().toLowerCase().replace("_", " ");
    }

    // Gets Block player is curses is looking at
    public static Block getTarget(Player player){
        return player.getTargetBlock(null, 300);
    }
}
