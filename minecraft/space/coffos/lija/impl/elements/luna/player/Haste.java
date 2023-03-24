package space.coffos.lija.impl.elements.luna.player;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import space.coffos.lija.LiJA;
import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.element.ElementStructure;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.impl.events.EventUpdate;

/**
 * @author Zhn17
 * <-> 2018-04-30 <-> 21:27
 **/
@ElementStructure(name = "Haste", category = Category.PLAYER, clientType = "Luna")
public class Haste extends Element {

    @EventRegister
    public void onUpdate(EventUpdate e) {
        mc.thePlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 0, 1));
    }

    @Override
    public void onDisable() {
        if (!LiJA.INSTANCE.isLoading)
            mc.thePlayer.removePotionEffect(3);
    }
}