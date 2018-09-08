package net.pl3x.pl3xcraft.commands;

import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CmdEnchant implements TabExecutor {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)){
            Lang.send(sender, Lang.PLAYER_COMMAND);
            return true;
        }

        if (!sender.hasPermission("command.enchant")){
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        if (args.length < 1){
            sendEnchantmentList(sender);
            return true; // Might need to be false
        }

        Player player = (Player) sender;
        ItemStack inHand = player.getItemInHand();

        if (inHand == null || inHand.getType() == Material.AIR){
            Lang.send(sender, Lang.CAN_NOT_AIR);
            return true;
        }

        Enchantment addedEnchantment = Enchantment.getByName(args[0].toUpperCase());
        int MAX_LEVEL = -1;
        int MAX_INT = -2;
        int REMOVE = 0;

        if (addedEnchantment == null){
            if (args[0].equalsIgnoreCase("all")){
                int level;
                if (args.length < 2){
                    level = -1;
                } else if (args.length > 1 && args[1].equalsIgnoreCase("max")){
                    level = -2;
                } else {
                    try {
                        level = Integer.parseInt(args[1]);
                    } catch (NumberFormatException numberFormatException){
                        Lang.send(sender, Lang.INVALID_NUMBER);
                        return true;
                    }
                    if (level < 0){
                        Lang.send(sender, Lang.LEVEL_BELOW_MIN);
                        return true;
                    }
                    if (level > 30){
                        Lang.send(sender, Lang.LEVEL_ABOVE_MAX);
                        return true;
                    }
                }
                if (level == REMOVE){
                    for (Enchantment enchantment : Enchantment.values()){
                        if (!inHand.containsEnchantment(enchantment)){
                            continue;
                        }
                        inHand.removeEnchantment(enchantment);
                    }
                    Lang.send(sender, Lang.REMOVED_ALL_ENCHANTMENTS
                            .replace("{getItemName}", ItemUtil.getItemName(inHand)));
                } else if (level == MAX_LEVEL) {
                    for (Enchantment enchantment : Enchantment.values()){
                        inHand.addUnsafeEnchantment(enchantment, enchantment.getMaxLevel());
                    }
                    Lang.send(sender, Lang.ADDED_ALL_ENCHANTMENTS_TO_MAX
                            .replace("{getItemInhand}",ItemUtil.getItemName(inHand)));
                } else  if (level == MAX_INT){
                    for (Enchantment enchantment : Enchantment.values()){
                        inHand.addUnsafeEnchantment(enchantment, 30);
                    }
                    Lang.send(sender, Lang.ADDED_ALL_ENCHANTMENTS_TO_30
                            .replace("{getItemInhand}",ItemUtil.getItemName(inHand)));
                } else {
                    for (Enchantment enchantment : Enchantment.values()){
                        inHand.addUnsafeEnchantment(enchantment, level);
                    }
                    Lang.send(sender, Lang.ADDED_ALL_ENCHANTMENTS_TO_X
                            .replace("{getItemInhand}", ItemUtil.getItemName(inHand))
                            .replace("{getLevel}", Integer.toString(level)));
                }
                return true;
            }
            sendEnchantmentList(sender);
            Lang.send(sender, Lang.NOT_ENCHANTMENT);
            return true;
        }
        int level;
        if (args.length < 2){
            level = -1;
        } else if (args.length > 1 && args[1].equalsIgnoreCase("max")){
            level = -2;
        } else {
            try {
                level = Integer.parseInt(args[1]);
            } catch (NumberFormatException numberFormantException){
                Lang.send(sender, Lang.INVALID_NUMBER);
                return true;
            }
            if (level < 0){
                Lang.send(sender, Lang.LEVEL_BELOW_MIN);
                return true;
            }
            if (level > 30){
                Lang.send(sender, Lang.LEVEL_ABOVE_MAX);
                return true;
            }
        }

        if (level == REMOVE){
            if (!inHand.containsEnchantment(addedEnchantment)){
                Lang.send(sender, Lang.ENCHANTMENT_NOT_FOUND
                        .replace("{getItemInhand}", ItemUtil.getItemName(inHand))
                        .replace("{getEnchantment}", addedEnchantment.getName().toLowerCase().replace("_", " ")));
                return true;
            }
            inHand.removeEnchantment(addedEnchantment);
            Lang.send(sender, Lang.ADD_X_ENCHANMENT
                    .replace("{getEnchantment}", addedEnchantment.getName().toLowerCase().replace("_", " "))
                    .replace("{getItemInhand}", ItemUtil.getItemName(inHand)));
        } else if (level == MAX_LEVEL) {
            inHand.addUnsafeEnchantment(addedEnchantment, addedEnchantment.getMaxLevel());
            Lang.send(sender, Lang.ADD_X_ENCHANMENT_TO_MAX
                    .replace("{getEnchantment}", addedEnchantment.getName().toLowerCase().replace("_", " "))
                    .replace("{getItemInhand}", ItemUtil.getItemName(inHand))
                    .replace("{getMaxLevel}", Integer.toString(addedEnchantment.getMaxLevel())));
        } else if (level == MAX_INT) {
            inHand.addUnsafeEnchantment(addedEnchantment, 30);
            Lang.send(sender, Lang.ADD_X_ENCHANMENT_TO_30
                    .replace("{getEnchantment}", addedEnchantment.getName().toLowerCase().replace("_", " "))
                    .replace("{getItemInhand}", ItemUtil.getItemName(inHand)));
        } else {
            inHand.addUnsafeEnchantment(addedEnchantment, level);
            Lang.send(sender, Lang.ADD_X_ENCHANMENT_TO_X
                    .replace("{getEnchantment}", addedEnchantment.getName().toLowerCase().replace("_", " "))
                    .replace("{getItemInhand}", ItemUtil.getItemName(inHand))
                    .replace("{getLevel}", Integer.toString(level)));
        }
        return true;
    }

    private void sendEnchantmentList(CommandSender sender){
        StringBuilder stringBuilder = new StringBuilder();
        for (Enchantment enchantment : Enchantment.values()){
            stringBuilder.append("&7");
            stringBuilder.append(enchantment.getName());
            stringBuilder.append(enchantment != null ? "&d,  " : "");
        }
        Lang.send(sender, "&d===============\n&7Enchantment List\n&d===============\n" + stringBuilder.substring(0, stringBuilder.length() - 4) + "\n&d===============");
    }
}
