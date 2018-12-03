package net.pl3x.pl3xcraft.commands;

import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.configuration.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdBurn implements TabExecutor {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("command.burn")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }
        if (args.length == 0) {
            return false;
        }
        Player playerBurned = Pl3xCraft.getInstance().getServer().getPlayer(args[0]);
        if ((playerBurned == null)) {
            Lang.send(sender, Lang.PLAYER_NOT_FOUND);
            return true;
        }
        int burnTime;
        if (args.length > 1) {
            try {
                burnTime = Integer.parseInt(args[1]);
            } catch (NumberFormatException numberFormatException) {
                return false;
            }
        } else {
            burnTime = 5;
        }
        if (sender.hasPermission("command.burn.exempt")) {
            Lang.send(sender, Lang.PLAYER_CANT_BURN);
            return true;
        }
        playerBurned.setFireTicks(burnTime * 20);
        String theBurner = (sender instanceof Player) ? ((Player) sender).getDisplayName() : "CONSOLE";
        Lang.send(playerBurned, Lang.PLAYER_HAS_BURNED_YOU
                .replace("{playerBurner}", theBurner));
        Lang.send(sender, Lang.YOU_BURNED_PLAYER
                .replace("{burnedPlayer}", playerBurned.getDisplayName()));
        return true;
    }
}
