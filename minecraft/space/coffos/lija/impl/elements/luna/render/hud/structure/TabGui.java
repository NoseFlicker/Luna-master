package space.coffos.lija.impl.elements.luna.render.hud.structure;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import space.coffos.lija.LiJA;
import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.api.setting.Setting;
import space.coffos.lija.impl.events.EventKeyBoard;
import space.coffos.lija.impl.events.EventRender2D;
import space.coffos.lija.util.math.MathUtils;
import space.coffos.lija.util.render.ColorUtils;
import space.coffos.lija.util.render.FontUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static space.coffos.lija.impl.elements.luna.render.hud.HUD.tabGui;

public class TabGui {

    private ArrayList<Category> categoryArrayList;
    private int currentCategorySelection, currentModSelection, currentSettingSelection, screen;
    private boolean editMode;

    private int transition;

    public TabGui() {
        categoryArrayList = new ArrayList<>();
        currentCategorySelection = 0;
        currentModSelection = 0;
        currentSettingSelection = 0;
        editMode = false;
        categoryArrayList.addAll(Arrays.asList(Category.values()));
    }

    private int getTransition() {
        return transition;
    }

    private void setTransition(final int transition) {
        this.transition = transition;
    }

    @EventRegister
    public void onRender2D(EventRender2D e) {

        if (!tabGui.getValBoolean()) return;

        GL11.glPushMatrix();

        drawName(2, 1);
        int startX = 2;
        int startY = 11;

        Gui.drawRect(startX - 1, startY - 1, startX + getWidestCategory() + 4, startY + categoryArrayList.size() * (9 + 2), 0x80000000);

        for (Category c : categoryArrayList) {

            if (getTransition() > 0) setTransition(getTransition() - 1);

            if (getCurrentCategory().equals(c))
                Gui.drawRect(startX, startY, startX + getWidestCategory() + 4 - 1, startY + 9 + 2 - 1, ColorUtils.getColor(ColorUtils.ColorChoices.MAIN));

            FontUtils.drawStringWithShadow(c.name().substring(0, 1).toUpperCase() + c.name().substring(1).toLowerCase(), getCurrentCategory().equals(c) ? startX + 3 : startX + 1, startY + 1, -1);

            startY += 9 + 2;
        }

        if (screen == 1 | screen == 2) {

            int modX = startX + getWidestCategory() + 4 + 3;
            int modY = 11 + (9 + 2) * currentCategorySelection;

            Gui.drawRect(modX - 1, modY - 1, modX + getWidestMod() + 12, modY + getModsForCurrentCategory().size() * (9 + 2), 0x80000000);

            for (Element m : getModsForCurrentCategory()) {

                if (getCurrentModule().equals(m))
                    Gui.drawRect(modX, modY, modX + getWidestMod() + 12 - 1, modY + 9 + 2 - 1, ColorUtils.getColor(ColorUtils.ColorChoices.MAIN));

                if (LiJA.INSTANCE.settingManager.getSettingsByElement(m) != null)
                    FontUtils.drawStringWithShadow("≡", modX + getWidestMod() + 12 - FontUtils.getStringWidth("≡") - 2, modY + 1, new Color(128, 131, 152).getRGB());

                FontUtils.drawStringWithShadow(m.getName(), modX + (getCurrentModule().equals(m) ? 3 : 1), modY + 1, m.isToggled() ? -1 : new Color(128, 131, 152).getRGB());

                modY += 9 + 2;
            }
        }

        if (screen == 2) {

            int setX = (startX + getWidestCategory() + 4 + 3) + getWidestMod() + 12 + 3;
            int modY = (11) + (9 + 2) * (currentCategorySelection);
            int setY = modY + (9 + 2) * (currentModSelection);

            Gui.drawRect(setX - 1, setY - 1, setX + getWidestSetting() + 8, setY + getSettingsForCurrentModule().size() * (9 + 2), 0x80000000);

            for (Setting s : getSettingsForCurrentModule()) {
                if (getCurrentSetting().equals(s))
                    Gui.drawRect(setX, setY, setX + getWidestSetting() + 8 - 1, setY + 9 + 2 - 1, ColorUtils.getColor(ColorUtils.ColorChoices.MAIN));
                if (s.isSlider())
                    FontUtils.drawStringWithShadow(s.getName() + ": " + MathUtils.roundToPlace(s.getValDouble(), 2), setX + (getCurrentSetting().equals(s) ? 3 : 1), setY + 1, editMode & getCurrentSetting().equals(s) ? -1 : new Color(128, 131, 152).getRGB());
                else if (s.isCheck())
                    FontUtils.drawStringWithShadow(s.getName(), setX + (getCurrentSetting().equals(s) ? 3 : 1), setY + 1, s.getValBoolean() ? -1 : new Color(128, 131, 152).getRGB());
                else if (s.isCombo())
                    FontUtils.drawStringWithShadow(s.getName() + ": " + s.getValString(), setX + (getCurrentSetting().equals(s) ? 3 : 1), setY + 1, editMode & getCurrentSetting().equals(s) ? -1 : new Color(128, 131, 152).getRGB());
                setY += 9 + 2;
            }
        }
        GL11.glPopMatrix();
    }

    private void drawName(int x, int y) {
        FontUtils.drawStringWithShadow(ChatFormatting.WHITE + LiJA.INSTANCE.subManager.getClient() + ChatFormatting.RESET + " " + LiJA.INSTANCE.currentFormat + LiJA.INSTANCE.build[1], x, y, ColorUtils.getColor(ColorUtils.ColorChoices.MAIN));
    }

    private void up() {
        if (screen == 0) {
            if (currentCategorySelection > 0)
                currentCategorySelection--;
            else
                currentCategorySelection = categoryArrayList.size() - 1;
        } else if (screen == 1) {
            if (currentModSelection > 0)
                currentModSelection--;
            else
                currentModSelection = getModsForCurrentCategory().size() - 1;
        } else if (screen == 2 & !editMode) {
            if (currentSettingSelection > 0)
                currentSettingSelection--;
            else
                currentSettingSelection = getSettingsForCurrentModule().size() - 1;
        } else if (screen == 2) {
            Setting s = getCurrentSetting();
            if (s.isSlider()) {
                s.setValDouble(s.onlyInt() ? s.getValDouble() + 1 : s.getValDouble() + 0.1);
                if (s.getValDouble() > s.getMax()) s.setValDouble(s.getMax());
            } else if (s.isCombo()) try {
                s.setValString(s.getOptions().get(s.currentIndex() - 1));
            } catch (Exception e) {
                s.setValString(s.getOptions().get(s.getOptions().size() - 1));
            }
            else if (s.isCheck()) s.setValBoolean(!s.getValBoolean());
        }
    }

    private void down() {
        if (screen == 0) {
            if (currentCategorySelection < categoryArrayList.size() - 1)
                currentCategorySelection++;
            else
                currentCategorySelection = 0;
        } else if (screen == 1) {
            if (currentModSelection < getModsForCurrentCategory().size() - 1)
                currentModSelection++;
            else
                currentModSelection = 0;
        } else if (screen == 2 & !editMode) {
            if (currentSettingSelection < getSettingsForCurrentModule().size() - 1)
                currentSettingSelection++;
            else
                currentSettingSelection = 0;
        } else if (screen == 2) {
            Setting s = getCurrentSetting();
            if (s.isSlider()) {
                s.setValDouble(s.onlyInt() ? s.getValDouble() - 1 : s.getValDouble() - 0.1);
                if (s.getValDouble() < s.getMin()) s.setValDouble(s.getMin());
            } else if (s.isCombo()) try {
                s.setValString(s.getOptions().get(s.currentIndex() + 1));
            } catch (Exception e) {
                s.setValString(s.getOptions().get(0));
            }
            else if (s.isCheck()) s.setValBoolean(!s.getValBoolean());
        }
    }

    private void left() {
        if (screen == 1) {
            screen = 0;
            currentModSelection = 0;
        } else if (screen == 2) {
            screen = 1;
            currentSettingSelection = 0;
            editMode = false;
        }
    }

    private void right() {
        if (screen == 0) screen = 1;
        else if (screen == 1 & getCurrentModule() != null & getSettingsForCurrentModule() == null)
            getCurrentModule().toggle();
        else if (screen == 1 & getSettingsForCurrentModule() != null & getCurrentModule() != null)
            screen = 2;
    }

    private void enter() {
        if (screen == 1 & getCurrentModule() != null) getCurrentModule().toggle();
        else if (screen == 2 & getCurrentSetting().isCheck()) {
            Setting s = getCurrentSetting();
            s.setValBoolean(!s.getValBoolean());
        } else if (screen == 2) {
            editMode = !editMode;
        }
    }

    @EventRegister
    public void onKey(EventKeyBoard e) {
        switch (e.getKey()) {
            case Keyboard.KEY_UP:
                up();
                break;
            case Keyboard.KEY_DOWN:
                down();
                break;
            case Keyboard.KEY_RIGHT:
                right();
                break;
            case Keyboard.KEY_LEFT:
                left();
                break;
            case Keyboard.KEY_RETURN:
                enter();
                break;
        }
    }

    private Category getCurrentCategory() {
        return categoryArrayList.get(currentCategorySelection);
    }

    private ArrayList<Element> getModsForCurrentCategory() {
        return LiJA.INSTANCE.elementManager.getElementsForCategory(getCurrentCategory());
    }

    private Element getCurrentModule() {
        return getModsForCurrentCategory().get(currentModSelection);
    }

    private ArrayList<Setting> getSettingsForCurrentModule() {
        return LiJA.INSTANCE.settingManager.getSettingsByElement(getCurrentModule());
    }

    private Setting getCurrentSetting() {
        return getSettingsForCurrentModule().get(currentSettingSelection);
    }

    private int getWidestCategory() {
        int width = 0;
        for (Category c : categoryArrayList)
            if (FontUtils.getStringWidth(c.name()) >= width) width = FontUtils.getStringWidth(c.name());
        return width;
    }

    private int getWidestMod() {
        int width = 0;
        for (Element m : getModsForCurrentCategory())
            if (FontUtils.getStringWidth(m.getName()) >= width)
                width = FontUtils.getStringWidth(m.getName() + " ");
        return width;
    }

    private int getWidestSetting() {
        int width = 0;
        for (Setting s : getSettingsForCurrentModule())
            if (s.isCombo()) {
                if (FontUtils.getStringWidth(s.getName() + ": " + s.getValString()) > width)
                    width = FontUtils.getStringWidth(s.getName() + ": " + s.getValString());
            } else if (s.isSlider()) {
                if (FontUtils.getStringWidth(s.getName() + ": " + MathUtils.roundToPlace(s.getValDouble(), 7)) > width)
                    width = FontUtils.getStringWidth(s.getName() + ": " + MathUtils.roundToPlace(s.getValDouble(), 7));
            } else if (s.isCheck()) if (FontUtils.getStringWidth(s.getName() + ": " + s.getValBoolean()) > width)
                width = FontUtils.getStringWidth(s.getName() + s.getValBoolean());
        return width;
    }
}