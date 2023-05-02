package me.ma1de.anticheat.processor;

import me.ma1de.anticheat.packet.Packet;

public interface Processor {
    void handle(final Packet packet);
}
