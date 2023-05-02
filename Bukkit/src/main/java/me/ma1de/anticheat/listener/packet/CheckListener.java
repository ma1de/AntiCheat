package me.ma1de.anticheat.listener.packet;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import me.ma1de.anticheat.AntiCheat;
import me.ma1de.anticheat.check.Check;
import me.ma1de.anticheat.packet.Packet;
import me.ma1de.anticheat.user.User;
import me.ma1de.anticheat.util.CPSRecorder;

public class CheckListener extends PacketListenerAbstract {
    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        this.handle(new Packet(event));

        if (event.getPacketId() == PacketType.Play.Client.ARM_ANIMATION) {
            for (final CPSRecorder recorder : AntiCheat.getInstance().getRecorderHandler().getRecorders()) {
                recorder.record(1);
            }
        }
    }

    @Override
    public void onPacketPlaySend(PacketPlaySendEvent event) {
        this.handle(new Packet(event));
    }

    private void handle(final Packet packet) {
        if (AntiCheat.getInstance().getUserHandler().getUser(packet.getPlayer().getUniqueId()) == null) {
            return;
        }

        final User user = AntiCheat.getInstance().getUserHandler().getUser(packet.getPlayer().getUniqueId());

        if (System.currentTimeMillis() - user.getJoinTime() < 3000L) {
            return;
        }

        if (packet.isFromClient()) {
            user.getCombatProcessor().handle(packet);
            user.getPingProcessor().handle(packet);
        }

        for (final Check check : user.getChecks()) {
            check.handle(packet);
        }
    }
}
