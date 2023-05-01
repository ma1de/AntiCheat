package me.ma1de.anticheat.util;

import lombok.experimental.UtilityClass;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

@UtilityClass
public class PlayerUtil {
    public int getPing(final Player player) {
        return ((CraftPlayer) player).getHandle().ping;
    }
}
