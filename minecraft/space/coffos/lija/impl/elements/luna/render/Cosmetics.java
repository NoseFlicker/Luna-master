package space.coffos.lija.impl.elements.luna.render;

import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.element.ElementStructure;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.api.setting.BooleanHandler;
import space.coffos.lija.api.setting.DoubleHandler;
import space.coffos.lija.api.setting.Setting;
import space.coffos.lija.impl.events.EventUpdate;

@ElementStructure(name = "Cosmetics", category = Category.RENDER, clientType = "Luna")
public class Cosmetics extends Element {

    @BooleanHandler(name = "Wings", booleanValue = true)
    public static Setting wings;

    @BooleanHandler(name = "Ears", booleanValue = false)
    public static Setting ears;

    @BooleanHandler(name = "Resize", booleanValue = false)
    public static Setting resize;

    @DoubleHandler(name = "Player Size", currentValue = 0.3D, minValue = 0.3D, maxValue = 2.0D, onlyInt = false, locked = true)
    public static Setting sizeMNG;

    @BooleanHandler(name = "FastChat", booleanValue = false)
    public static Setting fastChat;

    @EventRegister
    public void onUpdate(EventUpdate e) {
        if (resize.getValBoolean() & sizeMNG.isLockedDouble()) {
            sizeMNG.setLockedDouble(false);
            toggle();
            toggle();
        } else if (!resize.getValBoolean() & !sizeMNG.isLockedDouble()) {
            sizeMNG.setLockedDouble(true);
            toggle();
            toggle();
        }
    }
}