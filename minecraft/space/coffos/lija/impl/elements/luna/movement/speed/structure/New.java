package space.coffos.lija.impl.elements.luna.movement.speed.structure;

import net.minecraft.util.MovementInput;
import space.coffos.lija.impl.elements.luna.movement.speed.Speed;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.impl.events.EventMotion;
import space.coffos.lija.impl.events.EventMove;
import space.coffos.lija.util.math.MathUtils;
import space.coffos.lija.util.packets.PacketUtils;

import java.util.List;

import static space.coffos.lija.api.element.Element.mc;
import static space.coffos.lija.api.event.Event.Type.PRE;

/**
 * @author Zhn17
 * <-> 2018-09-12 <->
 * space.lunaclient.luna.impl.elements.luna.movement.speed.structure
 **/
public class New {

    private double moveSpeed, lastDist;
    private int stage;

    @EventRegister
    public boolean onUpdate(EventMotion event) {
        if (event.getType() == PRE) {
            final double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            final double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
            this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
        }
        return true;
    }

    @EventRegister
    private void onMove(EventMove e) {
        mc.timer.resetTimer();
        if (mc.thePlayer.moveForward == 0.0F && mc.thePlayer.moveStrafing == 0.0F)
            moveSpeed = mc.thePlayer.getBaseMoveSpeed() - 0.04D;
        if (stage == 1 && mc.thePlayer.isCollidedVertically && (mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F))
            moveSpeed = MathUtils.setRandom(1, 1.22) + mc.thePlayer.getBaseMoveSpeed() - 0.04D;
        if (stage == 2 && mc.thePlayer.isCollidedVertically && (mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F)) {
            PacketUtils.setState(true);
            PacketUtils.interceptC03Pos(0, -1.0E-17, 0, true);
            e.setY(mc.thePlayer.motionY = 0.42 * MathUtils.setRandom(0.96, MathUtils.setRandom(0.98, 0.99)));
            moveSpeed *= Speed.bSpeed.getValDouble();
        } else if (stage == 3) {
            double difference = MathUtils.setRandom(0.5295, 0.73) * (lastDist - mc.thePlayer.getBaseMoveSpeed() - 0.04);
            moveSpeed = lastDist - difference;
        } else {
            List collidingList = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D));
            if ((collidingList.size() > 0 || mc.thePlayer.isCollidedVertically) && stage > 0)
                stage = mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F ? 1 : 0;
            moveSpeed = lastDist - lastDist / MathUtils.setRandom(150, 149);
        }
        moveSpeed = Math.max(moveSpeed, mc.thePlayer.getBaseMoveSpeed());
        if (stage > 0) {
            double forward = MovementInput.moveForward;
            double strafe = MovementInput.moveStrafe;
            float yaw = mc.thePlayer.rotationYaw;
            if (forward == 0.0D && strafe == 0.0D) {
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
                e.setX(forward * moveSpeed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * moveSpeed * Math.sin(Math.toRadians(yaw + 89.9F)));
                e.setZ(forward * moveSpeed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * moveSpeed * Math.cos(Math.toRadians(yaw + 89.9F)));
            }
        }
        if (mc.thePlayer.isMoving()) stage += 1;
    }
}