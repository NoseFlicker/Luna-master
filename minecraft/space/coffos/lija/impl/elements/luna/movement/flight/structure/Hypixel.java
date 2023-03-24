package space.coffos.lija.impl.elements.luna.movement.flight.structure;

import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.impl.events.EventUpdate;
import space.coffos.lija.util.entity.PlayerUtils;
import space.coffos.lija.util.math.MathUtils;

public class Hypixel {

    @EventRegister
    public void onUpdate(EventUpdate e) {
        Element.mc.thePlayer.stepHeight = 0.0f;
        Element.mc.thePlayer.motionY = 0;
        Element.mc.thePlayer.onGround = true;
        if (Element.mc.thePlayer.ticksExisted % 2 == 0) PlayerUtils.tpRel(0, MathUtils.setRandom(1.0E-12D, 1.0E-6D), 0);
        else PlayerUtils.tpRel(0, -MathUtils.setRandom(1.0E-12D, 1.0E-6D), 0);
    }
}