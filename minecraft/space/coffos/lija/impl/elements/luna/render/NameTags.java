package space.coffos.lija.impl.elements.luna.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;
import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.element.ElementStructure;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.api.setting.BooleanHandler;
import space.coffos.lija.api.setting.Setting;
import space.coffos.lija.impl.events.EventRender3D;
import space.coffos.lija.impl.events.EventRenderNameTag;
import space.coffos.lija.impl.managers.FriendManager;
import space.coffos.lija.util.render.GLUtil;
import space.coffos.lija.util.render.RenderUtils;

import java.util.List;
import java.util.function.IntFunction;

/**
 * @author Zhn17
 * <-> 2018-04-15 <-> 19:37
 **/
@ElementStructure(name = "NameTags", category = Category.RENDER, description = "Makes nametags bigger.", clientType = "Luna")
public class NameTags extends Element {

    @BooleanHandler(name = "Names", booleanValue = true)
    public Setting names;

    public boolean formatting = true;

    @EventRegister
    public void onEventRender3D(EventRender3D event) {
        List playerEntities = mc.theWorld.playerEntities;
        IntFunction<Object> mapper = playerEntities::get;
        int bound = playerEntities.size();
        for (int i = 0; i < bound; i++) {
            Object o = mapper.apply(i);
            EntityPlayer p = (EntityPlayer) o;
            if (p != mc.func_175606_aa() & p.isEntityAlive()) {
                double pX = p.lastTickPosX + (p.posX - p.lastTickPosX) * mc.timer.renderPartialTicks - RenderManager.renderPosX;
                double pY = p.lastTickPosY + (p.posY - p.lastTickPosY) * mc.timer.renderPartialTicks - RenderManager.renderPosY;
                double pZ = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * mc.timer.renderPartialTicks - RenderManager.renderPosZ;
                renderNameTag(p, formatting ? p.getDisplayName().getFormattedText() : p.getName(), pX, pY, pZ);
            }
        }
    }

    @EventRegister
    private void renderNameTag(EntityPlayer entity, String tag, double pX, double pY, double pZ) {
        pY += (entity.isSneaking() ? 0.5D : 0.7D);
        float var13 = mc.thePlayer.getDistanceToEntity(entity) / 4.0F;
        if (var13 < 1.6F) var13 = 1.6F;
        int colour = 16777215;
        if (!formatting) tag = EnumChatFormatting.getTextWithoutFormattingCodes(tag);

        if (FriendManager.isFriend(entity.getName())) colour = 0xff09d3c9;
        else if (entity.isInvisible()) colour = 16756480;
        else if (entity.isSneaking()) colour = 11468900;

        double health = Math.ceil(entity.getHealth() + entity.getAbsorptionAmount()) / 2.0D;
        EnumChatFormatting healthCol = health < 5.0D ? EnumChatFormatting.DARK_RED : (health > 5.0D) & (health < 6.5D) ? EnumChatFormatting.GOLD : EnumChatFormatting.DARK_GREEN;
        if (names.getValBoolean()) {
            tag = entity.getName();
            tag = Math.floor(health) == health ? tag + " " + healthCol + (int) Math.floor(health) : tag + " " + healthCol + health;
        } else tag = "" + healthCol + (int) Math.floor(health);
        float scale = var13 * 2.0F;
        scale /= 100.0F;
        GL11.glPushMatrix();
        GL11.glTranslatef((float) pX, (float) pY + 1.4F, (float) pZ);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(RenderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-scale, -scale, scale);
        GLUtil.setGLCap(2896, false);
        GLUtil.setGLCap(2929, false);
        int width = mc.fontRendererObj.getStringWidth(tag) / 2;
        GLUtil.setGLCap(3042, true);
        GL11.glBlendFunc(770, 771);

        RenderUtils.drawBorderedRectNameTag(-width - 2, -(mc.fontRendererObj.FONT_HEIGHT + 1), width + 2, 2.0F, 1.0F, -16777216, 1593835520);
        mc.fontRendererObj.func_175065_a(tag, -width, -(mc.fontRendererObj.FONT_HEIGHT - 1), colour, true);
        GL11.glPushMatrix();
        int xOffset = 0;
        for (ItemStack armourStack : entity.inventory.armorInventory)
            if (armourStack != null)
                xOffset -= 8;
        ItemStack renderStack;
        if (entity.getHeldItem() != null) {
            xOffset -= 8;
            renderStack = entity.getHeldItem().copy();
            if (renderStack.hasEffect() & renderStack.getItem() instanceof ItemTool | renderStack.getItem() instanceof ItemArmor)
                renderStack.stackSize = 1;
            renderItemStack(renderStack, xOffset);
            xOffset += 16;
        }
        for (ItemStack armourStack : entity.inventory.armorInventory)
            if (armourStack != null) {
                ItemStack renderStack1 = armourStack.copy();
                if (renderStack1.hasEffect() & renderStack1.getItem() instanceof ItemTool | renderStack1.getItem() instanceof ItemArmor)
                    renderStack1.stackSize = 1;
                renderItemStack(renderStack1, xOffset);
                xOffset += 16;
            }
        GL11.glPopMatrix();
        GLUtil.revertAllCaps();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    @EventRegister
    private void renderItemStack(ItemStack stack, int x) {
        GL11.glPushMatrix();
        GL11.glDepthMask(true);
        GlStateManager.clear(256);
        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
        mc.getRenderItem().zLevel = -150.0F;
        fixItemGlint();
        mc.getRenderItem().func_180450_b(stack, x, -26);
        mc.getRenderItem().func_175030_a(mc.fontRendererObj, stack, x, -26);
        mc.getRenderItem().zLevel = 0.0F;
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5D, 0.5D, 0.5D);
        GlStateManager.disableDepth();
        renderEnchantText(stack, x);
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        GL11.glPopMatrix();
    }

    @EventRegister
    private void renderEnchantText(ItemStack stack, int x) {
        int encY = -26 - 24;
        if (stack.getItem() instanceof ItemArmor) {
            int pLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180310_c.effectId, stack);
            int tLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
            int uLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            if (pLevel > 0) {
                mc.fontRendererObj.drawString("p" + pLevel, x * 2, encY, 16777215);
                encY += 7;
            }
            if (tLevel > 0) {
                mc.fontRendererObj.drawString("t" + tLevel, x * 2, encY, 16777215);
                encY += 7;
            }
            if (uLevel > 0) {
                mc.fontRendererObj.drawString("u" + uLevel, x * 2, encY, 16777215);
                encY += 7;
            }
        }
        if (stack.getItem() instanceof ItemBow) {
            int sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
            int kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
            int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
            int uLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            if (sLevel > 0) {
                mc.fontRendererObj.drawString("d" + sLevel, x * 2, encY, 16777215);
                encY += 7;
            }
            if (kLevel > 0) {
                mc.fontRendererObj.drawString("k" + kLevel, x * 2, encY, 16777215);
                encY += 7;
            }
            if (fLevel > 0) {
                mc.fontRendererObj.drawString("f" + fLevel, x * 2, encY, 16777215);
                encY += 7;
            }
            if (uLevel > 0) {
                mc.fontRendererObj.drawString("u" + uLevel, x * 2, encY, 16777215);
                encY += 7;
            }
        }
        if (stack.getItem() instanceof ItemSword) {
            int sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180314_l.effectId, stack);
            int kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180313_o.effectId, stack);
            int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
            int uLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            if (sLevel > 0) {
                mc.fontRendererObj.drawString("s" + sLevel, x * 2, encY, 16777215);
                encY += 7;
            }
            if (kLevel > 0) {
                mc.fontRendererObj.drawString("k" + kLevel, x * 2, encY, 16777215);
                encY += 7;
            }
            if (fLevel > 0) {
                mc.fontRendererObj.drawString("f" + fLevel, x * 2, encY, 16777215);
                encY += 7;
            }
            if (uLevel > 0) {
                mc.fontRendererObj.drawString("u" + uLevel, x * 2, encY, 16777215);
            }
        }
    }

    private void fixItemGlint() {
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.func_179090_x();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.func_179098_w();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
    }

    @Override
    public void onEnable() {
        EventRenderNameTag.cancel = true;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        EventRenderNameTag.cancel = false;
        super.onDisable();
    }
}