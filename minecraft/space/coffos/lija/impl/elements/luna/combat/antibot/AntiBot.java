package space.coffos.lija.impl.elements.luna.combat.antibot;

import net.minecraft.entity.player.EntityPlayer;
import space.coffos.lija.LiJA;
import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.element.ElementStructure;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.api.setting.BooleanHandler;
import space.coffos.lija.api.setting.ModeHandler;
import space.coffos.lija.api.setting.Setting;
import space.coffos.lija.impl.elements.luna.combat.antibot.structure.Advanced;
import space.coffos.lija.impl.events.EventUpdate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhn17
 * <-> 2018-04-16 <-> 18:44
 **/
@ElementStructure(name = "AntiBot", category = Category.COMBAT, clientType = "Luna")
public class AntiBot extends Element {

    public static List<EntityPlayer> invalid = new ArrayList<>();

    @ModeHandler(name = "Mode", currentOption = "Advanced", options = {"Advanced"}, locked = false)
    public static Setting mode;

    @BooleanHandler(name = "Remove", booleanValue = true)
    public static Setting remove;

    @BooleanHandler(name = "Notify", booleanValue = true)
    public static Setting notify;

    private Advanced advanced = new Advanced();

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
        if (mode.getValString().equalsIgnoreCase("Advanced")) {
            setMode(mode.getValString());
            LiJA.INSTANCE.eventManager.register(advanced);
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        Advanced.invalidID.clear();
        LiJA.INSTANCE.eventManager.unregister(advanced);
        super.onDisable();
    }
}
