package space.coffos.lija.impl.elements.luna.movement.flight.structure.list;

import net.minecraft.util.MovementInput;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.event.Event;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.impl.events.EventMotion;
import space.coffos.lija.impl.events.EventMove;
import space.coffos.lija.util.math.MathUtils;

import java.util.List;
import java.util.Random;

public class Boost {

    private double moveSpeed, lastDist;
    private int stage;

    @EventRegister
    public boolean onUpdate(EventMotion event) {
        if (event.getType() == Event.Type.PRE) {
            final double xDist = Element.mc.thePlayer.posX - Element.mc.thePlayer.prevPosX;
            final double zDist = Element.mc.thePlayer.posZ - Element.mc.thePlayer.prevPosZ;
            this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
        }
        return true;
    }

    @EventRegister
    private void onMove(EventMove e) {
        Element.mc.timer.resetTimer();
        if (Element.mc.thePlayer.moveForward == 0.0F && Element.mc.thePlayer.moveStrafing == 0.0F)
            moveSpeed = Element.mc.thePlayer.getBaseMoveSpeed() - 0.04D;
        if (stage == 1 && Element.mc.thePlayer.isCollidedVertically && (Element.mc.thePlayer.moveForward != 0.0F || Element.mc.thePlayer.moveStrafing != 0.0F))
            moveSpeed = 1.31D + Element.mc.thePlayer.getBaseMoveSpeed() - 0.04D;
        if ((stage == 1) && (Element.mc.thePlayer.isCollidedVertically) && ((Element.mc.thePlayer.moveForward != 0.0F) || (Element.mc.thePlayer.moveStrafing != 0.0F))) {
            Random random = new Random();
            //MoveUtils.motionBoost(MathUtils.setRandom(46.5, 48.5), MathUtils.setRandom(157.9, 160.5));
            double[] speedVal = {0.1, 0.2, 0.3};
            int index = random.nextInt(speedVal.length);
            double var = speedVal[index];
            moveSpeed *= var;
            stage = 2;
        } else if (stage == 3) {
            double difference = MathUtils.setRandom(0.68, 0.81) * (lastDist - Element.mc.thePlayer.getBaseMoveSpeed() - 0.04);
            moveSpeed = lastDist - difference;
        } else {
            List collidingList = Element.mc.theWorld.getCollidingBoundingBoxes(Element.mc.thePlayer, Element.mc.thePlayer.boundingBox.offset(0.0D, Element.mc.thePlayer.motionY, 0.0D));
            if ((collidingList.size() > 0 || Element.mc.thePlayer.isCollidedVertically) && stage > 0)
                stage = Element.mc.thePlayer.moveForward != 0.0F || Element.mc.thePlayer.moveStrafing != 0.0F ? 1 : 0;
            moveSpeed = lastDist - lastDist / MathUtils.setRandom(80, 90);
        }
        moveSpeed = Math.max(moveSpeed, Element.mc.thePlayer.getBaseMoveSpeed());
        if (stage > 0) {
            double forward = MovementInput.moveForward;
            double strafe = MovementInput.moveStrafe;
            float yaw = Element.mc.thePlayer.rotationYaw;
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
        if (Element.mc.thePlayer.isMoving()) stage = 1;
    }
}