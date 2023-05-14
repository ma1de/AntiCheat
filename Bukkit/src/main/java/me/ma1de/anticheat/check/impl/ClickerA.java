package me.ma1de.anticheat.check.impl;

import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import io.github.retrooper.packetevents.utils.boundingbox.BoundingBox;
import me.ma1de.anticheat.AntiCheat;
import me.ma1de.anticheat.check.Check;
import me.ma1de.anticheat.check.CheckData;
import me.ma1de.anticheat.check.CheckType;
import me.ma1de.anticheat.packet.Packet;
import me.ma1de.anticheat.user.User;
import me.ma1de.anticheat.util.PlayerUtil;

/**
 * Auto Clicker check itself
 * Feel free to use this in your own anticheat I give you a right to :D
 * I really couldn't care less if anticheat devs will skid this or not, you have a right to do so cause this is an open sourced project with a GPL-v3 license.
 * And this check is actually pretty stable and good, probably better than the checks that utilize WrappedPacketInFlying, because you don't even need to check
 * if the player is breaking/placing a block, because it triggers once you've swung your arm.
 */
@CheckData(name = "Clicker (A)", type = CheckType.COMBAT, description = "Checks player's CPS (clicks per one second)", maxVl = 2)
public class ClickerA extends Check {
    public ClickerA(User user) {
        super(user);
    }

    private final int maxClickSpeed = AntiCheat.getInstance().getConfig().getInt("maxClickSpeed", 15);
    private final double pingValueToCompensate = AntiCheat.getInstance().getConfig().getInt("ping-value-to-compensate", 300);
    private final double thresholdMax = AntiCheat.getInstance().getConfig().getDouble("threshold-max", 1.5);
    private final double thresholdDecay = AntiCheat.getInstance().getConfig().getDouble("threshold-decay", 0.25);
    private final double thresholdIncrease = AntiCheat.getInstance().getConfig().getDouble("threshold-increase", 0.5);

    @Override
    public void handle(Packet packet) {
        if (packet.isFromClient() && packet.is(PacketType.Play.Client.USE_ENTITY)) {
            if (new WrappedPacketInUseEntity(packet.getPacket()).getAction() != WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
                return;
            }

            debug("cps=" + user.getCombatProcessor().getCps()
                    + " threshold=" + getThreshold()
                    + " ping=" + PlayerUtil.getPing(packet.getPlayer()) + "ms"
                    + " lag=" + user.getLagCompensation()
                    + " zeroSwingTicks=" + user.getCombatProcessor().getZeroSwingTicks()
                    + " deltaPing=" + user.getPingProcessor().getDeltaPing() + (user.getPingProcessor().getDeltaPing() >= 20 ? " (Unusual Ping Behaviour)" : " (Normal Ping Behaviour)"));

            if (user.getCombatProcessor().getCps() > maxClickSpeed) {
                if (user.getPingProcessor().getDeltaPing() >= 20 && user.getPingProcessor().getPing() > user.getPingProcessor().getLastPing()) {
                    user.compensateLag();
                    return;
                }

                if (user.getCombatProcessor().getZeroSwingTicks() > 3) {
                    user.compensateLag();
                    return;
                }

                if (user.getPingProcessor().getPing() >= pingValueToCompensate) {
                    user.compensateLag();
                    return;
                }

                user.reduceCompensations();

                if (increaseThresholdBy(thresholdIncrease) >= thresholdMax && user.getLagCompensation() == 0) {
                    flag("cps=" + user.getCombatProcessor().getCps());
                }
            } else {
                decreaseThresholdBy(thresholdDecay);
            }
        }
    }
}
