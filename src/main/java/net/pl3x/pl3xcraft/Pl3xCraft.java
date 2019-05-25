package net.pl3x.pl3xcraft;

import net.pl3x.pl3xcraft.commands.CmdAssign;
import net.pl3x.pl3xcraft.commands.CmdBack;
import net.pl3x.pl3xcraft.commands.CmdBurn;
import net.pl3x.pl3xcraft.commands.CmdClearInventory;
import net.pl3x.pl3xcraft.commands.CmdCoords;
import net.pl3x.pl3xcraft.commands.CmdDelHome;
import net.pl3x.pl3xcraft.commands.CmdDepth;
import net.pl3x.pl3xcraft.commands.CmdEnchant;
import net.pl3x.pl3xcraft.commands.CmdEnchantingTable;
import net.pl3x.pl3xcraft.commands.CmdEnderChest;
import net.pl3x.pl3xcraft.commands.CmdFeed;
import net.pl3x.pl3xcraft.commands.CmdFly;
import net.pl3x.pl3xcraft.commands.CmdFurnace;
import net.pl3x.pl3xcraft.commands.CmdGamemode;
import net.pl3x.pl3xcraft.commands.CmdHarm;
import net.pl3x.pl3xcraft.commands.CmdHat;
import net.pl3x.pl3xcraft.commands.CmdHeal;
import net.pl3x.pl3xcraft.commands.CmdHome;
import net.pl3x.pl3xcraft.commands.CmdHomes;
import net.pl3x.pl3xcraft.commands.CmdInvmod;
import net.pl3x.pl3xcraft.commands.CmdJump;
import net.pl3x.pl3xcraft.commands.CmdKickMe;
import net.pl3x.pl3xcraft.commands.CmdKillAll;
import net.pl3x.pl3xcraft.commands.CmdList;
import net.pl3x.pl3xcraft.commands.CmdPl3xCraft;
import net.pl3x.pl3xcraft.commands.CmdRepair;
import net.pl3x.pl3xcraft.commands.CmdSeen;
import net.pl3x.pl3xcraft.commands.CmdSetHome;
import net.pl3x.pl3xcraft.commands.CmdTeleportAccept;
import net.pl3x.pl3xcraft.commands.CmdTeleportDeny;
import net.pl3x.pl3xcraft.commands.CmdTeleportRequest;
import net.pl3x.pl3xcraft.commands.CmdTeleportRequestAll;
import net.pl3x.pl3xcraft.commands.CmdTeleportRequestHere;
import net.pl3x.pl3xcraft.commands.CmdTeleportToggle;
import net.pl3x.pl3xcraft.commands.CmdTop;
import net.pl3x.pl3xcraft.commands.CmdWorkbench;
import net.pl3x.pl3xcraft.configuration.Config;
import net.pl3x.pl3xcraft.configuration.Data;
import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.configuration.PlayerConfig;
import net.pl3x.pl3xcraft.hook.Vault;
import net.pl3x.pl3xcraft.listener.MOTDListener;
import net.pl3x.pl3xcraft.listener.PlayerListener;
import net.pl3x.pl3xcraft.listener.VillagerListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
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
        Data.getInstance();

        if (!Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            Logger.error("Missing required dependency: Vault");
            return;
        }

        if (!Vault.setupPermissions()) {
            Logger.error("Vault could not find a permissions plugin to hook to!");
            return;
        }

        getServer().getPluginManager().registerEvents(new MOTDListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new VillagerListener(), this);

        getCommand("pl3xcraft").setExecutor(new CmdPl3xCraft());
        getCommand("assign").setExecutor(new CmdAssign());
        getCommand("back").setExecutor(new CmdBack());
        getCommand("clearinventory").setExecutor(new CmdClearInventory());
        getCommand("coords").setExecutor(new CmdCoords());
        getCommand("burn").setExecutor(new CmdBurn());
        getCommand("delhome").setExecutor(new CmdDelHome());
        getCommand("depth").setExecutor(new CmdDepth());
        getCommand("enchant").setExecutor(new CmdEnchant());
        getCommand("enchantingtable").setExecutor(new CmdEnchantingTable());
        getCommand("enderchest").setExecutor(new CmdEnderChest());
        getCommand("feed").setExecutor(new CmdFeed());
        getCommand("fly").setExecutor(new CmdFly());
        getCommand("furnace").setExecutor(new CmdFurnace());
        getCommand("gmadventure").setExecutor(new CmdGamemode(GameMode.ADVENTURE));
        getCommand("gmcreative").setExecutor(new CmdGamemode(GameMode.CREATIVE));
        getCommand("gmspectator").setExecutor(new CmdGamemode(GameMode.SPECTATOR));
        getCommand("gmsurvival").setExecutor(new CmdGamemode(GameMode.SURVIVAL));
        getCommand("home").setExecutor(new CmdHome());
        getCommand("homes").setExecutor(new CmdHomes());
        getCommand("harm").setExecutor(new CmdHarm());
        getCommand("hat").setExecutor(new CmdHat());
        getCommand("heal").setExecutor(new CmdHeal());
        getCommand("invmod").setExecutor(new CmdInvmod());
        getCommand("jump").setExecutor(new CmdJump());
        getCommand("kickme").setExecutor(new CmdKickMe());
        getCommand("killall").setExecutor(new CmdKillAll());
        getCommand("list").setExecutor(new CmdList());
        getCommand("repair").setExecutor(new CmdRepair());
        getCommand("seen").setExecutor(new CmdSeen());
        getCommand("sethome").setExecutor(new CmdSetHome());
        getCommand("teleportaccept").setExecutor(new CmdTeleportAccept());
        getCommand("teleportdeny").setExecutor(new CmdTeleportDeny());
        getCommand("teleportrequest").setExecutor(new CmdTeleportRequest());
        getCommand("teleportrequestall").setExecutor(new CmdTeleportRequestAll());
        getCommand("teleportrequesthere").setExecutor(new CmdTeleportRequestHere());
        getCommand("teleporttoggle").setExecutor(new CmdTeleportToggle());
        getCommand("top").setExecutor(new CmdTop());
        getCommand("workbench").setExecutor(new CmdWorkbench());

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

    public static Pl3xCraft getInstance() {
        return instance;
    }
}
