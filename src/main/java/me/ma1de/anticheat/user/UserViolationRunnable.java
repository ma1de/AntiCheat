package me.ma1de.anticheat.user;

import com.google.common.collect.Lists;
import me.ma1de.anticheat.AntiCheat;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class UserViolationRunnable extends BukkitRunnable {
    @Override
    public void run() {
        for (final User user : AntiCheat.getInstance().getUserHandler().getUsers()) {
            if (user.getUserViolations().isEmpty()) {
                continue;
            }

            final List<UserViolation> newVl = Lists.newArrayList();

            for (final UserViolation vl : user.getUserViolations()) {
                if (!vl.isExpired()) {
                    continue;
                }

                newVl.add(vl);
            }

            user.getUserViolations().removeAll(newVl);
        }
    }
}
