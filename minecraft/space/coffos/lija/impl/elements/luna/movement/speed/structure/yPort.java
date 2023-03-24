package space.coffos.lija.impl.elements.luna.movement.speed.structure;

/*
 * Made by Zhn17 <-> 2018-06-24 <-> 17:31
 */

import net.minecraft.block.BlockAir;
import space.coffos.lija.impl.elements.luna.movement.speed.Speed;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.impl.events.EventUpdate;
import space.coffos.lija.util.entity.BlockUtils;

import java.util.concurrent.atomic.AtomicInteger;

import static space.coffos.lija.api.element.Element.mc;

public class yPort {

    public static AtomicInteger state = new AtomicInteger();

    @EventRegister
    public void onUpdate(EventUpdate e) {
        mc.thePlayer.posY = Speed.yOffset;
        if (!(BlockUtils.getBlockUnderPlayer(mc.thePlayer, 1) instanceof BlockAir)) {
            if ((mc.thePlayer.moveForward != 0.0F | mc.thePlayer.moveStrafing != 0.0F) & !mc.thePlayer.isCollidedHorizontally) {
                if ((double) mc.thePlayer.fallDistance > 3.994D | mc.thePlayer.isInWater() | mc.thePlayer.isOnLadder() |
                        mc.gameSettings.keyBindJump.pressed | mc.thePlayer.fallDistance > 0.1) {
                    mc.timer.timerSpeed = 1.0F;
                    return;
                }
                mc.thePlayer.cameraPitch = 0;
                mc.timer.timerSpeed = 1.0F;
                mc.thePlayer.setSpeed(mc.thePlayer.getBaseMoveSpeed() - 0.03);
            }

            if (mc.thePlayer.isInWater() | mc.thePlayer.isOnLadder()) return;

            if (mc.thePlayer.fallDistance < 0.0784 & mc.thePlayer.isMoving() & !mc.thePlayer.isCollidedHorizontally) {
                mc.timer.timerSpeed = (float) Speed.vSpeed.getValDouble();
                mc.thePlayer.motionY--;

                mc.thePlayer.posY = Speed.yOffset;
            } else
                mc.timer.timerSpeed = 1.0F;

            if (mc.thePlayer.onGround & (mc.thePlayer.moveForward != 0.0F | mc.thePlayer.moveStrafing != 0.0F) & !mc.thePlayer.isCollidedHorizontally) {
                mc.thePlayer.jump();
                if (Speed.vSpeed.getValDouble() > 2) {
                    mc.thePlayer.setSpeed(mc.thePlayer.getBaseMoveSpeed() + 0.17);
                    mc.timer.timerSpeed = (float) (Speed.vSpeed.getValDouble() - 0.15);
                }
                mc.thePlayer.posY = Speed.yOffset;
                mc.thePlayer.cameraPitch = 0.0F;
            }
        }
    }
}