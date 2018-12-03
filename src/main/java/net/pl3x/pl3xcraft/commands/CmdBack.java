package net.pl3x.pl3xcraft.commands;

import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.task.TeleportSounds;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CmdBack implements TabExecutor {
    private static final HashMap<UUID, Location> backdb = new HashMap<>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Lang.send(sender, Lang.PLAYER_COMMAND);
            return true;
        }

        if (!sender.hasPermission("command.back")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        Player player = (Player) sender;
        Location back = getPreviousLocation(player);

        if (back == null) {
            Lang.send(sender, Lang.NO_BACK_LOCATION);
            return true;
        }

        new TeleportSounds(back, player.getLocation())
                .runTaskLater(Pl3xCraft.getInstance(), 1);

        player.teleportAsync(back).thenAccept(result ->
                Lang.send(sender, Lang.TELEPORTING_BACK));
        return true;
    }

    private static Location getPreviousLocation(Player player) {
        return backdb.get(player.getUniqueId());
    }

    public static void setPreviousLocation(Player player, Location location) {
        if (location == null) {
            backdb.remove(player.getUniqueId());
            return;
        }
        backdb.put(player.getUniqueId(), location);
    }

    public static void clearBackLocations() {
        backdb.clear();
    }
}
