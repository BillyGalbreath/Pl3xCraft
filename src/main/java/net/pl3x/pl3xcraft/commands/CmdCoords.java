package net.pl3x.pl3xcraft.commands;

import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.configuration.Lang;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdCoords implements TabExecutor {
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
        if (!sender.hasPermission("command.coords")){
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }
        Player player = (Player) sender;
        if (args.length < 1){
            Location location = player.getLocation();
            Lang.send(player, Lang.YOUR_COORDS);
            Lang.send(player, Lang.X_COORDS
                    .replace("{getX}", Double.toString(location.getX()) ));
            Lang.send(player, Lang.Y_COORDS
                    .replace("{getY}", Double.toString(location.getY()) ));
            Lang.send(player, Lang.Z_COORDS
                    .replace("{getZ}", Double.toString(location.getZ()) ));
            Lang.send(player, Lang.PITCH_COORDS
                    .replace("{getPitch}", Float.toString(location.getPitch()) ));
            Lang.send(player, Lang.YAW_COORDS
                    .replace("{getYaw}", Float.toString(location.getYaw()) ));
            Lang.send(player, Lang.WORLD_COORDS
                    .replace("{getWorld}", location.getWorld().getName() ));
            return true;
        }
        if (!sender.hasPermission("command.coords.other")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }
        Player otherPlayer = Pl3xCraft.getPlugin().getServer().getPlayer(args[0]);
        Location location = otherPlayer.getLocation();

        Lang.send(player, Lang.OTHER_PLAYER_COORDS
                .replace("{otherPlayer}", otherPlayer.getDisplayName() ));
        Lang.send(player, Lang.X_COORDS
                .replace("{getX}", Double.toString(location.getX()) ));
        Lang.send(player, Lang.Y_COORDS
                .replace("{getY}", Double.toString(location.getY()) ));
        Lang.send(player, Lang.Z_COORDS
                .replace("{getZ}", Double.toString(location.getZ()) ));
        Lang.send(player, Lang.PITCH_COORDS
                .replace("{getPitch}", Float.toString(location.getPitch()) ));
        Lang.send(player, Lang.YAW_COORDS
                .replace("{getYaw}", Float.toString(location.getYaw()) ));
        Lang.send(player, Lang.WORLD_COORDS
                .replace("{getWorld}", location.getWorld().getName() ));
        return true;
    }
}
