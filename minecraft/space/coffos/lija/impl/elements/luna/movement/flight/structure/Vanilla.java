package space.coffos.lija.impl.elements.luna.movement.flight.structure;

import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.impl.elements.luna.movement.flight.Flight;
import space.coffos.lija.impl.events.EventUpdate;

import static space.coffos.lija.api.element.Element.mc;

/**
 * @author Zhn17
 * <-> 2018-08-02 <->
 * space.lunaclient.luna.impl.elements.luna.movement.flight.structure
 **/
public class Vanilla {

    @EventRegister
    public void onUpdate(EventUpdate e) {
        mc.thePlayer.onGround = true;
        mc.thePlayer.capabilities.isFlying = true;
        mc.thePlayer.setSpeed(mc.thePlayer.isMoving() ? Flight.speed.getValDouble() : 0);
    }
}