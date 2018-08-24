package net.pl3x.pl3xcraft.configuration;

import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.hook.Vault;
import net.pl3x.pl3xcraft.request.Request;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerConfig extends YamlConfiguration {
    private static final Map<OfflinePlayer, PlayerConfig> configs = new HashMap<>();

    public static PlayerConfig getConfig(Pl3xCraft plugin, OfflinePlayer player) {
        synchronized (configs) {
            return configs.computeIfAbsent(player, k -> new PlayerConfig(plugin, player));
        }
    }

    public static void remove(OfflinePlayer player) {
        configs.remove(player);
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

    private PlayerConfig(Pl3xCraft plugin, OfflinePlayer player) {
        super();
        this.player = player;
        this.file = new File(plugin.getDataFolder(),
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
}
