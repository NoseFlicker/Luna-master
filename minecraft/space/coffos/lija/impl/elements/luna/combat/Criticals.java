package space.coffos.lija.impl.elements.luna.combat;

import space.coffos.lija.LiJA;
import space.coffos.lija.impl.elements.luna.movement.speed.Speed;
import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.element.ElementStructure;
import space.coffos.lija.impl.elements.luna.movement.flight.Flight;
import space.coffos.lija.impl.elements.luna.movement.longjump.LongJump;

@ElementStructure(name = "Criticals", category = Category.COMBAT, clientType = "Luna")
public class Criticals extends Element {

    static boolean canCrit() {
        return mc.thePlayer.onGround & !LiJA.INSTANCE.elementManager.getElement(Speed.class).isToggled() & !LiJA.INSTANCE.elementManager.getElement(LongJump.class).isToggled() & !LiJA.INSTANCE.elementManager.getElement(Flight.class).isToggled();
    }
}
