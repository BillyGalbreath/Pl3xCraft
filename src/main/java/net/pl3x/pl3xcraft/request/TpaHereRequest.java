package net.pl3x.pl3xcraft.request;

import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.configuration.Lang;
import org.bukkit.entity.Player;

public class TpaHereRequest extends Request {
    public TpaHereRequest(Pl3xCraft plugin, Player requester, Player target) {
        super(plugin, requester, target);
        Lang.send(target, Lang.TELEPORT_REQUESTHERE_TARGET
                .replace("{requester}", requester.getName()));
    }

    @Override
    protected void teleport() {
        if (!getTarget().isOnline() || !getRequester().isOnline()) {
            cancel();
            return;
        }
        playTeleportSounds();
        getTarget().teleport(getRequester().getLocation());
    }
}
