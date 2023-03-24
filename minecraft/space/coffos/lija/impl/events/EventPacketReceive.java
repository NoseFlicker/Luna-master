package space.coffos.lija.impl.events;

import net.minecraft.network.Packet;
import space.coffos.lija.api.event.Event;

public class EventPacketReceive extends Event {

    private Packet packet;

    public EventPacketReceive(Packet packet) {
        super(Type.SINGLE);
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }
}