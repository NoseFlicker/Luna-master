package space.coffos.lija.impl.elements.luna.player;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.status.server.S01PacketPong;
import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.element.ElementStructure;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.impl.events.EventPacketReceive;
import space.coffos.lija.impl.events.EventPacketSend;
import space.coffos.lija.impl.events.EventUpdate;

@ElementStructure(name = "Sync", category = Category.PLAYER, clientType = "Luna")
public class Sync extends Element {

    @EventRegister
    public void onPacketSend(EventPacketSend e) {
        e.setCancelled(e.getPacket() instanceof S2EPacketCloseWindow);
    }

    @EventRegister
    public void onPacketReceive(EventPacketReceive e) {
        e.setCancelled(e.getPacket() instanceof C03PacketPlayer | e.getPacket() instanceof S01PacketPong | e.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition);
    }

    @EventRegister
    public void onUpdate(EventUpdate e) {
        int oldSlot = mc.thePlayer.inventory.currentItem;
        mc.thePlayer.inventory.currentItem = 9;
        mc.thePlayer.inventory.currentItem = oldSlot;
    }
}