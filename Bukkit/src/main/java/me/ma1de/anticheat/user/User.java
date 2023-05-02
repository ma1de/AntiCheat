package me.ma1de.anticheat.user;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.ma1de.anticheat.AntiCheat;
import me.ma1de.anticheat.check.Check;
import me.ma1de.anticheat.check.CheckData;
import me.ma1de.anticheat.processor.impl.CombatProcessor;
import me.ma1de.anticheat.processor.impl.PingProcessor;
import org.bukkit.Bukkit;
import org.reflections.Reflections;

import java.util.List;
import java.util.UUID;

@Getter
public class User {
    private final UUID uuid;
    private final List<Check> checks;
    private final List<UserViolation> userViolations;
    private final long joinTime;

    private final CombatProcessor combatProcessor;
    private final PingProcessor pingProcessor;

    @Setter private int lagCompensation = 0;

    public User(final UUID uuid) {
        if (Bukkit.getPlayer(uuid) == null) {
            throw new IllegalArgumentException("player cannot be null");
        }

        this.uuid = uuid;
        this.checks = Lists.newArrayList();
        this.userViolations = Lists.newArrayList();
        this.joinTime = System.currentTimeMillis();

        this.combatProcessor = new CombatProcessor(this);
        this.pingProcessor = new PingProcessor(this);
    }

    public int getVl(final Check check) {
        int count = 0;

        for (final UserViolation vl : userViolations) {
            if (vl.getCheck() != check) {
                continue;
            }

            if (vl.isExpired()) {
                continue;
            }

            count++;
        }

        return count;
    }

    public void addVl(final UserViolation vl) {
        if (vl.isExpired()) {
            AntiCheat.getInstance().getLogger().severe("Attempted adding an expired user violation. Skipping.");
            return;
        }

        this.userViolations.add(vl);
    }

    public void compensateLag() {
        this.lagCompensation++;
    }

    public void reduceCompensations() {
        this.lagCompensation = Math.max(this.lagCompensation - 1, 0);
    }

    @SneakyThrows
    public void loadChecks() {
        for (final Class<? extends Check> clazz : new Reflections("me.ma1de.anticheat.check.impl").getSubTypesOf(Check.class)) {
            if (clazz.getAnnotation(CheckData.class) == null) {
                continue;
            }

            this.checks.add(clazz.getConstructor(User.class).newInstance(this));
        }

        AntiCheat.getInstance().getLogger().info("Loaded " + this.checks.size() + " checks for " + Bukkit.getPlayer(uuid).getName());
    }
}
