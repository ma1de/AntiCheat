package me.ma1de.anticheat.check;

import lombok.Getter;
import me.ma1de.anticheat.AntiCheat;
import me.ma1de.anticheat.packet.Packet;
import me.ma1de.anticheat.user.User;
import me.ma1de.anticheat.user.UserViolation;
import me.ma1de.anticheat.util.PlayerUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

@Getter
public abstract class Check {
    public final User user;

    private double threshold;

    public Check(User user) {
        this.user = user;
    }

    public abstract void handle(final Packet packet);

    public void flag(final String info) {
        final TextComponent component = new TextComponent(
                ("&7[&eAC&7] &e" + Bukkit.getPlayer(user.getUuid()).getName() + " &7has failed &e" + getName() + " &7[" + user.getVl(this) + "/" + getMaxVl() + "]")
                            .replace('&', ChatColor.COLOR_CHAR));

        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,  new BaseComponent[]{
                new TextComponent(ChatColor.YELLOW + getDescription() + "\n\n"),
                new TextComponent(ChatColor.YELLOW + "Player: " + ChatColor.WHITE + Bukkit.getPlayer(user.getUuid()).getName() + "\n"),
                new TextComponent(ChatColor.YELLOW + "Info: " + ChatColor.WHITE + info + "\n"),
                new TextComponent(ChatColor.YELLOW + "Ping: " + ChatColor.WHITE + PlayerUtil.getPing(Bukkit.getPlayer(user.getUuid())) + "ms\n"),
                new TextComponent(ChatColor.YELLOW + "False Flag: " + ChatColor.WHITE + (PlayerUtil.getPing(Bukkit.getPlayer(user.getUuid())) >= 225 ? "Likely" : "Unlikely") + "\n"),
                new TextComponent(ChatColor.YELLOW + "TPS: " + ChatColor.WHITE + MinecraftServer.getServer().recentTps[0] + "\n"),
                new TextComponent(ChatColor.YELLOW + "Timestamp: " + ChatColor.WHITE + new SimpleDateFormat("MM/dd/yyy hh:mm:ss:S z").format(new Date(System.currentTimeMillis())) + "\n\n"),
                new TextComponent(ChatColor.YELLOW + "Click here to teleport to the player")
        }));

        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + Bukkit.getPlayer(user.getUuid()).getName()));

        for (final Player player : Bukkit.getOnlinePlayers()
                .stream()
                .filter(player -> player.hasPermission("cpsdetector.alerts") && AntiCheat.getInstance().getAlertsEnabled().contains(player.getUniqueId()))
                .collect(Collectors.toSet())) {
            player.spigot().sendMessage(component);
        }

        user.addVl(new UserViolation(user, this, info, System.currentTimeMillis()));

        if (user.getVl(this) >= getMaxVl()) {
            for (final Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(ChatColor.YELLOW + "AntiCheat" + ChatColor.WHITE + " has caught " + ChatColor.YELLOW + Bukkit.getPlayer(user.getUuid()).getName());
            }

            Bukkit.getScheduler().runTask(AntiCheat.getInstance(), () -> {
                /*
                  Don't remove this, because if the anticheat will ban the player too quick, this will
                  set off a NullPointerException.
                 */
                if (user == null || Bukkit.getPlayer(user.getUuid()) == null) {
                    return;
                }

                Bukkit.getPlayer(user.getUuid()).kickPlayer(ChatColor.RED + "Unfair Advantage");
            });
        }
    }

    public void debug(final String info) {
        final TextComponent component = new TextComponent(ChatColor.YELLOW + Bukkit.getPlayer(user.getUuid()).getName() + ChatColor.WHITE + " is debugging " + ChatColor.YELLOW + getName() +
                ChatColor.GRAY + " (Click Here)");

        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[] {
                new TextComponent(ChatColor.YELLOW + "Click here to copy the debug info")
        }));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, info));

        for (final Player player : Bukkit.getOnlinePlayers()
                .stream()
                .filter(player -> player.hasPermission("cpsdetector.debug") && AntiCheat.getInstance().getDebugEnabled().contains(player.getUniqueId()))
                .collect(Collectors.toSet())) {
            player.spigot().sendMessage(component);
        }
    }

    public String getName() {
        return this.getClass().getAnnotation(CheckData.class).name();
    }

    public String getDescription() {
        return this.getClass().getAnnotation(CheckData.class).description();
    }

    public int getMaxVl() {
        return this.getClass().getAnnotation(CheckData.class).maxVl();
    }

    public double increaseThreshold() {
        return threshold++;
    }

    public double increaseThresholdBy(final double by) {
        return threshold = threshold + by;
    }

    public void decreaseThreshold() {
        threshold = Math.max(threshold--, 0);
    }

    public void decreaseThresholdBy(final double by) {
        threshold = Math.max(threshold - by, 0);
    }

    public double resetThreshold() {
        return threshold = 0;
    }
}
