package space.coffos.lija.impl.elements.luna.movement.speed.structure;

import net.minecraft.init.Blocks;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.impl.events.EventUpdate;
import space.coffos.lija.util.entity.BlockUtils;
import space.coffos.lija.util.entity.PlayerUtils;
import space.coffos.lija.util.math.MathUtils;

import static space.coffos.lija.api.element.Element.mc;

public class HypixelGroundBoost {

    @EventRegister
    public void onUpdate(EventUpdate e) {
        if (BlockUtils.getBlockUnderPlayer(mc.thePlayer, 0.82) != Blocks.air) {
            mc.thePlayer.stepHeight = 0.0f;
            mc.thePlayer.motionY = 0;
            mc.thePlayer.onGround = true;
            if (mc.thePlayer.ticksExisted % 2 == 0) PlayerUtils.tpRel(0, MathUtils.setRandom(1.0E-12D, 1.0E-5D), 0);
            else PlayerUtils.tpRel(0, -MathUtils.setRandom(1.0E-12D, 1.0E-5D), 0);
        }
    }
}