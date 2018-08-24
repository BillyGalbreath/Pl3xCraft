package net.pl3x.pl3xcraft.commands;

import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.configuration.Config;
import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.configuration.PlayerConfig;
import net.pl3x.pl3xcraft.hook.Vault;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdHome implements TabExecutor {
    private final Pl3xCraft plugin;

    public CmdHome(Pl3xCraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                return PlayerConfig.getConfig(plugin, (Player) sender).getMatchingHomeNames(args[0]);
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
        if (!(sender instanceof Player)) {
            Lang.send(sender, Lang.PLAYER_COMMAND);
            return true;
        }

        if (!sender.hasPermission("command.home")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        Player player = (Player) sender;
        PlayerConfig config = PlayerConfig.getConfig(plugin, player);
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
                return true;
            }
            if (home == null || home.isEmpty()) {
                Lang.send(sender, Lang.HOME_NOT_SET);
                return true;
            }
        } else {
            home = args[0];
        }

        if (args.length > 1) {
            if (!sender.hasPermission("command.home.other")) {
                Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
                return true;
            }

            //noinspection deprecation (fucking bukkit)
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            if (target == null) {
                Lang.send(sender, Lang.PLAYER_NOT_FOUND);
                return true;
            }

            if (Vault.hasPermission(target, "command.home.exempt")) {
                Lang.send(sender, Lang.HOME_EXEMPT);
                return true;
            }

            config = PlayerConfig.getConfig(plugin, target);

            if (config == null) {
                Lang.send(sender, Lang.PLAYER_NOT_FOUND);
                return true;
            }

            // disable limit check if traveling to someone else's home
            limit = -1;
        }

        Location homeLoc = home.equalsIgnoreCase("bed") ?
                player.getBedSpawnLocation() : config.getHome(home);
        if (homeLoc == null) {
            Lang.send(sender, Lang.HOME_DOES_NOT_EXIST);
            return true;
        }

        int count = config.getCount();
        if (limit >= 0 && count > limit) {
            Lang.send(sender, Lang.PLEASE_DELETE_HOMES
                    .replace("{limit}", Integer.toString(limit))
                    .replace("{count}", Integer.toString(count)));
            return true;
        }

        if (Config.USE_TELEPORT_SOUNDS) {
            Location from = player.getLocation();
            from.getWorld().playSound(from, Config.SOUND_FROM, 1.0F, 1.0F);
            homeLoc.getWorld().playSound(homeLoc, Config.SOUND_TO, 1.0F, 1.0F);
        }

        player.teleport(homeLoc);

        Lang.send(sender, Lang.HOME
                .replace("{home}", home));
        return true;
    }
}
