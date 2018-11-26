package net.pl3x.pl3xcraft.commands;

import java.util.List;
import net.pl3x.pl3xcraft.configuration.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class CmdInvmod implements TabExecutor {

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

        if (!sender.hasPermission("command.invmod")){
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        if (args.length < 1){
            Lang.send(sender, Lang.NO_PLAYER_SPECIFIED);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null){
            Lang.send(sender, Lang.PLAYER_NOT_FOUND);
            return true;
        }

        if (target.hasPermission("command.invmod.exempt")){
            Lang.send(sender, Lang.PLAYER_EXEMPT
                    .replace("{command}", cmd.getName())
                    .replace("{player}", target.getDisplayName()));
            return true;
        }

        ((Player) sender).openInventory(target.getInventory());
        Lang.send(sender, Lang.OPENED_PLAYER_INVENTORY
                .replace("{player}", target.getDisplayName())
                .replace("{possessive}", target.getDisplayName().toLowerCase().endsWith("s") ? "'" : "'s"));

        return true;
    }
}
