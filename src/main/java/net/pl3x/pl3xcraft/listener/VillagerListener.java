package net.pl3x.pl3xcraft.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.stream.Collectors;

public class VillagerListener implements Listener {
    @EventHandler
    public void onVillagerAcquireTrades(VillagerAcquireTradeEvent event) {
        /*MerchantRecipe recipe = event.getRecipe();

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
        }*/
    }
}
