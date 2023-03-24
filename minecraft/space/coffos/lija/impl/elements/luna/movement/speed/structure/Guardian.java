package space.coffos.lija.impl.elements.luna.movement.speed.structure;

import net.minecraft.network.play.client.C03PacketPlayer;
import space.coffos.lija.util.math.Timer;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.impl.events.EventMove;
import space.coffos.lija.util.entity.PlayerUtils;

import java.util.stream.IntStream;

import static space.coffos.lija.api.element.Element.mc;

/**
 * @author xdxd
 **/
public class Guardian {

    private Timer timer = new Timer();

    @EventRegister
    public void onMotion(EventMove e) {
        if (timer.hasReached(700L)) {
            e.setCancelled(mc.thePlayer.ticksExisted % 2 == 0);
            IntStream.range(0, 20).forEachOrdered(i -> {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 3.002358, mc.thePlayer.posZ, false));
                PlayerUtils.tpPacket(0, 1.0E-19D, 0, true);
            });
            timer.reset();
        }
        PlayerUtils.setSpeed(e, (int) 2.4);
    }
}