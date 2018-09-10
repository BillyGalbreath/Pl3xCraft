package net.pl3x.pl3xcraft.commands;

import net.pl3x.pl3xcraft.configuration.Config;
import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CmdEnchant implements TabExecutor {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1){
            return Arrays.stream(Enchantment.values())
                            .filter(enchantment -> enchantment.getName().toLowerCase().startsWith(args[1].toLowerCase()))
                            .map(Enchantment::getName)
                            .collect(Collectors.toList());
        }
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
            Lang.send(sender, Lang.ENCHANTMENT_DESCRIPTION
                    .replace("{getDescription}", cmd.getDescription()));
            Lang.send(sender, Lang.ENCHANTMENT_USAGE
                    .replace("{getUsage}", cmd.getUsage()));
            return true;
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
                            if (Config.UNSAFE_ENCHANTMENTS){
                                inHand.addUnsafeEnchantment(enchantment, level);
                            } else {
                                inHand.addEnchantment(enchantment, level);
                            }
                            continue;
                        }
                        inHand.removeEnchantment(enchantment);
                    }
                    Lang.send(sender, Lang.REMOVED_ALL_ENCHANTMENTS
                            .replace("{getItemName}", ItemUtil.getItemName(inHand)));
                } else if (level == MAX_LEVEL) {
                    for (Enchantment enchantment : Enchantment.values()){
                        if (Config.UNSAFE_ENCHANTMENTS){
                            inHand.addUnsafeEnchantment(enchantment, enchantment.getMaxLevel());
                        } else {
                            inHand.addEnchantment(enchantment, enchantment.getMaxLevel());
                        }
                    }
                    Lang.send(sender, Lang.ADDED_ALL_ENCHANTMENTS_TO_MAX
                            .replace("{getItemInhand}",ItemUtil.getItemName(inHand)));
                } else  if (level == MAX_INT){
                    for (Enchantment enchantment : Enchantment.values()){
                        if (Config.UNSAFE_ENCHANTMENTS){
                            inHand.addUnsafeEnchantment(enchantment, 30);
                        } else {
                            inHand.addEnchantment(enchantment, 30);
                        }
                    }
                    Lang.send(sender, Lang.ADDED_ALL_ENCHANTMENTS_TO_30
                            .replace("{getItemInhand}",ItemUtil.getItemName(inHand)));
                } else {
                    for (Enchantment enchantment : Enchantment.values()){
                        if (Config.UNSAFE_ENCHANTMENTS){
                            inHand.addUnsafeEnchantment(enchantment, level);
                        } else {
                            inHand.addEnchantment(enchantment, level);
                        }

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
            if (Config.UNSAFE_ENCHANTMENTS){
                inHand.addUnsafeEnchantment(addedEnchantment, addedEnchantment.getMaxLevel());
            } else {
                inHand.addEnchantment(addedEnchantment, addedEnchantment.getMaxLevel());
            }
            Lang.send(sender, Lang.ADD_X_ENCHANMENT_TO_MAX
                    .replace("{getEnchantment}", addedEnchantment.getName().toLowerCase().replace("_", " "))
                    .replace("{getItemInhand}", ItemUtil.getItemName(inHand))
                    .replace("{getMaxLevel}", Integer.toString(addedEnchantment.getMaxLevel())));
        } else if (level == MAX_INT) {
            if (Config.UNSAFE_ENCHANTMENTS){
                inHand.addUnsafeEnchantment(addedEnchantment, 30);
            } else {
                inHand.addEnchantment(addedEnchantment, 30);
            }
            Lang.send(sender, Lang.ADD_X_ENCHANMENT_TO_30
                    .replace("{getEnchantment}", addedEnchantment.getName().toLowerCase().replace("_", " "))
                    .replace("{getItemInhand}", ItemUtil.getItemName(inHand)));
        } else {
            if (Config.UNSAFE_ENCHANTMENTS){
                inHand.addUnsafeEnchantment(addedEnchantment, level);
            } else {
                inHand.addEnchantment(addedEnchantment, level);
            }
            Lang.send(sender, Lang.ADD_X_ENCHANMENT_TO_X
                    .replace("{getEnchantment}", addedEnchantment.getName().toLowerCase().replace("_", " "))
                    .replace("{getItemInhand}", ItemUtil.getItemName(inHand))
                    .replace("{getLevel}", Integer.toString(level)));
        }
        return true;
    }

    private void sendEnchantmentList(CommandSender sender){
        String enchantments = "&7" + String.join("&d, &7",
                Arrays.stream(Enchantment.values())
                    .map(Enchantment::getName)
                    .collect(Collectors.toList()));
        Lang.send(sender, "&d===============\n&8Enchantment List\n&d===============\n" + enchantments + "\n&d===============");
    }
}
