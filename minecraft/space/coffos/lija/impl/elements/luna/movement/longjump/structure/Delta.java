package space.coffos.lija.impl.elements.luna.movement.longjump.structure;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.impl.events.EventMotion;
import space.coffos.lija.impl.events.EventMove;
import space.coffos.lija.util.math.MathUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Delta {

    public static double speed, moveSpeed;
    public static int stage, groundTicks;
    private double lastDist;

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @EventRegister
    public void onMotion(EventMotion e) {
        double xDist = Element.mc.thePlayer.posX - Element.mc.thePlayer.prevPosX;
        double zDist = Element.mc.thePlayer.posZ - Element.mc.thePlayer.prevPosZ;
        this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
    }

    @EventRegister
    public void onMove(EventMove event) {
        if (!Element.mc.thePlayer.onGround) this.moveSpeed = Element.mc.thePlayer.getBaseMoveSpeed() / 2;
        MovementInput movementInput = Element.mc.thePlayer.movementInput;
        float forward = movementInput.moveForward;
        float strafe = movementInput.moveStrafe;
        float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;

        if ((this.stage == 1) && ((Element.mc.thePlayer.moveForward != 0.0F) || (Element.mc.thePlayer.moveStrafing != 0.0F))) {
            this.stage = 2;
            this.moveSpeed = (1.38D * Element.mc.thePlayer.getBaseMoveSpeed() - 0.01D) / 1.6;
        } else if (this.stage == 2) {
            this.stage = 3;
            Element.mc.thePlayer.setPosition(Element.mc.thePlayer.posX, Element.mc.thePlayer.posY - 0.001, Element.mc.thePlayer.posZ);
            Element.mc.thePlayer.setPosition(Element.mc.thePlayer.posX, Element.mc.thePlayer.posY + 0.001, Element.mc.thePlayer.posZ);
            Element.mc.thePlayer.jump();
            event.setY(Element.mc.thePlayer.motionY = 0.42 * MathUtils.setRandom(0.95, 0.99));
            this.moveSpeed *= 2.149D;
        } else if (this.stage == 3) {
            this.stage = 4;
            double difference = MathUtils.setRandom(0.65, 0.71) * (lastDist - Element.mc.thePlayer.getBaseMoveSpeed() - 0.04);
            this.moveSpeed = (this.lastDist - difference) * 1.95;
        } else {
            if ((Element.mc.theWorld.getCollidingBoundingBoxes(Element.mc.thePlayer,
                    Element.mc.thePlayer.boundingBox.offset(0.0D, Element.mc.thePlayer.motionY, 0.0D)).size() > 0)
                    || (Element.mc.thePlayer.isCollidedVertically)) {
                this.stage = 1;
            }

            this.moveSpeed = (this.lastDist - this.lastDist / 159.0D);

            if (Element.mc.thePlayer.motionY < 0.1) Element.mc.thePlayer.motionY -= 0.005;
        }

        this.moveSpeed = Math.max(this.moveSpeed, Element.mc.thePlayer.getBaseMoveSpeed());

        if (forward != 0.0F) {
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