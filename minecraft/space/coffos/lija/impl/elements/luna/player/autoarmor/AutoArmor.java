package space.coffos.lija.impl.elements.luna.player.autoarmor;

import space.coffos.lija.LiJA;
import space.coffos.lija.impl.elements.luna.player.autoarmor.structure.New;
import space.coffos.lija.impl.elements.luna.player.autoarmor.structure.Normal;
import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.element.ElementStructure;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.api.setting.ModeHandler;
import space.coffos.lija.api.setting.Setting;
import space.coffos.lija.impl.events.EventUpdate;

/**
 * @author Zhn17
 * <-> 2018-04-15 <-> 20:26
 **/
@ElementStructure(name = "AutoArmor", category = Category.PLAYER, description = "Allows you to auto equip armor.", clientType = "Luna")
public class AutoArmor extends Element {

    @ModeHandler(name = "Mode", currentOption = "Normal", options = {"Normal", "New"}, locked = false)
    public static Setting mode;

    private Normal normal = new Normal();
    private New aNew = new New();

    @EventRegister
    public void onUpdate(EventUpdate event) {
        if (isToggled()) {
            if (!getMode().contains(mode.getValString())) {
                toggle();
                toggle();
            }
        }
    }

    @Override
    public void onEnable() {
        if (mode.getValString().equalsIgnoreCase("Normal")) {
            setMode(mode.getValString());
            normal.timer.reset();
            LiJA.INSTANCE.eventManager.register(normal);
        } else if (mode.getValString().equalsIgnoreCase("New")) {
            setMode(mode.getValString());
            LiJA.INSTANCE.eventManager.register(aNew);
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        LiJA.INSTANCE.eventManager.unregister(normal);
        normal.timer.reset();
        LiJA.INSTANCE.eventManager.unregister(aNew);
        super.onDisable();
    }
}