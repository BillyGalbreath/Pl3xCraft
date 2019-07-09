package net.pl3x.pl3xcraft.commands;

import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.task.TeleportSounds;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class CmdJump implements TabExecutor {
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

        if (!sender.hasPermission("command.jump")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        Player player = (Player) sender;
        Block target = player.getTargetBlock(Collections.singleton(Material.AIR), 120);

        if (target == null) {
            Lang.send(sender, Lang.NO_TARGET_BLOCK);
            return true;
        }

        Location targetLoc = target.getLocation();
        targetLoc.setYaw(player.getLocation().getYaw());
        targetLoc.setPitch(player.getLocation().getPitch());
        targetLoc.add(0.5, 1, 0.5);

        new TeleportSounds(targetLoc, player.getLocation())
                .runTaskLater(Pl3xCraft.getInstance(), 1);

        player.teleport(targetLoc);
        Lang.send(sender, Lang.TELEPORTING_JUMP);
        return true;
    }
}
