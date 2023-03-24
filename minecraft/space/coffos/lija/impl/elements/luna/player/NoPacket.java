package space.coffos.lija.impl.elements.luna.player;

import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.play.server.S30PacketWindowItems;
import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.element.ElementStructure;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.api.setting.ModeHandler;
import space.coffos.lija.api.setting.Setting;
import space.coffos.lija.impl.events.EventPacketReceive;

/**
 * @author Zhn17
 * <-> 2018-04-17 <-> 11:20
 **/
@ElementStructure(name = "NoPacket", category = Category.PLAYER, description = "Allows you to stop incoming packets.", clientType = "Luna")
public class NoPacket extends Element {

    @ModeHandler(name = "Mode", currentOption = "Rotate", options = {"Rotate", "CloseINV", "Both"}, locked = false)
    public Setting mode;

    @EventRegister
    public void onPacketReceive(EventPacketReceive event) {
        if (mc.currentScreen instanceof GuiDownloadTerrain) return;
        if (mode.getValString().equalsIgnoreCase("Rotate") | mode.getValString().equalsIgnoreCase("All")) {
            if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();
                packet.field_148936_d = mc.thePlayer.rotationYaw;
                packet.field_148937_e = mc.thePlayer.rotationPitch;
            }
        } else if (mode.getValString().equalsIgnoreCase("CloseINV") | mode.getValString().equalsIgnoreCase("Both"))
            event.setCancelled(event.getPacket() instanceof S2EPacketCloseWindow | event.getPacket() instanceof S30PacketWindowItems);
    }
}