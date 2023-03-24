package space.coffos.lija.impl.elements.luna.movement.longjump;

import space.coffos.lija.LiJA;
import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.element.ElementStructure;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.api.setting.ModeHandler;
import space.coffos.lija.api.setting.Setting;
import space.coffos.lija.impl.elements.luna.movement.longjump.structure.Delta;
import space.coffos.lija.impl.elements.luna.movement.longjump.structure.Normal;
import space.coffos.lija.impl.events.EventUpdate;

@ElementStructure(name = "Longjump", category = Category.MOVEMENT, clientType = "Luna")
public class LongJump extends Element {

    @ModeHandler(name = "Mode", currentOption = "Normal", options = {"Normal", "Delta"}, locked = false)
    public static Setting mode;

    private Normal n = new Normal();
    private Delta d = new Delta();

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
        if (LiJA.INSTANCE.isLoading) return;
        if (mode.getValString().equalsIgnoreCase("Normal")) {
            setMode(mode.getValString());
            mc.thePlayer.addedToChunk = false;
            LiJA.INSTANCE.eventManager.register(n);
        } else if (mode.getValString().equalsIgnoreCase("Delta")) {
            setMode(mode.getValString());
            LiJA.INSTANCE.eventManager.register(d);
            Delta.moveSpeed = mc.thePlayer.getBaseMoveSpeed();
            Delta.speed = 1;
            Delta.groundTicks = 8;
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.001, mc.thePlayer.posZ);
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.001, mc.thePlayer.posZ);
            Delta.stage = 0;
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        LiJA.INSTANCE.eventManager.unregister(n);
        LiJA.INSTANCE.eventManager.unregister(d);
        super.onDisable();
    }
}