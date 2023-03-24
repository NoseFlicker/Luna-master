package space.coffos.lija.impl.elements.luna.movement.speed.structure;

import space.coffos.lija.api.event.Event;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.impl.events.EventMotion;

import static space.coffos.lija.api.element.Element.mc;

public class OnGround {

    /*
     * @author TheBlackGolem/Zephyr
     */

    private double prevY;
    public boolean move;
    public boolean hop;

    @EventRegister
    public void onUpdate(EventMotion e) {
        if (e.getType() != Event.Type.POST)
            return;
        onGround();
    }

    private void onGround() {
        if ((this.hop) & (mc.thePlayer.posY >= this.prevY + 0.399994D)) {
            mc.thePlayer.motionY = -0.9D;
            mc.thePlayer.posY = this.prevY;
            this.hop = false;
        }
        if ((mc.thePlayer.moveForward != 0.0F) & (!mc.thePlayer.isCollidedHorizontally) & (!mc.thePlayer.isEating())) {
            if ((mc.thePlayer.moveForward == 0.0F) & (mc.thePlayer.moveStrafing == 0.0F)) {
                mc.thePlayer.motionX = 0.0D;
                mc.thePlayer.motionZ = 0.0D;
                if (mc.thePlayer.isCollidedVertically) {
                    mc.thePlayer.jump();
                    this.move = true;
                }
                if ((this.move) & (mc.thePlayer.isCollidedVertically)) this.move = false;
            }
            if (mc.thePlayer.isCollidedVertically) {
                mc.thePlayer.motionX *= 1.0379D;
                mc.thePlayer.motionZ *= 1.0379D;
                jump();
            }
            if ((this.hop) & (!this.move) & (mc.thePlayer.posY >= this.prevY + 0.399994D)) {
                mc.thePlayer.motionY = -100.0D;
                mc.thePlayer.posY = this.prevY;
                this.hop = false;
            }
        }
    }

    private void jump() {
        this.hop = true;
        this.prevY = mc.thePlayer.posY;
        mc.thePlayer.jump();
    }
}