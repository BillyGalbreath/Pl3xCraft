package net.pl3x.pl3xcraft.request;

import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.configuration.Config;
import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.configuration.PlayerConfig;
import net.pl3x.pl3xcraft.task.TeleportSounds;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class Request {
    private final Pl3xCraft plugin;
    private final Player requester;
    private final Player target;
    private final RequestTimeout timeoutTask;

    public Request(Pl3xCraft plugin, Player requester, Player target) {
        this.plugin = plugin;
        this.requester = requester;
        this.target = target;

        this.timeoutTask = new RequestTimeout(this);

        if (Config.TELEPORT_REQUEST_TIMEOUT > 0) {
            this.timeoutTask.runTaskLater(plugin,
                    Config.TELEPORT_REQUEST_TIMEOUT * 20);
        }

    }

    public Player getRequester() {
        return requester;
    }

    public Player getTarget() {
        return target;
    }

    public void accept() {
        teleport();

        Lang.send(target, Lang.TELEPORT_ACCEPT_TARGET
                .replace("{requester}", requester.getName()));
        Lang.send(requester, Lang.TELEPORT_ACCEPT_REQUESTER
                .replace("{target}", target.getName()));

        cancel();
    }

    public void deny() {
        Lang.send(target, Lang.TELEPORT_DENIED_TARGET
                .replace("{requester}", requester.getName()));
        Lang.send(requester, Lang.TELEPORT_DENIED_REQUESTER
                .replace("{target}", target.getName()));

        cancel();
    }

    protected abstract void teleport();

    void playTeleportSounds() {
        if (Config.USE_TELEPORT_SOUNDS) {
            new TeleportSounds(target.getLocation(), requester.getLocation())
                    .runTaskLater(plugin, 1);
        }
    }

    void cancel() {
        try {
            timeoutTask.cancel();
            PlayerConfig.getConfig(target).setRequest(null);
        } catch (IllegalStateException ignore) {
        }
    }

    private class RequestTimeout extends BukkitRunnable {
        private final Request request;

        RequestTimeout(Request request) {
            this.request = request;
        }

        @Override
        public void run() {
            if (!request.target.isOnline() || !request.requester.isOnline()) {
                request.cancel();
                return;
            }

            String type;
            if (request instanceof TpaRequest) {
                type = "Tpa";
            } else if (request instanceof TpaHereRequest) {
                type = "TpaHere";
            } else {
                return; // should never happen
            }

            String message = Lang.REQUEST_TIMED_OUT
                    .replace("{type}", type);

            Lang.send(request.requester, message);
            Lang.send(request.target, message);

            request.cancel();
        }
    }
}
