package space.coffos.lija.util.entity.list;

import net.minecraft.util.MovementInput;
import space.coffos.lija.api.event.Event;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.impl.events.EventMotion;
import space.coffos.lija.impl.events.EventMove;
import space.coffos.lija.util.entity.MoveUtils;
import space.coffos.lija.util.math.MathUtils;
import space.coffos.lija.util.packets.PacketUtils;

import static jdk.nashorn.internal.objects.NativeMath.round;
import static space.coffos.lija.api.element.Element.mc;
import static space.coffos.lija.util.entity.MoveUtils.scf;

public class Booster {

    private int stage, level;
    public static double moveSpeed, boost, slowDown, lastDist;

    @EventRegister
    public void onMotion(EventMotion event) {
        if (event.getType() == Event.Type.PRE & MoveUtils.validated) {
            double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
            this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
        }
    }

    @EventRegister
    public void onMove(EventMove event) {
        /* Check so the boost can continue, if not, cut it's head off. */
        if (!MoveUtils.validated) return;
        float forward = MovementInput.moveForward;
        float strafe = MovementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        double mx = Math.cos(Math.toRadians(yaw + 90.0F));
        double mz = Math.sin(Math.toRadians(yaw + 90.0F));
        moveSpeed = Math.max(moveSpeed, mc.thePlayer.getBaseMoveSpeed());
        if (forward == 0.0F & strafe == 0.0F) {
            event.x = 0.0D;
            event.z = 0.0D;
        }
        if (!mc.thePlayer.onGround) this.moveSpeed = mc.thePlayer.getBaseMoveSpeed() / 2;

        if ((this.stage == 1) & ((mc.thePlayer.moveForward != 0.0F) | (mc.thePlayer.moveStrafing != 0.0F))) {
            this.stage = 2;
            this.moveSpeed = (1.38D * mc.thePlayer.getBaseMoveSpeed() - 0.01D) / 1.6;
        } else if (this.stage == 2) {
            this.stage = 3;
            if (!scf) {
                PacketUtils.setState(true);
                mc.thePlayer.motionY = 0.42 * MathUtils.setRandom(0.96, MathUtils.setRandom(0.98, 0.99));
                event.y = 0.42 * MathUtils.setRandom(0.96, MathUtils.setRandom(0.98, 0.99));
                PacketUtils.interceptC03Pos(0, 0.42, 0, true);
            }
            this.moveSpeed *= 1.249D;
        } else if (this.stage == 3) {
            this.stage = 4;
            double difference = MathUtils.setRandom(0.5295, 0.73) * (this.lastDist - mc.thePlayer.getBaseMoveSpeed());
            this.moveSpeed = (this.lastDist - difference) * 1.05;
        } else {
            if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY - 1.0E-10D, 0.0D)).size() > 0) | (mc.thePlayer.isCollidedVertically))
                this.stage = 1;
            this.moveSpeed = (this.lastDist - this.lastDist / 61.0D);
        }

        this.moveSpeed = Math.max(this.moveSpeed, mc.thePlayer.getBaseMoveSpeed());

        if (forward != 0.0F) {
            if (strafe >= 1.0F | strafe <= -1.0F) strafe = 0.0F;

            if (forward > 0.0F) forward = 1.0F;
            else if (forward < 0.0F) forward = -1.0F;
        }

        event.x = (forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz);
        event.z = (forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx);
        event.x = (double) forward * moveSpeed * mx + (double) strafe * moveSpeed * mz;
        event.z = (double) forward * moveSpeed * mz - (double) strafe * moveSpeed * mx;

        if ((this.stage == 1) & ((mc.thePlayer.moveForward != 0.0F) | (mc.thePlayer.moveStrafing != 0.0F))) {
            this.stage = 2;
            this.moveSpeed = (1.38D * mc.thePlayer.getBaseMoveSpeed() - 0.01D);
        } else if (this.stage == 2) {
            this.stage = 3;
        } else if (this.stage == 3) {
            this.stage = 4;
            double difference = 0.66D * (this.lastDist - mc.thePlayer.getBaseMoveSpeed());
            this.moveSpeed = (this.lastDist - difference) * MathUtils.setRandom(boost, boost - 0.1);
        } else {
            if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0) | (mc.thePlayer.isCollidedVertically))
                this.stage = 1;
            level++;
            if (level > 2) level = 0;
            this.moveSpeed = this.lastDist - this.lastDist / slowDown;
        }

        this.moveSpeed = Math.max(this.moveSpeed, mc.thePlayer.getBaseMoveSpeed());

        if ((forward == 0.0F) & (strafe == 0.0F)) {
            event.x = 0.0D;
            event.z = 0.0D;
        } else if (forward != 0.0F) {
            if (strafe >= 1.0F | strafe <= -1.0F) strafe = 0.0F;
            forward = forward > 0.0F ? 1.0F : -1.0F;
        }

        event.x = (forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz);
        event.z = (forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx);
    }
}