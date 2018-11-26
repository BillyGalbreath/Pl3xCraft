package net.pl3x.pl3xcraft.commands;

import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.configuration.PlayerConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CmdMe implements TabExecutor {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("command.me")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        if (args.length == 0) {
            Lang.send(sender, Lang.NO_MESSAGE_SPECIFIED);
            return true;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (PlayerConfig.getConfig(player).isMuted()) {
                Lang.send(sender, Lang.YOU_ARE_MUTED);
                return true;
            }
        }

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Lang.ME_FORMAT
                .replace("{sender}", sender.getName().equals("CONSOLE") ? "Console" : sender.getName())
                .replace("{message}", String.join(" ", Arrays.asList(args)))));

        if (Pl3xCraft.getDiscordSRVHook() != null) {
            Pl3xCraft.getDiscordSRVHook().sendToDiscord(Lang.ME_DISCORD_FORMAT
                    .replace("{sender}", sender.getName().equals("CONSOLE") ? "Console" : sender.getName())
                    .replace("{message}", String.join(" ", Arrays.asList(args))));
        }
        return true;
    }
}
