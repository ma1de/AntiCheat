package me.ma1de.anticheat.listener;

import com.google.common.collect.Lists;
import me.ma1de.anticheat.AntiCheat;
import me.ma1de.anticheat.util.CPSRecorder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class UserListener implements Listener {
    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        AntiCheat.getInstance().getUserHandler().addUser(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        final List<CPSRecorder> recs = Lists.newArrayList();

        for (final CPSRecorder rec : AntiCheat.getInstance().getRecorderHandler().getRecorders()) {
            if (!rec.getUser().getUuid().equals(event.getPlayer().getUniqueId())) {
                continue;
            }

            recs.add(rec);
        }

        AntiCheat.getInstance().getRecorderHandler().getRecorders().removeAll(recs);

        AntiCheat.getInstance().getUserHandler().removeUser(event.getPlayer().getUniqueId());
    }
}
