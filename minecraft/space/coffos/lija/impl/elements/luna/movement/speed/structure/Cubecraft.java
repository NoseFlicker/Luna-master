package space.coffos.lija.impl.elements.luna.movement.speed.structure;

import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.impl.events.EventMove;

import static space.coffos.lija.api.element.Element.mc;
import static space.coffos.lija.util.entity.PlayerUtils.setSpeed;

/**
 * @author Zhn17
 * <-> 2018-09-02 <->
 * space.lunaclient.luna.impl.elements.luna.movement.speed.structure
 **/
public class Cubecraft {

    @EventRegister
    public void onMotion(EventMove e) {
        if (mc.thePlayer.onGround & mc.thePlayer.isMoving()) {
            mc.timer.timerSpeed = 0.25f;
            setSpeed(e, (int) 2.0);
        } else
            mc.timer.resetTimer();
    }
}