package net.pl3x.pl3xcraft.commands;

import com.sun.org.apache.regexp.internal.RE;
import net.pl3x.pl3xcraft.configuration.Lang;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

public class CmdHat implements TabExecutor {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)){
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        if (!sender.hasPermission("command.hat")){
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        Player player = (Player) sender;

        if (args.length > 0 && (args[0].contains("remove") || args[0].contains("off") )){
            final PlayerInventory inventory = player.getInventory();
            final ItemStack getHelmet = inventory.getHelmet();
            if (getHelmet == null || getHelmet.getType() == Material.AIR){
                Lang.send(sender, Lang.WEARING_NOTHING);
                return true;
            }
            final ItemStack setAirHelmet = new ItemStack(Material.AIR);
            inventory.setHelmet(setAirHelmet);
            player.getInventory().addItem(getHelmet);
            Lang.send(sender, Lang.REMOVED_HELMET);
            return true;
        }

        if (player.getInventory().getItemInMainHand().getType() == Material.AIR){
            Lang.send(sender, Lang.NOTHING_IN_HAND);
            return true;
        }

        final ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType().getMaxDurability() != 0){
            Lang.send(sender, Lang.CANNOT_WEAR_THIS_ITEM);
            return true;
        }

        final PlayerInventory inventory = player.getInventory();
        final ItemStack getHelmet = inventory.getHelmet();
        inventory.removeItem(itemInHand);
        inventory.setHelmet(itemInHand);
        inventory.setItemInMainHand(getHelmet);
        Lang.send(sender, Lang.HELMET_SET);
        return true;
    }
}
