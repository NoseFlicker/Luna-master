package space.coffos.lija.impl.elements.luna.player;

import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.element.ElementStructure;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.api.setting.BooleanHandler;
import space.coffos.lija.api.setting.ModeHandler;
import space.coffos.lija.api.setting.Setting;
import space.coffos.lija.impl.events.EventSlowDown;

/**
 * @author Zhn17
 * <-> 2018-04-15 <-> 20:01
 **/
@ElementStructure(name = "NoSlowdown", category = Category.PLAYER, clientType = "Luna")
public class NoSlow extends Element {

    @ModeHandler(name = "Normal", currentOption = "Normal", options = {"Normal", "AAC"}, locked = false)
    public static Setting mode;

    @BooleanHandler(name = "Release", booleanValue = false)
    public static Setting release;

    @EventRegister
    public void onUpdate(EventSlowDown eventSlowDown) {
        if (mc.thePlayer.isUsingItem()) {
            if (mode.getValString().equalsIgnoreCase("AAC") & mc.thePlayer.isMoving())
                mc.thePlayer.setSpeed(0.05);
            eventSlowDown.setCancelled(true);
            if (release.getValBoolean())
                mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(0, 0, 0), 255, mc.thePlayer.inventory.getCurrentItem(), 0.0F, 0.0F, 0.0F));
        }
    }
}