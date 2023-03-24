package space.coffos.lija.impl.elements.luna.render.hud.structure;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import space.coffos.lija.LiJA;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.impl.elements.luna.render.hud.HUD;
import space.coffos.lija.impl.events.EventRender2D;
import space.coffos.lija.impl.managers.FontManager;
import space.coffos.lija.util.render.ColorUtils;
import space.coffos.lija.util.render.FontUtils;
import space.coffos.lija.util.render.RenderUtils;

import java.util.ArrayList;
import java.util.Iterator;

public class ToggledMods {

    @EventRegister
    public void onRender(EventRender2D e) {
        if (HUD.arrayList.getValBoolean()) {
            GlStateManager.pushMatrix();
            if (!Element.mc.gameSettings.showDebugInfo) {
                int yStart = 1;
                int right = RenderUtils.getScaledRes().getScaledWidth();
                int y = 15;
                ArrayList<Element> mods = new ArrayList(LiJA.INSTANCE.elementManager.getToggledElements()); /* Grab elements */
                String themeString = HUD.theme.getValString();
                switch (themeString) {
                    default:
                        mods.sort((o1, o2) -> FontManager.standard.getStringWidth(o2.getDisplayName()) - FontManager.standard.getStringWidth(o1.getDisplayName()));

                    case "Modern": {
                        mods.sort((o1, o2) -> LiJA.INSTANCE.fontManager.ico_regularT2.getStringWidth(o2.getDisplayName()) - LiJA.INSTANCE.fontManager.ico_regularT2.getStringWidth(o1.getDisplayName()));
                        break;
                    }
                }

                for (Iterator var6 = mods.iterator(); var6.hasNext(); yStart += FontUtils.getFontHeight() + 1) {
                    Element element = (Element) var6.next();
                    if (element.getTransition() > 0) element.setTransition(element.getTransition() - 1);
                    int startX = themeString.equalsIgnoreCase("Modern") ? e.getWidth() - LiJA.INSTANCE.fontManager.ico_regularT2.getStringWidth(element.getDisplayName()) - 6 : e.getWidth() - FontManager.standard.getStringWidth(element.getDisplayName()) - 6;
                    if (!HUD.rectangle.getValString().equalsIgnoreCase("None")) {
                        /* Draw Elements on the screen, included with rectangles. */
                        Gui.drawRect(startX + element.getTransition(), yStart - 1, e.getWidth(), yStart + FontUtils.getFontHeight(), ColorUtils.getColor(ColorUtils.ColorChoices.SECONDARY));
                        Gui.drawRect(e.getWidth() - 2 + element.getTransition(), (double) yStart - 1.5D, e.getWidth(), yStart + FontUtils.getFontHeight(), themeString.equalsIgnoreCase("Modern") ? HUD.rainbowArrayList.getValBoolean() ? RenderUtils.getRainbow(6000, yStart * y, 0.5f).getRGB() : -1 : HUD.rainbowArrayList.getValBoolean() ? RenderUtils.getRainbow(6000, yStart * y).getRGB() : ColorUtils.getColor(ColorUtils.ColorChoices.MAIN));
                        Gui.drawVerticalLine(startX - 1 + element.getTransition(), yStart - 2, yStart + FontUtils.getFontHeight(), themeString.equalsIgnoreCase("Modern") ? HUD.rainbowArrayList.getValBoolean() ? RenderUtils.getRainbow(6000, yStart * y, 0.5f).getRGB() : -1 : HUD.rainbowArrayList.getValBoolean() ? RenderUtils.getRainbow(6000, yStart * y).getRGB() : ColorUtils.getColor(ColorUtils.ColorChoices.MAIN));
                        Gui.drawHorizontalLine(startX - 1 + element.getTransition(), e.getWidth(), yStart + FontUtils.getFontHeight(), themeString.equalsIgnoreCase("Modern") ? HUD.rainbowArrayList.getValBoolean() ? RenderUtils.getRainbow(6000, yStart * y, 0.5f).getRGB() : -1 : HUD.rainbowArrayList.getValBoolean() ? RenderUtils.getRainbow(6000, yStart * y).getRGB() : ColorUtils.getColor(ColorUtils.ColorChoices.MAIN));
                    }
                    if (HUD.theme.getValString().equalsIgnoreCase("Modern"))
                        LiJA.INSTANCE.fontManager.ico_regularT2.drawStringWithShadow(element.getDisplayName(), (float) (right + 15 - LiJA.INSTANCE.fontManager.ico_regularT2.getStringWidth(element.getDisplayName()) - 17.5 + element.getTransition() - 1), (float) (yStart - 1),  HUD.rainbowArrayList.getValBoolean() ? RenderUtils.getRainbow(6000, yStart * y, 0.5f).getRGB() : -1);
                    if (HUD.theme.getValString().equalsIgnoreCase("Luna"))
                        FontManager.standard.drawStringWithShadow(element.getDisplayName(), (float) (right + 15 - FontManager.standard.getStringWidth(element.getDisplayName()) - 18 + element.getTransition() - 1), (float) (yStart - 1), HUD.rainbowArrayList.getValBoolean() ? RenderUtils.getRainbow(6000, yStart * y).getRGB() : ColorUtils.getColor(ColorUtils.ColorChoices.MAIN));
                }
                GlStateManager.popMatrix();
            }
        }
    }
}