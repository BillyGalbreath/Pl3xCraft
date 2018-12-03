package net.pl3x.pl3xcraft.commands;

import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.configuration.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdEnderChest implements TabExecutor {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)){
            Lang.send(sender, Lang.PLAYER_NOT_FOUND);
            return true;
        }

        if (!sender.hasPermission("command.enderchest")){
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        Player player= (Player)sender;
        if (args.length > 0){
            if (!sender.hasPermission("command.enderchest.other")){
                Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
                return true;
            }

            Player otherPlayer = Pl3xCraft.getInstance().getServer().getPlayer(args[0]);
            player.openInventory(otherPlayer.getEnderChest());
            Lang.send(sender, Lang.OPENED_OTHER_PLAYER_ENDERCHEST
                    .replace("{getPlayer}", otherPlayer.getDisplayName()));
            return true;
        }
        player.openInventory(player.getEnderChest());
        Lang.send(sender, Lang.OPENED_YOUR_ENDERCHEST);
        return true;
    }
}
