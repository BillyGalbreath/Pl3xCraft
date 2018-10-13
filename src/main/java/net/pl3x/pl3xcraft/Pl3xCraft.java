package net.pl3x.pl3xcraft;

import net.pl3x.pl3xcraft.commands.CmdAssign;
import net.pl3x.pl3xcraft.commands.CmdBack;
import net.pl3x.pl3xcraft.commands.CmdBroadcast;
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
import net.pl3x.pl3xcraft.commands.CmdKillAll;
import net.pl3x.pl3xcraft.commands.CmdNick;
import net.pl3x.pl3xcraft.commands.CmdPl3xCraft;
import net.pl3x.pl3xcraft.commands.CmdSetHome;
import net.pl3x.pl3xcraft.commands.CmdSetSpawn;
import net.pl3x.pl3xcraft.commands.CmdSpawn;
import net.pl3x.pl3xcraft.commands.CmdTeleportAccept;
import net.pl3x.pl3xcraft.commands.CmdTeleportDeny;
import net.pl3x.pl3xcraft.commands.CmdTeleportRequest;
import net.pl3x.pl3xcraft.commands.CmdTeleportRequestAll;
import net.pl3x.pl3xcraft.commands.CmdTeleportRequestHere;
import net.pl3x.pl3xcraft.commands.CmdTeleportToggle;
import net.pl3x.pl3xcraft.commands.CmdTop;
import net.pl3x.pl3xcraft.configuration.Config;
import net.pl3x.pl3xcraft.configuration.Data;
import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.configuration.PlayerConfig;
import net.pl3x.pl3xcraft.hook.Vault;
import net.pl3x.pl3xcraft.listener.MOTDListener;
import net.pl3x.pl3xcraft.listener.PlayerListener;
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

        getServer().getPluginManager().registerEvents(new MOTDListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        getCommand("pl3xcraft").setExecutor(new CmdPl3xCraft(this));
        getCommand("assign").setExecutor(new CmdAssign());
        getCommand("back").setExecutor(new CmdBack(this));
        getCommand("clearinventory").setExecutor(new CmdClearInventory());
        getCommand("coords").setExecutor(new CmdCoords());
        getCommand("broadcast").setExecutor(new CmdBroadcast());
        getCommand("burn").setExecutor(new CmdBurn());
        getCommand("delhome").setExecutor(new CmdDelHome(this));
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
        getCommand("home").setExecutor(new CmdHome(this));
        getCommand("homes").setExecutor(new CmdHomes(this));
        getCommand("harm").setExecutor(new CmdHarm());
        getCommand("hat").setExecutor(new CmdHat());
        getCommand("heal").setExecutor(new CmdHeal());
        getCommand("invmod").setExecutor(new CmdInvmod());
        getCommand("jump").setExecutor(new CmdJump(this));
        getCommand("killall").setExecutor(new CmdKillAll());
        getCommand("nick").setExecutor(new CmdNick());
        getCommand("sethome").setExecutor(new CmdSetHome(this));
        getCommand("setspawn").setExecutor(new CmdSetSpawn());
        getCommand("spawn").setExecutor(new CmdSpawn(this));
        getCommand("teleportaccept").setExecutor(new CmdTeleportAccept(this));
        getCommand("teleportdeny").setExecutor(new CmdTeleportDeny(this));
        getCommand("teleportrequest").setExecutor(new CmdTeleportRequest(this));
        getCommand("teleportrequestall").setExecutor(new CmdTeleportRequestAll(this));
        getCommand("teleportrequesthere").setExecutor(new CmdTeleportRequestHere(this));
        getCommand("teleporttoggle").setExecutor(new CmdTeleportToggle(this));
        getCommand("top").setExecutor(new CmdTop(this));

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
