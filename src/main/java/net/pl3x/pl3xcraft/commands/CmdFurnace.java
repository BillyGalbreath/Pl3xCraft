package net.pl3x.pl3xcraft.commands;

import java.util.HashMap;
import java.util.List;
import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.util.ItemUtil;
import org.bukkit.block.Furnace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceInventory;

public class CmdFurnace implements TabExecutor {
    private HashMap<Player, Furnace> furnaceDateBase = new HashMap<Player, Furnace>();

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

        if (!sender.hasPermission("command.furnace")){
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        if (args.length < 1 || args.length > 1){
            Lang.send(sender, Lang.FURNACE_INFO
                    .replace("{getDescription}", cmd.getDescription())
                    .replace("{getUsage}", cmd.getUsage()));
            return true;
        }

        Player player = (Player) sender;
        String furnaceOption = args[0].toLowerCase();

        if (furnaceOption.equals("set")){
            if (!(ItemUtil.getTarget(player).getState() instanceof Furnace)){
                Lang.send(sender, Lang.NOT_FURNACE);
                return true;
            }

            Furnace furnace = (Furnace) ItemUtil.getTarget(player).getState();

            furnaceDateBase.put(player, furnace);
            Lang.send(sender, Lang.FURNACE_SET);
        } else if (furnaceOption.equals("show")){
            if (!furnaceDateBase.containsKey(player)){
                Lang.send(sender, Lang.NO_FURNACE_SET);
                return true;
            }

            Furnace getFurnace = furnaceDateBase.get(player);

            if (!(getFurnace.getBlock().getState() instanceof Furnace)){
                Lang.send(sender, Lang.FURNACE_GONE);
                return true;
            }

            getFurnace = (Furnace) getFurnace.getBlock().getState();
            FurnaceInventory furnaceInventory = getFurnace.getInventory();
            player.openInventory(furnaceInventory);
            Lang.send(sender, Lang.FURNACE_INVENTORY_OPENED);
        } else {
            Lang.send(sender, Lang.FURNACE_INFO
                    .replace("{getDescription}", cmd.getDescription())
                    .replace("{getUsage}", cmd.getUsage()));
        }
        return true;
    }
}
