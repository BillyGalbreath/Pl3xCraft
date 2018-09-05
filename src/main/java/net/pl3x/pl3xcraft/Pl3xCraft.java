package net.pl3x.pl3xcraft;

import net.pl3x.pl3xcraft.commands.*;
import net.pl3x.pl3xcraft.configuration.Config;
import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.configuration.PlayerConfig;
import net.pl3x.pl3xcraft.hook.Vault;
import net.pl3x.pl3xcraft.listener.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Pl3xCraft extends JavaPlugin {
    private static Pl3xCraft instance;

    public Pl3xCraft() {
        instance = this;
    }

    @Override
    public void onEnable() {
        Config.reload(this);
        Lang.reload(this);

        if (!Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            Logger.error("Missing required dependency: Vault");
            return;
        }

        if (!Vault.setupPermissions()) {
            Logger.error("Vault could not find a permissions plugin to hook to!");
            return;
        }

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        getCommand("pl3xcraft").setExecutor(new CmdPl3xCraft(this));
        getCommand("back").setExecutor(new CmdBack(this));
        getCommand("delhome").setExecutor(new CmdDelHome(this));
        getCommand("home").setExecutor(new CmdHome(this));
        getCommand("homes").setExecutor(new CmdHomes(this));
        getCommand("jump").setExecutor(new CmdJump(this));
        getCommand("sethome").setExecutor(new CmdSetHome(this));
        getCommand("teleportaccept").setExecutor(new CmdTeleportAccept(this));
        getCommand("teleportdeny").setExecutor(new CmdTeleportDeny(this));
        getCommand("teleportrequest").setExecutor(new CmdTeleportRequest(this));
        getCommand("teleportrequestall").setExecutor(new CmdTeleportRequestAll(this));
        getCommand("teleportrequesthere").setExecutor(new CmdTeleportRequestHere(this));
        getCommand("teleporttoggle").setExecutor(new CmdTeleportToggle(this));
        getCommand("top").setExecutor(new CmdTop(this));
        getCommand("assign").setExecutor(new CmdAssign(this));

        Logger.info(getName() + " v" + getDescription().getVersion() + " enabled!");
    }

    @Override
    public void onDisable() {
        CmdBack.clearBackLocations();
        PlayerConfig.removeAll();

        Logger.info(getName() + " disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&4" + getName() + " is disabled. See console log for more information."));
        return true;
    }

    public static Pl3xCraft getPlugin() {
        return instance;
    }
}
