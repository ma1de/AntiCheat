package me.ma1de.anticheat.check.impl;

import me.ma1de.anticheat.check.Check;
import me.ma1de.anticheat.check.CheckData;
import me.ma1de.anticheat.check.CheckType;
import me.ma1de.anticheat.packet.Packet;
import me.ma1de.anticheat.user.User;
import me.ma1de.anticheat.util.PlayerUtil;

/**
 * Auto Clicker check itself
 */
@CheckData(name = "Clicker (A)", type = CheckType.COMBAT, description = "Checks player's CPS (clicks per one second)", maxVl = 2)
public class ClickerA extends Check {
    public ClickerA(User user) {
        super(user);
    }

    @Override
    public void handle(Packet packet) {
        if (packet.isFromClient() && packet.isArmAnimation()) {
            if (user.getCombatProcessor().getCps() > 15 && PlayerUtil.getPing(packet.getPlayer()) < 500) {
                if (increaseThresholdBy(0.5) >= 20) {
                    flag("cps=" + user.getCombatProcessor().getCps());
                }
            } else {
                decreaseThresholdBy(0.25);
            }

            debug("cps=" + user.getCombatProcessor().getCps()
                    + " highestCps=" + user.getCombatProcessor().getHighestCps()
                    + " averageCps=" + user.getCombatProcessor().getAverageCps()
                    + " lowestCps=" + user.getCombatProcessor().getLowestCps()
                    + " threshold=" + getThreshold());
        }
    }
}
