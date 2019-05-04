package net.pl3x.pl3xcraft.commands;

import java.util.List;
import javax.swing.LookAndFeel;
import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.configuration.PlayerConfig;
import net.pl3x.pl3xcraft.hook.Vault;
import net.pl3x.pl3xcraft.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CmdSeen implements TabExecutor {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // LuckPerms does not like new Java features
        // Executing in another thread
        new BukkitRunnable() {
            public void run() {
                execute(sender, command, label, args);
            }
            }.runTaskAsynchronously(Pl3xCraft.getInstance());
        return true;
    }

    private void execute(CommandSender sender, Command command, String label, String[] args){
        if (!sender.hasPermission("command.seen")){
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return;
        }

        if (args.length < 1){
            Lang.send(sender, Lang.NO_PLAYER_SPECIFIED);
            return;
        }

        OfflinePlayer targetOffline = Bukkit.getOfflinePlayer(args[0]);

        if (targetOffline == null) {
            Lang.send(sender, Lang.NO_PLAYER_FOUND
                    .replace("{getPlayer}", targetOffline.getName()));
            return;
        }

        if (Vault.hasPermission(targetOffline, "command.seen.exempt")){
            Lang.send(sender, Lang.PLAYER_EXEMPT
                    .replace("{command}", command.getName())
                    .replace("{getPlayer}", targetOffline.getName()));
            return;
        }

        if (targetOffline.isOnline()){
            Lang.send(sender, Lang.PLAYER_ONLINE
                    .replace("{getPlayer}", targetOffline.getName()));
            return;
        }

        PlayerConfig targetConfig = PlayerConfig.getConfig(targetOffline);
        long seen = targetConfig.getLong("seen");
        String lastSeen = TimeUtil.formatDateDiff(seen);


        if (targetConfig == null){
            Lang.send(sender, Lang.NO_PLAYER_FOUND
                    .replace("{getPlayer}", targetOffline.getName()));
            return;
        }

        if (targetConfig.get("seen") == null){
            Lang.send(sender, Lang.NO_RECORDED_TIME
                    .replace("{getPlayer}", targetOffline.getName()));
            return;
        }

        Lang.send(sender, Lang.PLAYER_LAST_SEEN
                    .replace("{getPlayer}", targetOffline.getName())
                    .replace("{getTime}", lastSeen.trim()));
        return;
    }
}
