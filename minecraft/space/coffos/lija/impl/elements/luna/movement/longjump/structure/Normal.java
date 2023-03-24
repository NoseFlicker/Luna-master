package space.coffos.lija.impl.elements.luna.movement.longjump.structure;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;
import space.coffos.lija.util.math.MathUtils;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.impl.events.EventMotion;
import space.coffos.lija.impl.events.EventMove;

public class Normal {

    private int stage;
    private double moveSpeed, lastDist;

    private Minecraft mc = Minecraft.getMinecraft();

    @EventRegister
    public void onMotion(EventMotion e) {
        double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
        double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
        this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
    }

    @EventRegister
    public void onMove(EventMove event) {
        MovementInput movementInput = mc.thePlayer.movementInput;
        float forward = movementInput.moveForward;
        float strafe = movementInput.moveStrafe;
        float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;

        if ((this.stage == 1) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F))) {
            this.stage = 2;
            this.moveSpeed = (MathUtils.setRandom(0.5, 1.3) * mc.thePlayer.getBaseMoveSpeed() - MathUtils.setRandom(0.01, 0.05));
        } else if (this.stage == 2) {
            this.stage = 3;
            if (mc.thePlayer.onGround) mc.thePlayer.motionY = MathUtils.setRandom(0.3999994, 0.42);
            event.y = MathUtils.setRandom(0.3999994, 0.42);
            this.moveSpeed *= MathUtils.setRandom(1.149D, 2.149D);
        } else if (this.stage == 3) {
            this.stage = 4;
            double difference = MathUtils.setRandom(0.55, 0.77) * (this.lastDist - mc.thePlayer.getBaseMoveSpeed() - MathUtils.setRandom(0.01, 0.05));
            this.moveSpeed = (this.lastDist - difference) * 1.5;
        } else {
            if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
                    mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0)
                    || (mc.thePlayer.isCollidedVertically)) {
                this.stage = 1;
            }
            this.moveSpeed = (this.lastDist - this.lastDist / MathUtils.setRandom(170, 189));
        }

        this.moveSpeed = Math.max(this.moveSpeed, mc.thePlayer.getBaseMoveSpeed() - 0.1);

        if ((forward == 0.0F) && (strafe == 0.0F)) {
            event.x = 0.0D;
            event.z = 0.0D;
        } else if (forward != 0.0F) {
            if (strafe >= 1.0F) {
                yaw += (forward > 0.0F ? -45 : 45);
                strafe = 0.0F;
            } else if (strafe <= -1.0F) {
                yaw += (forward > 0.0F ? 45 : -45);
                strafe = 0.0F;
            }

            if (forward > 0.0F) {
                forward = 1.0F;
            } else if (forward < 0.0F) {
                forward = -1.0F;
            }
        }

        double mx = Math.cos(Math.toRadians(yaw + 90.0F));
        double mz = Math.sin(Math.toRadians(yaw + 90.0F));
        event.x = (forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz);
        event.z = (forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx);
    }
}