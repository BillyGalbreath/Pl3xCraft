package net.pl3x.pl3xcraft.util;

import net.pl3x.pl3xcraft.configuration.Config;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

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


    public static void repairItem(Player player){
        ItemStack inHand = (!Config.REPAIR_MAIN_HAND) ? player.getInventory().getItemInOffHand() : player.getInventory().getItemInMainHand();

        ItemStack newRepairedItem = new ItemStack(inHand.getType(), inHand.getAmount());
        newRepairedItem.addEnchantments(inHand.getEnchantments());
        ItemMeta newRepairedMeta = inHand.getItemMeta();
        //newRepairedItem.setDurability((short) 0); // Deprecated
        if (newRepairedMeta instanceof Damageable) {
            Damageable damagedMeta = (Damageable) newRepairedMeta;
            damagedMeta.setDamage(0);
        }
        if (inHand.hasItemMeta()){
            if (inHand.getItemMeta().hasDisplayName()){
                newRepairedMeta.setDisplayName(inHand.getItemMeta().getDisplayName());
            }
            if (inHand.getItemMeta().hasLore()){
                newRepairedMeta.setLore(inHand.getItemMeta().getLore());
            }
        }
        newRepairedItem.setItemMeta(newRepairedMeta);

        if (!Config.REPAIR_MAIN_HAND) {
            player.getInventory().setItemInOffHand(newRepairedItem);
        } else {
            player.getInventory().setItemInMainHand(newRepairedItem);
        }
        player.updateInventory();
    }

    public static void repairAllItems(Player player){
        ItemStack[] playerInventory = player.getInventory().getContents();

        Arrays.stream(playerInventory)
                .filter(unWantedItems -> !unWantedItems.equals(null) && !unWantedItems.equals(Material.AIR) && (unWantedItems instanceof Damageable) )
                .forEach(wantedItems -> {
                    //if (!(wantedItems instanceof Damageable)) { return; }
                    Damageable damagedItem = (Damageable) wantedItems;
                    damagedItem.setDamage(0);
                    ItemStack repairedItem = new ItemStack(wantedItems);
                    repairedItem.addEnchantments(wantedItems.getEnchantments());
                    ItemMeta repairedMeta = wantedItems.getItemMeta();
                    if (wantedItems.hasItemMeta()){
                        if (wantedItems.getItemMeta().hasDisplayName()) { repairedMeta.setDisplayName(wantedItems.getItemMeta().getDisplayName()); }
                        if (wantedItems.getItemMeta().hasLore()) { repairedMeta.setLore(wantedItems.getItemMeta().getLore()); }
                    }
                    wantedItems.setItemMeta(repairedMeta);
                });
        player.updateInventory();
    }
}
