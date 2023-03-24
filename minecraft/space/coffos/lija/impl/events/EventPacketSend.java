package space.coffos.lija.impl.events;

import net.minecraft.network.Packet;
import space.coffos.lija.api.event.Event;

public class EventPacketSend extends Event {

    private Packet packet;

    public EventPacketSend(Packet packetIn) {
        super(Type.SINGLE);
        packet = packetIn;
    }

    public Packet getPacket() {
        return packet;
    }
}