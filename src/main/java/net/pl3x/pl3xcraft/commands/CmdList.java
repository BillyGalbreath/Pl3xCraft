package net.pl3x.pl3xcraft.commands;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.configuration.Lang;
import org.apache.commons.lang.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class CmdList implements TabExecutor {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be bull");
        Validate.notNull(label, "Labels cannot be null");

        return ImmutableList.of();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("command.list")){
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        int maxPlayers = Pl3xCraft.getInstance().getServer().getMaxPlayers();
        StringBuilder online = new StringBuilder();
        Collection<? extends Player> allPlayers = Pl3xCraft.getInstance().getServer().getOnlinePlayers();

        String playerSizeString = Integer.toString(allPlayers.size());
        String maxPlayersString = Integer.toString(maxPlayers);

        for (Player player : allPlayers){
            if (sender instanceof Player && !((Player) sender).canSee(player))
                continue;

            if (online.length() > 0){
                online.append("&7,&r ");
            }

            online.append(player.getDisplayName());
        }

        Lang.send(sender, Lang.PLAYERS_ONLINE_NUM_TITLE
                .replace("{getPlayerSize}", playerSizeString)
                .replace("{getMaxPlayers}", maxPlayersString) );
        Lang.send(sender, Lang.PLAYERS_ONLINE_GROUP_LIST
                .replace("{getOnline}", online.toString()) );

        return true;
    }


}
