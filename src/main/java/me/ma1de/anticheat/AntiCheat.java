package me.ma1de.anticheat;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import lombok.Getter;
import me.ma1de.anticheat.listener.UserListener;
import me.ma1de.anticheat.listener.packet.CheckListener;
import me.ma1de.anticheat.user.UserHandler;
import me.ma1de.anticheat.user.UserViolationRunnable;
import me.ma1de.anticheat.util.cpsrecorder.CPSRecorderHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class AntiCheat extends JavaPlugin {
    private static AntiCheat instance;

    private UserHandler userHandler;
    private CPSRecorderHandler recorderHandler;

    @Override
    public void onLoad() {
        PacketEvents.create(this);

        PacketEvents.get().getSettings()
                .fallbackServerVersion(ServerVersion.v_1_8_8)
                .bStats(false)
                .checkForUpdates(false);

        PacketEvents.get().load();
    }

    @Override
    public void onEnable() {
        instance = this;

        this.userHandler = new UserHandler();
        this.recorderHandler = new CPSRecorderHandler();

        Bukkit.getPluginManager().registerEvents(new UserListener(), this);
        PacketEvents.get().registerListener(new CheckListener());

        PacketEvents.get().init();

        new UserViolationRunnable().runTaskTimer(this, 0L, 20L);
    }

    @Override
    public void onDisable() {
        instance = null;

        PacketEvents.get().terminate();
    }

    public static AntiCheat getInstance() {
        return instance;
    }
}
