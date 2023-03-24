package space.coffos.lija.impl.elements.luna.movement;

import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.element.ElementStructure;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.impl.events.EventUpdate;

@ElementStructure(name = "Sprint", category = Category.MOVEMENT, clientType = "Luna")
public class Sprint extends Element {

    @EventRegister
    public void onUpdate(EventUpdate e) {
        mc.thePlayer.setSprinting(mc.thePlayer.getFoodStats().getFoodLevel() > 6 & mc.thePlayer.isMoving());
    }
}