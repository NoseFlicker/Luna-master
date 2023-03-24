package space.coffos.lija.impl.elements.luna.movement.flight.structure;

import space.coffos.lija.LiJA;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.impl.elements.luna.movement.flight.Flight;
import space.coffos.lija.impl.events.EventMove;
import space.coffos.lija.impl.events.EventUpdate;
import space.coffos.lija.util.packets.PacketUtils;

import static space.coffos.lija.api.element.Element.mc;
import static space.coffos.lija.impl.elements.luna.movement.flight.Flight.init;


public class HypixelPacket {

    public static boolean shouldContinue;
    public double newPitch, oldPitch;
    public int counter, jumps;

    @EventRegister
    public void onMove(EventMove e) {
        if (!LiJA.INSTANCE.elementManager.getElement(Flight.class).isToggled()) return;
        mc.thePlayer.rotationYaw = counter;
        mc.thePlayer.rotationPitch = (float) newPitch;
        if (!mc.thePlayer.isMoving()) {
            mc.thePlayer.motionX *= 0f;
            mc.thePlayer.motionZ *= 0f;
            mc.timer.allowSyncModify(false);
            mc.timer.resetTimer();
            init = 60;
        }
        mc.thePlayer.rotationPitch = (float) oldPitch;
    }


    @EventRegister
    public void onUpdate(EventUpdate e) {
        counter = (int) mc.thePlayer.rotationYaw;
        oldPitch = mc.thePlayer.rotationPitch;
        mc.thePlayer.stepHeight = 0.0f;
        PacketUtils.setState(true);
        PacketUtils.interceptC03Pos(0, mc.thePlayer.ticksExisted % 2 == 0 ? 1.99E-14D : -1.99E-14D, 0, true);
        mc.thePlayer.motionY = 0;
        mc.thePlayer.onGround = true;
    }
}