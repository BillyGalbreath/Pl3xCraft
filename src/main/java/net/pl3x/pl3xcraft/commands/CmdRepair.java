package net.pl3x.pl3xcraft.commands;

import java.util.Arrays;
import net.pl3x.pl3xcraft.configuration.Config;
import net.pl3x.pl3xcraft.configuration.Lang;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;
import org.bukkit.inventory.ItemStack;

public class CmdRepair implements TabExecutor {

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

        Player target = args.length > 0 ? org.bukkit.Bukkit.getPlayer(args[0]) : (Player) sender;


        if (!sender.hasPermission("command.repair" + (target != sender ? ".other" : "")) ) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        if (!Config.ALLOW_REPAIR){
            Lang.send(sender, Lang.REPAIR_NOT_ALLOWED);
            return true;
        }

        ItemStack[] playerInventory = target.getInventory().getContents();
        String itemsDamaged = "";

        if (args.length > 0){
            if (target.hasPermission("command.repair.exempt")){
                Lang.send(sender, Lang.PLAYER_EXEMPT
                        .replace("{getCommand}", cmd.getName())
                        .replace("{getPlayer}", target.getDisplayName()));
                return true;
            }

            if (!target.isOnline()){
                Lang.send(sender, Lang.PLAYER_NOT_ONLINE);
                return true;
            }

            /* ** Java 7 For Loop **
            for (ItemStack aPlayerInventory : playerInventory){
                if (aPlayerInventory != null && aPlayerInventory.getType() != Material.AIR && aPlayerInventory.getDurability() != ((short) 0) ){
                    aPlayerInventory.setDurability((short) 0);
                    aPlayerInventory.setItemMeta(null);
                }
             */

            // Java 8 For Lambda
            /* .setDurability() is Depricated
            Arrays.stream(playerInventory)
                    .filter(unwantedPlayerInventory -> !unwantedPlayerInventory.equals(null) && !unwantedPlayerInventory.equals(Material.AIR) && !unwantedPlayerInventory.equals(0) )
                    .forEach(aPlayerInventory -> aPlayerInventory.setDurability((short) 0) );
            */

            Arrays.stream(playerInventory)
                    .filter(unwantedPlayerInventory -> !unwantedPlayerInventory.equals(null) && !unwantedPlayerInventory.equals(Material.AIR) && !unwantedPlayerInventory.equals(0) )
                    .forEach(aPlayerInventory -> aPlayerInventory.setItemMeta(null) );

            if (!itemsDamaged.equals("")){
                Lang.send(sender, Lang.ITEMS_REPAIRED
                        .replace("{getItem}", itemsDamaged) );
                Lang.send(target, Lang.ITEMS_REPAIRED_OTHER
                        .replace("{getItem}", itemsDamaged)
                        .replace("{getPlayer}", sender.getName()));
                return true;
            }

            Lang.send(sender, Lang.NO_REPAIR_NEEDED);
            return true;
        }

        return true;
    }
}
