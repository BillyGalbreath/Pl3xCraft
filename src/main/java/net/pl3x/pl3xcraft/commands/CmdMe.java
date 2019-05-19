package net.pl3x.pl3xcraft.commands;

import java.util.UUID;
import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.configuration.Config;
import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.configuration.LangCooldown;
import net.pl3x.pl3xcraft.configuration.PlayerConfig;
import net.pl3x.pl3xcraft.util.CooldownUtil;
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

        String meCooldown = "";
        Player target = (Player) sender;
        UUID targetUUID = target.getUniqueId();

        if (CooldownUtil.isInCooldown(targetUUID, meCooldown)){
            int cooldownTimer = CooldownUtil.getTimeLeft(targetUUID, meCooldown);
            String cooldownTimerString = Integer.toString(cooldownTimer);

            if (cooldownTimerString != null && cooldownTimer != 0) {

                if (Lang.isEmpty(cooldownTimerString)){
                    Lang.send(sender, "seconds is empty");
                    return true;
                }

                Lang.send(sender, LangCooldown.COOLDOWN_TIMER_LEFT
                        .replace("{getCooldownSeconds}", cooldownTimerString)
                        .replace("{getCommand}", command.getName()));
                return true;
            }
        }

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Lang.ME_FORMAT
                .replace("{sender}", sender.getName().equals("CONSOLE") ? "Console" : sender.getName())
                .replace("{message}", String.join(" ", Arrays.asList(args)))));

        if (Pl3xCraft.getDiscordHook() != null) {
            Pl3xCraft.getDiscordHook().sendToDiscord(Lang.ME_DISCORD_FORMAT
                    .replace("{sender}", sender.getName().equals("CONSOLE") ? "Console" : sender.getName())
                    .replace("{message}", String.join(" ", Arrays.asList(args))));
        }

       if (!sender.hasPermission("command.me.exempt")) {
            CooldownUtil cooldownUtil = new CooldownUtil(targetUUID, meCooldown, Config.ME_COOLDOWN_SECONDS);
            cooldownUtil.start();
       }

        return true;
    }
}
