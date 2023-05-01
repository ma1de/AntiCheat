package me.ma1de.anticheat.packet;

import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class Packet {
    private final NMSPacket packet;
    private final Player player;
    private final PacketDirection direction;
    private final byte packetId;

    public Packet(final PacketPlayReceiveEvent event) {
        this.packet = event.getNMSPacket();
        this.player = event.getPlayer();
        this.direction = PacketDirection.INCOMING;
        this.packetId = event.getPacketId();
    }

    public Packet(final PacketPlaySendEvent event) {
        this.packet = event.getNMSPacket();
        this.player = event.getPlayer();
        this.direction = PacketDirection.OUTGOING;
        this.packetId = event.getPacketId();
    }

    public boolean isMovement() {
        return packetId == PacketType.Play.Client.POSITION || packetId == PacketType.Play.Client.FLYING;
    }

    public boolean isRotation() {
        return packetId == PacketType.Play.Client.POSITION_LOOK
                || packetId == PacketType.Play.Client.LOOK
                || packetId == PacketType.Play.Client.FLYING;
    }

    public boolean isArmAnimation() {
        return packetId == PacketType.Play.Client.ARM_ANIMATION;
    }

    public boolean is(final byte packetId) {
        return this.packetId == packetId;
    }

    public boolean isFromClient() {
        return this.direction.equals(PacketDirection.INCOMING);
    }

    public boolean isFromServer() {
        return this.direction.equals(PacketDirection.OUTGOING);
    }
}
