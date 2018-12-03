package net.pl3x.pl3xcraft.commands;

import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.configuration.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;
import org.bukkit.entity.Player;

public class CmdFly implements TabExecutor {
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

        if (!sender.hasPermission("command.fly")){
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        Player player = (Player) sender;

        if (args.length > 0){
            if (!sender.hasPermission("command.fly.other")){
                Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
                return true;
            }

            Player otherPlayer = Pl3xCraft.getInstance().getServer().getPlayer(args[0]);

            if (otherPlayer.hasPermission("command.fly.exempt")){
                Lang.send(sender, Lang.PLAYER_EXEMPT
                        .replace("{command}", cmd.getName())
                        .replace("{player}", otherPlayer.getDisplayName()));
                return true;
            }

            if (otherPlayer.getAllowFlight()){
                otherPlayer.setAllowFlight(false);
            } else {
                otherPlayer.setAllowFlight(true);
            }

            String flyStatus = (otherPlayer.getAllowFlight()) ? "on" : "off";

            Lang.send(sender, Lang.TOGGLED_FLIGHT_X_ON_PLAYERX
                    .replace("{flyStatus}", flyStatus)
                    .replace("{getPlayer}", otherPlayer.getDisplayName()));
            Lang.send(otherPlayer, Lang.PLAYERX_TOGGLED_FLIGHT_X
                        .replace("{getPlayer}", player.getDisplayName())
                    .replace("{flyStatus}", flyStatus));
            return true;
        }

        if (player.getAllowFlight()){
            player.setAllowFlight(false);
        } else {
            player.setAllowFlight(true);
        }
        String flyStatus = (player.getAllowFlight()) ? "on" : "off";
        Lang.send(sender, Lang.TOGGLED_FLIGHT_X
                .replace("{flyStatus}", flyStatus));
        return true;
    }
}
