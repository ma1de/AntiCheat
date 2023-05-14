package me.ma1de.anticheat.processor.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import lombok.Getter;
import me.ma1de.anticheat.AntiCheat;
import me.ma1de.anticheat.packet.Packet;
import me.ma1de.anticheat.processor.Processor;
import me.ma1de.anticheat.user.User;
import me.ma1de.anticheat.util.CPSRecorder;

import java.util.List;
import java.util.OptionalDouble;

@Getter
public class CombatProcessor implements Processor {
    private final User user;

    private final List<Integer> cpsValues = Lists.newArrayList();

    private int cps = 0;
    private double highestCps, averageCps, lowestCps = 0.0;

    private long lastSwing, lastLastSwing,  deltaLastLastSwing; // i hate myself for naming it that way
    private long zeroSwingTicks;

    public CombatProcessor(User user) {
        this.user = user;
    }

    @Override
    public void handle(Packet packet) {
        if (user == null) {
            return;
        }

        if (packet.isFromClient() && packet.is(PacketType.Play.Client.USE_ENTITY)) {
            if (new WrappedPacketInUseEntity(packet.getPacket()).getAction() != WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
                return;
            }

            if (AntiCheat.getInstance().getRecorderHandler().getRecorder(user, this) == null) {
                AntiCheat.getInstance().getRecorderHandler().addRecorder(user, this);
                return;
            }

            final CPSRecorder recorder = AntiCheat.getInstance().getRecorderHandler().getRecorder(user, this);

            if (recorder.hasPassed(1000L)) {
                this.cpsValues.add(cps);
                this.cps = 0;

                final OptionalDouble optMax = this.cpsValues.stream().mapToDouble(a -> a).max();
                final OptionalDouble optAvg = this.cpsValues.stream().mapToDouble(a -> a).average();
                final OptionalDouble optMin = this.cpsValues.stream().mapToDouble(a -> a).min();

                this.highestCps = optMax.getAsDouble();

                if (optAvg.isPresent()) {
                    this.averageCps = optAvg.getAsDouble();
                }

                if (optMin.isPresent()) {
                    this.lowestCps = optMin.getAsDouble();
                }


                AntiCheat.getInstance().getRecorderHandler().removeRecorder(user, this);
                return;
            }

            this.cps = recorder.getSwings();

            this.lastLastSwing = this.lastSwing;
            this.lastSwing = System.currentTimeMillis();

            this.deltaLastLastSwing = System.currentTimeMillis() - lastLastSwing;

            if (this.deltaLastLastSwing < 15) {
                zeroSwingTicks++;
            } else {
                zeroSwingTicks = 0;
            }
        }
    }
}
