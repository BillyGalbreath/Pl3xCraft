package net.pl3x.pl3xcraft.commands;

import com.google.common.collect.ImmutableList;
import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.configuration.Lang;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import java.util.Collection;
import java.util.List;

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
        if (!sender.hasPermission("command.list")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        int maxPlayers = Pl3xCraft.getInstance().getServer().getMaxPlayers();
        StringBuilder sb = new StringBuilder();
        Collection<? extends Player> allPlayers = Pl3xCraft.getInstance().getServer().getOnlinePlayers();

        String playerSizeString = Integer.toString(allPlayers.size());
        String maxPlayersString = Integer.toString(maxPlayers);

        Player player = sender instanceof Player ? (Player) sender : null;
        String color = ChatColor.getLastColors(ChatColor.translateAlternateColorCodes('&', Lang.PLAYERS_ONLINE_GROUP_LIST));
        for (Player online : allPlayers) {
            if (isVanished(online) || (player != null && !player.canSee(online))) {
                continue; // do not list vanished players
            }

            if (sb.length() > 0) {
                sb.append("&7,");
            }

            if (online.isAfk()) {
                sb.append("&e[&3AFK&e]");
            }

            sb.append(color).append(" ").append(online.getDisplayName());
        }

        Lang.send(sender, Lang.PLAYERS_ONLINE_NUM_TITLE
                .replace("{getPlayerSize}", playerSizeString)
                .replace("{getMaxPlayers}", maxPlayersString));
        Lang.send(sender, Lang.PLAYERS_ONLINE_GROUP_LIST
                .replace("{getOnline}", sb.toString()));

        return true;
    }

    private boolean isVanished(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
    }
}
