package space.coffos.lija.util.render;

import space.coffos.lija.impl.elements.luna.render.hud.HUD;

import java.awt.*;

public class ColorUtils {

    public enum ColorChoices {MAIN, SECONDARY}

    public static int getColor(ColorChoices choice) {
        return choice == ColorChoices.MAIN ? new Color((int) HUD.red.getValDouble(), (int) HUD.green.getValDouble(), (int) HUD.blue.getValDouble()).getRGB() : new Color(44, 44, 44).getRGB();
    }
}