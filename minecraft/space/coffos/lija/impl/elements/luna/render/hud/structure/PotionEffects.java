package space.coffos.lija.impl.elements.luna.render.hud.structure;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import space.coffos.lija.util.render.FontUtils;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.impl.events.EventRender2D;

import java.util.Collection;

public class PotionEffects {

    public Minecraft mc = Minecraft.getMinecraft();

    @EventRegister
    public void onRender(EventRender2D e) {

        GL11.glPushMatrix();
        int size = 16;
        int margin = -5;
        float x = (e.getWidth() - size * 2) - margin;
        float y = (e.getHeight() - size * 2) - margin;
        Collection var4 = mc.thePlayer.getActivePotionEffects();
        int i = 0;
        if (!var4.isEmpty()) {
            for (Object o : mc.thePlayer.getActivePotionEffects()) {
                PotionEffect var7 = (PotionEffect) o;
                Potion var8 = Potion.potionTypes[var7.getPotionID()];
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
                if (var8.hasStatusIcon()) {
                    int var9 = var8.getStatusIconIndex();
                    Gui.INSTANCE.drawTexturedModalRect((int) x, (int) y - (18 * i), var9 % 8 * 18, 198 + var9 / 8 * 18, 18, 18);
                    FontUtils.drawStringWithShadow("" + (var7.getDuration() <= 300 ? ChatFormatting.RED : ChatFormatting.WHITE) + Potion.getDurationString(var7), (double) x - FontUtils.getStringWidth("" + Potion.getDurationString(var7)) - 5, (double) y - (18 * i) + 6, -1);
                    i++;
                }
            }
        }
        GL11.glPopMatrix();
    }
}