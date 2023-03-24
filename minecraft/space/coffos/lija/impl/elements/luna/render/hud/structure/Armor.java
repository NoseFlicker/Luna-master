package space.coffos.lija.impl.elements.luna.render.hud.structure;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.*;
import org.lwjgl.opengl.GL11;
import space.coffos.lija.util.render.FontUtils;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.impl.elements.luna.render.hud.HUD;
import space.coffos.lija.impl.events.EventRender2D;

public class Armor {

    public Minecraft mc = Minecraft.getMinecraft();

    @EventRegister
    public void onRender(EventRender2D e) {
        if (!HUD.armor.getValBoolean())
            return;

        int itemX = e.getWidth() / 2 + 9;
        for (int i = 0; i < 5; i++) {
            ItemStack ia = mc.thePlayer.getEquipmentInSlot(i);
            if (ia == null) continue;

            float oldZ = mc.getRenderItem().zLevel;
            GL11.glPushMatrix();
            GlStateManager.clear(GL11.GL_ACCUM);
            GlStateManager.disableAlpha();
            RenderHelper.enableStandardItemLighting();
            mc.getRenderItem().zLevel = -100;
            mc.getRenderItem().func_175042_a(ia, itemX, e.getHeight() - 55);
            mc.getRenderItem().func_175030_a(mc.fontRendererObj, ia, itemX, e.getHeight() - 55);
            mc.getRenderItem().zLevel = oldZ;
            RenderHelper.disableStandardItemLighting();
            GlStateManager.enableAlpha();
            GL11.glPopMatrix();

            if (ia.getItem() instanceof ItemSword | ia.getItem() instanceof ItemTool | ia.getItem() instanceof ItemArmor | ia.getItem() instanceof ItemBow) {
                GlStateManager.pushMatrix();
                int durability = ia.getMaxDamage() - ia.getItemDamage();
                int y = e.getHeight() - 60;
                GlStateManager.scale(0.5, 0.5, 0.5);
                FontUtils.drawStringWithShadow(durability + "", (itemX + 4) / (float) 0.5, ((float) y) / (float) 0.5, 0xffffff);
                GlStateManager.popMatrix();
            }
            itemX += 16;
        }
    }
}