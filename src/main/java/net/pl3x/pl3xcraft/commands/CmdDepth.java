package net.pl3x.pl3xcraft.commands;

import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.configuration.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdDepth implements TabExecutor {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)){
            Lang.send(sender, Lang.PLAYER_COMMAND);
            return true;
        }

        if (!sender.hasPermission("command.depth")){
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            final int depth = player.getLocation().getBlockY() - 63;
            if (depth > 0) {
                Lang.send(sender, Lang.ABOVE_SEA_LEVEL
                        .replace("{getPlayer}", "You're")
                        .replace("{aboveSeaLevel}", Integer.toString(depth))
                        .replace("{sUp}", ((depth > 1) ? "s" : "")));
            } else if (depth < 0) {
                Lang.send(sender, Lang.BELOW_SEA_LEVEL
                        .replace("{getPlayer}", "You're")
                        .replace("{belowSeaLevel}", Integer.toString(depth))
                        .replace("{sDown}", ((depth < -1) ? "s" : "")));
            } else {
                Lang.send(sender, Lang.AT_SEA_LEVEL
                        .replace("{getPlayer}", "You're"));
            }
            return true;
        }

        if (!sender.hasPermission("command.depth.other")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        Player otherPlayerDepth = Pl3xCraft.getInstance().getServer().getPlayer(args[0]);
        final int depth = otherPlayerDepth.getLocation().getBlockY() - 63;

        if (depth > 0) {
            Lang.send(sender, Lang.ABOVE_SEA_LEVEL
                    .replace("{getPlayer}", otherPlayerDepth.getDisplayName() + " is ")
                    .replace("{aboveSeaLevel}", Integer.toString(depth))
                    .replace("{sUp}", ((depth > 1) ? "s" : "")));
        } else if (depth < 0) {
            Lang.send(sender, Lang.BELOW_SEA_LEVEL
                    .replace("{getPlayer}", otherPlayerDepth.getDisplayName() + " is ")
                    .replace("{belowSeaLevel}", Integer.toString(depth))
                    .replace("{sDown}", ((depth < -1) ? "s" : "")));
        } else {
            Lang.send(sender, Lang.AT_SEA_LEVEL
                    .replace("{getPlayer}", otherPlayerDepth.getDisplayName() + " is "));
        }

        return true;
    }
}
