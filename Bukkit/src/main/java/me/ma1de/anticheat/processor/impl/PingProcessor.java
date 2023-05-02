package me.ma1de.anticheat.processor.impl;

import io.github.retrooper.packetevents.PacketEvents;
import lombok.Getter;
import me.ma1de.anticheat.packet.Packet;
import me.ma1de.anticheat.processor.Processor;
import me.ma1de.anticheat.user.User;

@Getter
public class PingProcessor implements Processor {
    private final User user;

    private int ping, lastPing;
    private float deltaPing;

    public PingProcessor(User user) {
        this.user = user;
    }

    @Override
    public void handle(Packet packet) {
        this.lastPing = ping;
        this.ping = PacketEvents.get().getPlayerUtils().getPing(packet.getPlayer());

        this.deltaPing = this.lastPing - ping;
    }
}
