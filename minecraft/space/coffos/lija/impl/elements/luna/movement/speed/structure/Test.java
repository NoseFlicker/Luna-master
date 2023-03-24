package space.coffos.lija.impl.elements.luna.movement.speed.structure;

import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.impl.events.EventMotion;

import static space.coffos.lija.api.element.Element.mc;

public class Test {

    @EventRegister
    public void onUpdate(EventMotion e) {
        if (mc.thePlayer.isMoving()) {
            mc.thePlayer.setSpeed(0.21324234);
            if (mc.thePlayer.onGround && mc.thePlayer.ticksExisted % 4 == 0) {
                mc.thePlayer.motionY = 0.403234294;
                mc.thePlayer.setSpeed(0.47845634);
            }
            if (!mc.thePlayer.onGround && mc.thePlayer.ticksExisted % 4 == 0) mc.thePlayer.setSpeed(0.59845634);
        } else mc.thePlayer.setSpeed(0);
    }
}