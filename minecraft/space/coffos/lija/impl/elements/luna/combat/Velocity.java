package space.coffos.lija.impl.elements.luna.combat;

import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.element.ElementStructure;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.api.setting.ModeHandler;
import space.coffos.lija.api.setting.Setting;
import space.coffos.lija.impl.events.EventPacketReceive;
import space.coffos.lija.impl.events.EventUpdate;
import space.coffos.lija.util.entity.PlayerUtils;

/**
 * @author Zhn17
 * <-> 2018-04-15 <-> 19:26
 **/
@ElementStructure(name = "Velocity", category = Category.COMBAT, description = "Take no velocity (knockback).", clientType = "Luna")
public class Velocity extends Element {

    @ModeHandler(name = "Mode", currentOption = "Normal", options = {"Normal", "Invalid-Position"}, locked = false)
    public Setting mode;

    @EventRegister
    public void onUpdate(EventUpdate event) {
        if (!isToggled() || !mode.getValString().equalsIgnoreCase("Invalid-Position") || !(mc.thePlayer.onGround & mc.thePlayer.hurtTime > 3))
            return;
        PlayerUtils.tpRel(0, 0.1, 0);
        mc.thePlayer.setVelocity(0, -1.0E-7, 0);
    }

    @EventRegister
    public void onPacketReceive(EventPacketReceive eventPacketReceive) {
        if (!isToggled()) return;
        if (mode.getValString().equalsIgnoreCase("Normal")) {
            if (eventPacketReceive.getPacket() instanceof S12PacketEntityVelocity | eventPacketReceive.getPacket() instanceof S27PacketExplosion)
                eventPacketReceive.setCancelled(true);
        } else if (mode.getValString().equalsIgnoreCase("Invalid-Position") && eventPacketReceive.getPacket() instanceof S08PacketPlayerPosLook & eventPacketReceive.getPacket() instanceof S12PacketEntityVelocity | eventPacketReceive.getPacket() instanceof S08PacketPlayerPosLook & eventPacketReceive.getPacket() instanceof S27PacketExplosion)
            eventPacketReceive.setCancelled(true);
    }
}