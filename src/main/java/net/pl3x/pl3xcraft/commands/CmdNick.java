package net.pl3x.pl3xcraft.commands;

import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.configuration.PlayerConfig;
import net.pl3x.pl3xcraft.hook.Vault;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

public class CmdNick implements TabExecutor {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        // tab complete player names only if we have permission
        if (args.length == 1 && sender.hasPermission("command.nick.other")) {
            return Bukkit.getOnlinePlayers().stream()
                    .filter(player -> player.getName().toLowerCase().startsWith(args[0].toLowerCase()))
                    .map(HumanEntity::getName).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Execute this command in another thread
        // This is for LuckPerms to handle OfflinePlayer permission checks
        new BukkitRunnable() {
            @Override
            public void run() {
                execute(sender, cmd, label, args);
            }
        }.runTaskAsynchronously(Pl3xCraft.getPlugin());
        return true;
    }

    private void execute(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("command.nick")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return;
        }

        // need at least a new nick, remove, or other's name to work with
        if (args.length < 1) {
            Lang.send(sender, cmd.getDescription());
            Lang.send(sender, cmd.getUsage());
            return;
        }

        // set nick for self
        if (args.length == 1) {
            // only players can set for self
            if (!(sender instanceof Player)) {
                Lang.send(sender, Lang.PLAYER_COMMAND);
                return;
            }

            Player target = (Player) sender;
            PlayerConfig config = PlayerConfig.getConfig(target);
            String nick = args[0].trim();

            // remove own nick
            if (nick.equalsIgnoreCase("remove")) {
                config.setNick(null);
                target.setDisplayName(null);
                Lang.send(sender, Lang.NICK_REMOVED_SELF);
                return;
            }

            // set own new nick
            config.setNick(nick);
            String newNick = config.getNick();
            target.setDisplayName(newNick);
            Lang.send(sender, Lang.NICK_SET_SELF
                    .replace("{nick}", newNick));
            return;
        }

        // check if can set other's nicks
        if (!sender.hasPermission("command.nick.other")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return;
        }

        // get other player (support offline editing of nicks)
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (target == null) {
            Lang.send(sender, Lang.PLAYER_NOT_FOUND);
            return;
        }

        // check other's exempt permission
        if (Vault.hasPermission(target, "command.nick.exempt")) {
            Lang.send(sender, Lang.PLAYER_EXEMPT
                    .replace("{command}", cmd.getName())
                    .replace("{player}", target.getName()));
            return;
        }

        PlayerConfig config = PlayerConfig.getConfig(target);
        String nick = args[1].trim();

        // remove other's nick
        if (nick.equalsIgnoreCase("remove")) {
            config.setNick(null);
            Lang.send(sender, Lang.NICK_REMOVED_OTHER
                    .replace("{player}", target.getName()));
            if (target.isOnline()) {
                // remove target's nick since they are online now
                target.getPlayer().setDisplayName(null);
                // inform target since they are online
                Lang.send(target.getPlayer(), Lang.NICK_REMOVED_BY_OTHER
                        .replace("{player}", sender.getName()));
            }
            return;
        }

        // set other's new nick
        config.setNick(nick);
        String newNick = config.getNick();
        Lang.send(sender, Lang.NICK_SET_OTHER
                .replace("{player}", target.getName())
                .replace("{nick}", newNick));
        if (target.isOnline()) {
            // set target's nick since they are online now
            target.getPlayer().setDisplayName(newNick);
            // inform target since they are online
            Lang.send(target.getPlayer(), Lang.NICK_SET_BY_OTHER
                    .replace("{player}", sender.getName())
                    .replace("{nick}", newNick));
        }
    }
}
