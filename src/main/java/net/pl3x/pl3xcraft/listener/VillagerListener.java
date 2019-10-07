package net.pl3x.pl3xcraft.listener;

import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

public class VillagerListener implements Listener {
    @EventHandler
    public void onVillagerAcquireTrades(VillagerAcquireTradeEvent event) {
        /*
        MerchantRecipe recipe = event.getRecipe();

        // replace emerald ingredients with gold ingot
        recipe.setIngredients(recipe.getIngredients().stream()
                .peek(ingredient -> {
                    if (ingredient.getType() == Material.EMERALD) {
                        ingredient.setType(Material.GOLD_INGOT);
                    }
                })
                .collect(Collectors.toList()));

        // make a completely new recipe to change the result
        if (recipe.getResult().getType() == Material.EMERALD) {
            ItemStack gold = new ItemStack(Material.GOLD_INGOT);
            gold.setAmount(recipe.getResult().getAmount());

            MerchantRecipe newRecipe = new MerchantRecipe(gold, recipe.getUses(), recipe.getMaxUses(), recipe.hasExperienceReward());
            newRecipe.setIngredients(recipe.getIngredients());

            event.setRecipe(newRecipe);
        }
        */
    }

    @EventHandler
    public void onVillagerOpenTrades(InventoryOpenEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof Villager)) {
            return;
        }
        Villager villager = (Villager) holder;
        for (MerchantRecipe recipe : villager.getRecipes()) {
            if (recipe.getResult().getType() == Material.EMERALD) {
                return; // has emerald trade
            }
            for (ItemStack ingredient : recipe.getIngredients()) {
                if (ingredient.getType() == Material.EMERALD) {
                    return; // has emerald trade
                }
            }
        }
        villager.resetOffers();
    }
}
