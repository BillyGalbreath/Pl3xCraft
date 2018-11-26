package net.pl3x.pl3xcraft.configuration;

import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.hook.Vault;
import net.pl3x.pl3xcraft.request.Request;
import net.pl3x.pl3xcraft.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerConfig extends YamlConfiguration {
    private static final Map<OfflinePlayer, PlayerConfig> configs = new HashMap<>();

    public static PlayerConfig getConfig(OfflinePlayer player) {
        synchronized (configs) {
            return configs.computeIfAbsent(player, k -> new PlayerConfig(player));
        }
    }

    public static void remove(OfflinePlayer player) {
        synchronized (configs) {
            configs.remove(player);
        }
    }

    public static void removeAll() {
        synchronized (configs) {
            configs.clear();
        }
    }

    private final File file;
    private final Object saveLock = new Object();
    private final OfflinePlayer player;
    private Request request;
    private Player replyTarget;

    private PlayerConfig(OfflinePlayer player) {
        super();
        this.player = player;
        this.file = new File(Pl3xCraft.getPlugin().getDataFolder(),
                "userdata" + File.separator + player.getUniqueId() + ".yml");
        reload();
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    private void reload() {
        synchronized (saveLock) {
            try {
                load(file);
            } catch (Exception ignore) {
            }
        }
    }

    private void save() {
        synchronized (saveLock) {
            try {
                save(file);
            } catch (Exception ignore) {
            }
        }
    }

    public Location getHome(String name) {
        if (get("home." + name) == null) {
            return null;
        }
        World world = Bukkit.getWorld(getString("home." + name + ".world", ""));
        if (world == null) {
            return null;
        }
        double x = getDouble("home." + name + ".x");
        double y = getDouble("home." + name + ".y");
        double z = getDouble("home." + name + ".z");
        float pitch = (float) getDouble("home." + name + ".pitch");
        float yaw = (float) getDouble("home." + name + ".yaw");
        return new Location(world, x, y, z, yaw, pitch);
    }

    public void setHome(String name, Location location) {
        if (location == null) {
            set("home." + name, null);
            save();
            return;
        }
        set("home." + name + ".world", location.getWorld().getName());
        set("home." + name + ".x", location.getX());
        set("home." + name + ".y", location.getY());
        set("home." + name + ".z", location.getZ());
        set("home." + name + ".pitch", location.getPitch());
        set("home." + name + ".yaw", location.getYaw());
        save();
    }

    // Billys kay/value swap
    // ----> WORKS LEAVE IT!
    public List<String> getAssignCommand(Material material) {
        ConfigurationSection section = getConfigurationSection("assign");
        return section == null ? Collections.EMPTY_LIST : section.getStringList(material.name());
    }

    // WORKS LEAVE IT!!
    public void setAssignCommand(Material material, List<String> commands) {
        if (commands == null) {
            set("assign." + material, null);
            save();
            return;
        }
        set("assign." + material, commands);
        save();
    }

    public int getCount() {
        ConfigurationSection cfg = getConfigurationSection("home");
        if (cfg == null) {
            return 0;
        }
        return cfg.getKeys(false).size();
    }

    public List<String> getMatchingHomeNames(String name) {
        ConfigurationSection section = getConfigurationSection("home");
        if (section == null) {
            return null;
        }
        List<String> list = section.getValues(false).keySet().stream()
                .filter(home -> home.toLowerCase().startsWith(name.toLowerCase()))
                .collect(Collectors.toList());
        if (player.getBedSpawnLocation() != null) {
            list.add("bed");
        }
        return list;
    }

    public Map<String, Location> getHomeData() {
        ConfigurationSection section = getConfigurationSection("home");
        if (section == null) {
            return null;
        }
        Map<String, Location> map = new HashMap<>();
        for (String key : section.getValues(false).keySet()) {
            map.put(key, getHome(key));
        }
        map.put("bed", player.getBedSpawnLocation());
        return map;
    }

    public List<String> getHomeList() {
        ConfigurationSection section = getConfigurationSection("home");
        if (section == null) {
            return null;
        }
        List<String> list = new ArrayList<>(section.getValues(false).keySet());
        list.add((player.getBedSpawnLocation() == null ? "&m" : "") + "bed&r");
        return list;
    }

    public int getHomesLimit() {
        if (Vault.hasPermission(player, "command.sethome.limit.*")) {
            return -1;
        }
        int limit = 0;
        for (int i = 0; i < 1024; i++) {
            if (Vault.hasPermission(player, "command.sethome.limit." + i) && i > limit) {
                limit = i;
            }
        }
        return limit;
    }

    public boolean allowTeleports() {
        return getBoolean("allow-teleports", true);
    }

    public void setAllowTeleports(boolean allowTeleports) {
        set("allow-teleports", allowTeleports);
        save();
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void setNick(String nick) {
        set("nickname", nick);
        save();
    }

    public String getNick() {
        return ChatUtil.checkColorPerms(player, "command.nick", getString("nickname"));
    }

    public boolean isMuted() {
        return !Vault.hasPermission(player, "exempt.mute") && // player is exempt from being muted
                getBoolean("muted", false);
    }

    public void setMuted(boolean isMuted) {
        assert !Vault.hasPermission(player, "exempt.mute") : "Cannot mute an exempt player!";
        set("muted", isMuted);
        save();
    }

    public boolean isSpying() {
        return Vault.hasPermission(player, "command.spy") && // not allowed to spy
                getBoolean("spy-mode", false);
    }

    public void setSpying(boolean isSpying) {
        assert !isSpying || !Vault.hasPermission(player, "command.spy") : "Cannot enable spy on that player!";
        set("spy-mode", isSpying);
        save();
    }

    public void setReplyTarget(Player target) {
        replyTarget = target;
    }

    public void removeReplyTarget(Player target) {
        if (replyTarget == target) {
            replyTarget = null;
        }
    }

    public Player getReplyTarget() {
        return replyTarget;
    }
}
