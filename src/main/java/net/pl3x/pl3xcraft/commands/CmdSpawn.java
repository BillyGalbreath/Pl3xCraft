package net.pl3x.pl3xcraft.commands;

import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.configuration.Data;
import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.task.TeleportSounds;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdSpawn implements TabExecutor {
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

        Player player = (Player) sender;
        if (!player.hasPermission("command.spawn")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        Location spawn = Data.getInstance().getSpawn();
        if (spawn == null) {
            spawn = Bukkit.getWorlds().get(0).getSpawnLocation();
        }

        new TeleportSounds(player.getLocation(), spawn)
                .runTaskLater(Pl3xCraft.getInstance(), 1);

        player.teleportAsync(spawn).thenAccept(result ->
                Lang.send(sender, Lang.SPAWN));


        return true;
    }
}
