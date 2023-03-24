package space.coffos.lija.impl.elements.luna.movement;

import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.element.ElementStructure;
import space.coffos.lija.api.setting.DoubleHandler;
import space.coffos.lija.api.setting.Setting;

@ElementStructure(name = "WebMove", category = Category.MOVEMENT, clientType = "Luna")
public class WebMove extends Element {

    @DoubleHandler(name = "Speed", currentValue = 0.25D, minValue = 0.1D, maxValue = 2.25D, onlyInt = false, locked = false)
    public static Setting speed;
}