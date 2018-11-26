package net.pl3x.pl3xcraft.commands;

import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.configuration.PlayerConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CmdReply implements TabExecutor {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Lang.send(sender, Lang.PLAYER_COMMAND);
            return true;
        }

        if (!sender.hasPermission("command.reply")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        Player player = (Player) sender;
        PlayerConfig senderConfig = PlayerConfig.getConfig(player);
        Player target = senderConfig.getReplyTarget();
        if (target == null) {
            Lang.send(sender, Lang.REPLY_NO_TARGET);
            return true;
        }

        if (args.length < 1) {
            Lang.send(sender, Lang.NO_MESSAGE_SPECIFIED);
            return true;
        }

        String message = String.join(" ", Arrays.asList(args));
        if (Lang.isEmpty(message)) {
            Lang.send(sender, Lang.NO_MESSAGE_SPECIFIED);
            return true;
        }

        if (senderConfig.isMuted()) {
            Lang.send(sender, Lang.YOU_ARE_MUTED);
            return true;
        }

        PlayerConfig targetConfig = PlayerConfig.getConfig(target);

        senderConfig.setReplyTarget(target);
        targetConfig.setReplyTarget(player);

        Lang.send(target, Lang.TELL_TARGET
                .replace("{sender}", sender.getName())
                .replace("{target}", target.getName())
                .replace("{message}", message));
        Lang.send(sender, Lang.TELL_SENDER
                .replace("{sender}", sender.getName())
                .replace("{target}", target.getName())
                .replace("{message}", message));
        Bukkit.getOnlinePlayers().stream()
                .filter(spy -> PlayerConfig.getConfig(spy).isSpying())
                .filter(spy -> spy != player && spy != target)
                .forEach(spy -> Lang.send(spy, Lang.SPY_PREFIX + Lang.TELL_SPY
                        .replace("{sender}", sender.getName())
                        .replace("{target}", target.getName())
                        .replace("{message}", message)));
        return true;
    }
}
