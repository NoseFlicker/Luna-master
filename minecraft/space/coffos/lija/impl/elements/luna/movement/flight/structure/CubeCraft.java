package space.coffos.lija.impl.elements.luna.movement.flight.structure;

import net.minecraft.util.Vec3;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.impl.events.EventUpdate;
import space.coffos.lija.util.entity.PlayerUtils;
import space.coffos.lija.util.math.Vector3DUtils;

public class CubeCraft {

    @EventRegister
    public void onUpdate(EventUpdate e) {
        Element.mc.timer.timerSpeed = 0.250592f;
        if (Element.mc.thePlayer.isMoving()) {
            Element.mc.thePlayer.motionY += 0.005D;
            Element.mc.thePlayer.noClip = true;
            PlayerUtils.updatePosition(Element.mc.thePlayer, "NORMAL");
            Vec3 pos = new Vec3(Element.mc.thePlayer.posX, Element.mc.thePlayer.posY, Element.mc.thePlayer.posZ);
            Vector3DUtils vec = new Vector3DUtils(pos, -Element.mc.thePlayer.rotationYaw, 0.0F, Element.mc.thePlayer.ticksExisted % 2 == 0 ? 0.1 : 1.5);
            Element.mc.thePlayer.onGround = Element.mc.thePlayer.isDead & Element.mc.thePlayer.isMoving();
            Element.mc.thePlayer.setPosition(vec.getEndVector().xCoord, vec.getEndVector().yCoord, vec.getEndVector().zCoord);
            if (Element.mc.gameSettings.keyBindJump.getIsKeyPressed())
                Element.mc.thePlayer.motionY = 0.45;
            else if (Element.mc.gameSettings.keyBindSneak.getIsKeyPressed())
                Element.mc.thePlayer.motionY = -0.45;
        } else Element.mc.thePlayer.setSpeed(0);
    }
}