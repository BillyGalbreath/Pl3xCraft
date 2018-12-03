package net.pl3x.pl3xcraft.commands;

import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.configuration.PlayerConfig;
import net.pl3x.pl3xcraft.hook.Vault;
import net.pl3x.pl3xcraft.task.TeleportSounds;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

public class CmdHome implements TabExecutor {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                return PlayerConfig.getConfig((Player) sender).getMatchingHomeNames(args[0]);
            }
            if (args.length == 2) {
                return Bukkit.getOnlinePlayers().stream()
                        .filter(player -> player.getName().toLowerCase().startsWith(args[1].toLowerCase()))
                        .map(Player::getName).collect(Collectors.toList());
            }
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
        }.runTaskAsynchronously(Pl3xCraft.getInstance());
        return true;
    }

    private void execute(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Lang.send(sender, Lang.PLAYER_COMMAND);
            return;
        }

        if (!sender.hasPermission("command.home")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return;
        }

        Player player = (Player) sender;
        PlayerConfig config = PlayerConfig.getConfig(player);
        int limit = config.getHomesLimit();
        String home = null;
        if (args.length == 0) {
            int count = config.getCount();
            if (count == 0) {
                // do not count the bed
                if (player.getBedSpawnLocation() != null) {
                    home = "bed";
                }
            } else if (count == 1) {
                home = config.getConfigurationSection("home").getKeys(false)
                        .stream().findFirst().orElse(null);
            } else {
                Lang.send(sender, Lang.SPECIFY_HOME
                        .replace("{home-list}", String.join(", ", config.getHomeList())));
                return;
            }
            if (home == null || home.isEmpty()) {
                Lang.send(sender, Lang.HOME_NOT_SET);
                return;
            }
        } else {
            home = args[0];
        }

        if (args.length > 1) {
            if (!sender.hasPermission("command.home.other")) {
                Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
                return;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            if (target == null) {
                Lang.send(sender, Lang.PLAYER_NOT_FOUND);
                return;
            }

            if (Vault.hasPermission(target, "command.home.exempt")) {
                Lang.send(sender, Lang.HOME_EXEMPT);
                return;
            }

            config = PlayerConfig.getConfig(target);

            if (config == null) {
                Lang.send(sender, Lang.PLAYER_NOT_FOUND);
                return;
            }

            // disable limit check if traveling to someone else's home
            limit = -1;
        }

        Location homeLoc = home.equalsIgnoreCase("bed") ?
                player.getBedSpawnLocation() : config.getHome(home);
        if (homeLoc == null) {
            Lang.send(sender, Lang.HOME_DOES_NOT_EXIST);
            return;
        }

        int count = config.getCount();
        if (limit >= 0 && count > limit) {
            Lang.send(sender, Lang.PLEASE_DELETE_HOMES
                    .replace("{limit}", Integer.toString(limit))
                    .replace("{count}", Integer.toString(count)));
            return;
        }

        new TeleportSounds(homeLoc, player.getLocation())
                .runTaskLater(Pl3xCraft.getInstance(), 1);

        String homeName = home;
        player.teleportAsync(homeLoc).thenAccept(result ->
                Lang.send(sender, Lang.HOME
                        .replace("{home}", homeName))
        );
    }
}
