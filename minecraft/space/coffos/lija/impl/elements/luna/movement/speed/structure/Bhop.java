package space.coffos.lija.impl.elements.luna.movement.speed.structure;

import net.minecraft.util.MovementInput;
import space.coffos.lija.LiJA;
import space.coffos.lija.api.event.Event;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.impl.elements.luna.movement.speed.Speed;
import space.coffos.lija.impl.elements.luna.world.Scaffold;
import space.coffos.lija.impl.events.EventMotion;
import space.coffos.lija.impl.events.EventMove;
import space.coffos.lija.util.packets.PacketUtils;

import java.util.List;
import java.util.Random;

import static space.coffos.lija.api.element.Element.mc;

public class Bhop {

    public static double moveSpeed;
    private static double lastDist;
    public static int stage = 0;

    @EventRegister
    public void onMotion(EventMotion event) {
        if (event.getType() == Event.Type.PRE) {
            double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
            lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
        }
    }

    @EventRegister
    public void onMove(EventMove e) {
        mc.timer.resetTimer();
        if (mc.thePlayer.moveForward == 0.0F && (mc.thePlayer.moveStrafing == 0.0F))
            moveSpeed = mc.thePlayer.getBaseMoveSpeed() - 0.05D;
        if (stage == 1 && (mc.thePlayer.isCollidedVertically) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F)))
            moveSpeed = 1.17D + mc.thePlayer.getBaseMoveSpeed() - 0.01D;
        if (stage == 2 && (mc.thePlayer.isCollidedVertically) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F))) {
            e.setY(mc.thePlayer.motionY = 0.42004D);
            Random random = new Random();
            double[] speedVal = {LiJA.INSTANCE.elementManager.getElement(Scaffold.class).isToggled() ? 1.2D : Speed.bSpeed.getValDouble()};
            int index = random.nextInt(speedVal.length);
            double var = speedVal[index];
            moveSpeed *= var;
        } else if (stage == 3) {
            double difference = 0.87D * (lastDist - mc.thePlayer.getBaseMoveSpeed() - 0.05D);
            moveSpeed = lastDist - difference;
        } else {
            List collidingList = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D));
            if ((collidingList.size() > 0 || mc.thePlayer.isCollidedVertically) && stage > 0)
                stage = mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F ? 1 : 0;
            moveSpeed = lastDist - lastDist / 121.0D;
        }
        moveSpeed = Math.max(moveSpeed, mc.thePlayer.getBaseMoveSpeed());
        if (stage > 0) {
            double forward = MovementInput.moveForward;
            double strafe = MovementInput.moveStrafe;
            float yaw = mc.thePlayer.rotationYaw;
            if ((forward == 0.0D) && (strafe == 0.0D)) {
                e.setX(0.0D);
                e.setZ(0.0D);
            } else {
                if (forward != 0.0D) {
                    if (strafe > 0.0D) yaw += (forward > 0.0D ? -45 : 45);
                    else if (strafe < 0.0D) yaw += (forward > 0.0D ? 45 : -45);
                    strafe = 0.0D;
                    if (forward > 0.0D) forward = 1.0D;
                    else forward = -1.0D;
                }
                double cos = Math.cos(Math.toRadians(yaw + 90.0F));
                double sin = Math.sin(Math.toRadians(yaw + 90.0F));
                e.setX(forward * moveSpeed * cos + strafe * moveSpeed * sin);
                e.setZ(forward * moveSpeed * sin - strafe * moveSpeed * cos);
            }
        }
        if (mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F) stage += 1;
    }
}