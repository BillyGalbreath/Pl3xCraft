package net.pl3x.pl3xcraft.listener;

import net.pl3x.pl3xcraft.commands.CmdBack;
import net.pl3x.pl3xcraft.configuration.Config;
import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.configuration.PlayerConfig;
import net.pl3x.pl3xcraft.util.ExpUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerListener implements Listener {
    private final Set<PlayerTeleportEvent.TeleportCause> backAllowCauses = new HashSet<>();

    public PlayerListener() {
        backAllowCauses.add(PlayerTeleportEvent.TeleportCause.PLUGIN);
        backAllowCauses.add(PlayerTeleportEvent.TeleportCause.COMMAND);
        backAllowCauses.add(PlayerTeleportEvent.TeleportCause.UNKNOWN);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void backOnDeath(PlayerDeathEvent event) {
        if (!Config.BACK_ON_DEATH) {
            return;
        }

        Player player = event.getEntity();
        if (!player.hasPermission("command.back")) {
            return;
        }

        CmdBack.setPreviousLocation(player, player.getLocation());
        Lang.send(player, Lang.BACK_DEATH_HINT);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void backOnTeleport(PlayerTeleportEvent event) {
        if (!backAllowCauses.contains(event.getCause())) {
            return; // cause not allowed
        }

        if (!event.getPlayer().hasPermission("command.back")) {
            return;
        }

        Location to = event.getTo();
        Location from = event.getFrom();

        // only save location if teleporting more than 5 blocks
        if (!to.getWorld().equals(from.getWorld()) || to.distanceSquared(from) > 25) {
            CmdBack.setPreviousLocation(event.getPlayer(), event.getFrom());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setGameMode(GameMode.SURVIVAL);
        player.setFallDistance(-1024F); // do not fall to death on login
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerConfig playerConfig = PlayerConfig.getConfig(player);

        CmdBack.setPreviousLocation(player, null);
        playerConfig.setSeen(System.currentTimeMillis());
        PlayerConfig.remove(event.getPlayer());

        // Possible fix for enderchests merging with viewer
        player.getEnderChest().getViewers().forEach(viewer -> {
            viewer.closeInventory();
            Lang.send(viewer, Lang.CLOSED_ENDERCHEST);
            Lang.send(viewer, Lang.PLAYER_DISCONNECTED);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChangeWorld(PlayerChangedWorldEvent event) {
        // Possible fix for enderchests merging with viewer
        event.getPlayer().getEnderChest().getViewers().forEach(viewer -> {
            viewer.closeInventory();
            Lang.send(viewer, Lang.CLOSED_ENDERCHEST);
            Lang.send(viewer, Lang.PLAYER_CHANGED_WORLDS);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        PlayerConfig playerConfig = PlayerConfig.getConfig(player);
        playerConfig.setSeen(System.currentTimeMillis());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerSleep(PlayerBedEnterEvent event) {
        event.getPlayer().setBedSpawnLocation(event.getBed().getLocation());
        Lang.send(event.getPlayer(), Lang.BED_SPAWN_SET);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerAssign(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("command.assign")) {
            return;
        }
        Material tool = event.getMaterial();
        if (tool != null && !tool.isBlock()) {
            PlayerConfig config = PlayerConfig.getConfig(player);
            List<String> command = config.getAssignCommand(tool);
            if (!command.isEmpty()) {
                for (String assignedTool : command) {
                    player.chat("/" + assignedTool);
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBreakLootChest(BlockBreakEvent event) {
        if (event.getBlock().getType() != Material.CHEST) {
            return; // not a chest
        }

        Chest chest = (Chest) event.getBlock().getState();
        if (chest.hasLootTable()) {
            Lang.send(event.getPlayer(), Lang.CANNOT_BREAK_LOOT_CHESTS);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerFallIntoVoid(PlayerMoveEvent event) {
        if (event.getTo().getY() < 0) {
            Player player = event.getPlayer();
            player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
            Lang.send(player, Lang.SAVED_FROM_VOID);
            player.setFallDistance(-1000);
        }
    }

    @EventHandler
    public void onPlayerSuffocate(EntityDamageEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            if (event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
                event.setCancelled(true);
                Player player = (Player) event.getEntity();
                player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
                Lang.send(player, Lang.SAVED_FROM_SUFFOCATING);
            }
        }
    }

    @EventHandler
    public void onRightClickWithMending(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) {
            return; // not a right click
        }
        Player player = event.getPlayer();
        if (!player.isSneaking()) {
            return; // not sneaking
        }
        ItemStack item = event.getItem();
        if (item == null || item.getType() == Material.AIR) {
            return; // not holding item
        }
        short damage = item.getDurability();
        if (damage <= 0) {
            return; // not damaged
        }
        if (item.getEnchantmentLevel(Enchantment.MENDING) <= 0) {
            return; // not enchanted with mending
        }
        if (player.getTotalExperience() < 2 && player.getLevel() <= 0) {
            return; // not enough experience
        }
        item.setDurability((short) (damage - 4));
        ExpUtil.addPlayerXP(player, -2);
    }
}
