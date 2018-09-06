package net.pl3x.pl3xcraft.commands;

import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.configuration.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdClearInventory implements TabExecutor {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Lang.send(sender, Lang.PLAYER_COMMAND);
            return true;
        }

        if (!sender.hasPermission("command.clearinventory")){
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        if (args.length > 0){
            if (!sender.hasPermission("command.clearinventory.other")){
                Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
                return true;
            }
            Player player = Pl3xCraft.getPlugin().getServer().getPlayer(args[0]);
            if (player == null){
                Lang.send(sender, Lang.PLAYER_NOT_FOUND);
                return true;
            }
            Lang.send(sender, Lang.PLAYER_INVENTORY_CLEARED
                    .replace("{playerCleared}", player.getDisplayName()));
            Lang.send(player, Lang.INVENTORY_CLEARED_BY_OTHER
                    .replace("{playerClearedBy}", ((Player) sender).getDisplayName()));
            player.getInventory().clear();
            return true;
        }
        Player player = (Player) sender;
        player.getInventory().clear();
        Lang.send(sender, Lang.INVENTORY_CLEARED);
        return true;
    }
}
