package space.coffos.lija.util.entity;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.*;
import optifine.Reflector;
import space.coffos.lija.LiJA;
import space.coffos.lija.impl.events.EventMove;

import java.util.List;

import static net.minecraft.client.Minecraft.getMinecraft;
import static space.coffos.lija.api.element.Element.mc;

public class PlayerUtils {

    public static void tellPlayer(String text, boolean IRCPrefix) {
        mc.ingameGUI.getChatGUI().printChatMessage(IRCPrefix ? new ChatComponentText(ChatFormatting.DARK_GRAY + "[" + ChatFormatting.GRAY + "IRC" + ChatFormatting.DARK_GRAY + "] " + ChatFormatting.GRAY + text) : new ChatComponentText(ChatFormatting.DARK_GRAY + "[" + ChatFormatting.GRAY + LiJA.INSTANCE.subManager.getClient() + ChatFormatting.DARK_GRAY + "] " + ChatFormatting.GRAY + text));
    }

    public static void updatePosition(EntityPlayer player, String NormalPacket) {
        if (NormalPacket.equalsIgnoreCase("NORMAL"))
            player.setPositionAndUpdate(player.posX, player.posY, player.posZ);
        else
            tpPacket(player.posX, player.posY, player.posZ, player.onGround);
    }

    public static void setSpeed(EventMove e, int speed) {
        double forward = MovementInput.moveForward;
        double strafe = MovementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            e.setX(0.0);
            e.setZ(0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) yaw += ((forward > 0.0) ? -45 : 45);
                else if (strafe < 0.0) yaw += ((forward > 0.0) ? 45 : -45);
                strafe = 0.0;
                if (forward > 0.0) forward = 1.0;
                else if (forward < 0.0) forward = -1.0;
            }
            double cos = Math.cos(Math.toRadians(yaw + 90.0f));
            double sin = Math.sin(Math.toRadians(yaw + 90.0f));
            e.setX(forward * speed * cos + strafe * speed * sin);
            e.setZ(forward * speed * sin - strafe * speed * cos);
        }
    }

    public static void tpRel(double x, double y, double z) {
        if (!LiJA.INSTANCE.isLoading)
            mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z);
    }

    public static void tpPacket(double x, double y, double z, boolean ground) {
        if (mc.thePlayer != null && mc.theWorld != null)
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z, ground));
    }

    public static ScaledResolution getScaledRes() {
        return new ScaledResolution(getMinecraft(), getMinecraft().displayWidth, getMinecraft().displayHeight);
    }

    /*
     * Cred to Cirex
     */

    public static Entity rayCast(Entity entity, float pitch, float yaw) {
        if (mc.thePlayer != null & mc.theWorld != null) {
            double var3 = 6.0D;
            Vec3 var78 = mc.thePlayer.func_174824_e(mc.timer.renderPartialTicks);
            Vec3 var88 = getLook(pitch, yaw);
            Vec3 var68 = var78.addVector(var88.xCoord * var3, var88.yCoord * var3, var88.zCoord * var3);
            MovingObjectPosition objectMouseOver = mc.theWorld.rayTraceBlocks(var78, var68, false, false, true);
            double var5 = var3;
            Vec3 var7 = mc.thePlayer.getPositionVector().add(new Vec3(0.0D, mc.thePlayer.getEyeHeight(), 0.0D));

            var3 = 6.0D;
            if (objectMouseOver != null) var5 = objectMouseOver.hitVec.distanceTo(var7);
            Vec3 var8 = getLook(mc.thePlayer.rotationPitch, mc.thePlayer.rotationYaw);
            Vec3 var9 = new Vec3(entity.posX, entity.posY, entity.posZ);
            Entity pointedEntity = null;
            float var11 = 1.0F;
            List var12 = mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().addCoord(var8.xCoord * var3, var8.yCoord * var3, var8.zCoord * var3).expand(var11, var11, var11));
            double var13 = var5;
            for (int var15 = 0; var15 < var12.size(); var15++) {
                Entity var16 = (Entity) var12.get(var15);
                if (var16.canBeCollidedWith()) {
                    float var17 = var16.getCollisionBorderSize();
                    AxisAlignedBB var18 = var16.getEntityBoundingBox().expand(var17, var17, var17);
                    MovingObjectPosition var19 = var18.calculateIntercept(var7, var9);
                    if (var18.isVecInside(var7)) {
                        if (0.0D < var13 | var13 == 0.0D) {
                            pointedEntity = var16;
                            var13 = 0.0D;
                        }
                    } else if (var19 != null) {
                        double var20 = var7.distanceTo(var19.hitVec);
                        if (var20 < var13 | var13 == 0.0D) {
                            boolean canRiderInteract = false;
                            if (Reflector.ForgeEntity_canRiderInteract.exists())
                                canRiderInteract = Reflector.callBoolean(var15, Reflector.ForgeEntity_canRiderInteract);
                            if (var16 == mc.thePlayer.ridingEntity & !canRiderInteract) {
                                if (var13 == 0.0D) pointedEntity = var16;
                            } else {
                                pointedEntity = var16;
                                var13 = var20;
                            }
                        }
                    }
                }
            }
            if (pointedEntity != null & var13 < var5 | mc.objectMouseOver == null) {
                assert pointedEntity != null;
                pointedEntity.setInvisible(false);
                return pointedEntity;
            }
        }
        return null;
    }

    public static Vec3 getLook(float p_174806_1_, float p_174806_2_) {
        float var3 = MathHelper.cos(-p_174806_2_ * 0.017453292F - 3.1415927F);
        float var4 = MathHelper.sin(-p_174806_2_ * 0.017453292F - 3.1415927F);
        float var5 = -MathHelper.cos(-p_174806_1_ * 0.017453292F);
        float var6 = MathHelper.sin(-p_174806_1_ * 0.017453292F);
        return new Vec3(var4 * var5, var6, var3 * var5);
    }

    public static float getDirection() {
        float yaw = mc.thePlayer.rotationYawHead;
        float forward = mc.thePlayer.moveForward;
        float strafe = mc.thePlayer.moveStrafing;
        yaw += (forward < 0.0F ? 180 : 0);
        int i = forward < 0.0F ? -45 : forward == 0.0F ? 90 : 45;
        if (strafe < 0.0F) yaw += i;
        if (strafe > 0.0F) yaw -= i;
        return yaw * 0.017453292F;
    }
}