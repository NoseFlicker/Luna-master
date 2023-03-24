package space.coffos.lija.impl.elements.luna.movement;

import net.minecraft.block.BlockAir;
import space.coffos.lija.util.entity.BlockUtils;
import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.element.ElementStructure;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.impl.events.EventUpdate;

@ElementStructure(name = "HighJump", category = Category.MOVEMENT, clientType = "Luna")
public class HighJump extends Element {

    private boolean canJump;
    public boolean jumping;
    private int ticks;

    @EventRegister
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer.onGround) {
            canJump = true;
            jumping = false;
        }

        boolean voidUnder = BlockUtils.getBlockUnderPlayer(mc.thePlayer, 22) instanceof BlockAir & !mc.thePlayer.onGround;

        ticks++;

        if (canJump && voidUnder && !jumping && mc.thePlayer.fallDistance > 2.1f) {
            jumping = true;
            canJump = false;
            mc.thePlayer.motionY = -7;
            ticks = 0;
        }

        if (jumping && ticks == 3) {
            mc.thePlayer.motionY = 3.6;
            mc.thePlayer.motionX *= 1.5f;
            mc.thePlayer.motionZ *= 1.5f;
        }

        if (jumping && ticks == 4) {
            mc.thePlayer.motionX *= 1.1f;
            mc.thePlayer.motionZ *= 1.1f;
        }

        if (jumping && ticks > 7) {
            canJump = jumping = false;
            ticks = 0;
        }
    }

    public void onDisable() {
        jumping = false;
        super.onDisable();
    }
}