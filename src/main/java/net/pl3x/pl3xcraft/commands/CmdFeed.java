package net.pl3x.pl3xcraft.commands;

import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.configuration.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdFeed implements TabExecutor {
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
        if (!sender.hasPermission("command.feed")){
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }
        Player player = (Player) sender;
        int foodLevel;
        float satLevel;

        if (args.length < 2){
            // Feed yourself Below
            if (args.length > 1){
                try {
                    foodLevel = Integer.parseInt(args[0]);
                    satLevel = Float.parseFloat(args[0]);
                } catch (NumberFormatException numberFormatException){
                    return false;
                }
            } else {
                foodLevel = 1;
                satLevel = 1;
            }
            if(player.getFoodLevel() + foodLevel >= 21 ) {
                player.setFoodLevel(20);
            } else {
                player.setFoodLevel(player.getFoodLevel() + foodLevel);
            }

            if (player.getSaturation() + satLevel >= 21){
                player.setSaturation(20);
            } else {
                player.setSaturation(player.getSaturation() + satLevel);
            }
            Lang.send(sender, Lang.YOU_FEED_YOURSELF);
            return true;
        }


        // Feed other Player Code
        if (args.length > 1){
            try {
                foodLevel = Integer.parseInt(args[1]);
                satLevel = Float.parseFloat(args[1]);
            } catch (NumberFormatException numberFormatException){
                return false;
            }
        } else {
            foodLevel = 1;
            satLevel = 1;
        }
        if (!sender.hasPermission("command.feed.other")){
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }
        Player otherPlayer = Pl3xCraft.getPlugin().getServer().getPlayer(args[0]);
        if (otherPlayer == null){
            Lang.send(sender, Lang.PLAYER_NOT_FOUND);
            return true;
        }
        Lang.send(sender, Lang.YOU_FEED_PLAYER_X
                .replace("{getFedPlayer}",otherPlayer.getDisplayName()));
        Lang.send(otherPlayer, Lang.PLAYER_X_FEED_YOU
                .replace("{getPlayerFeeding}",player.getDisplayName()));

        if(otherPlayer.getFoodLevel() + foodLevel >= 21 ) {
            otherPlayer.setFoodLevel(20);
        } else {
            otherPlayer.setFoodLevel(otherPlayer.getFoodLevel() + foodLevel);
        }
        if (otherPlayer.getSaturation() + satLevel >= 21){
            otherPlayer.setSaturation(20);
        } else {
            otherPlayer.setSaturation(otherPlayer.getSaturation() + satLevel);
        }
        return true;
    }
}
