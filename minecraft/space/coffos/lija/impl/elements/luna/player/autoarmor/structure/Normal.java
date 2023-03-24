package space.coffos.lija.impl.elements.luna.player.autoarmor.structure;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import space.coffos.lija.util.math.MathUtils;
import space.coffos.lija.util.math.Timer;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.impl.events.EventUpdate;

import java.util.stream.IntStream;

import static space.coffos.lija.api.element.Element.mc;

/**
 * @author Zhn17
 * <-> 2018-04-19 <-> 09:11
 **/
public class Normal {

    private int delay;
    public Timer timer = new Timer();

    @EventRegister
    public void onUpdate(EventUpdate e) {
        delay++;
        int[] bestArmor1 = IntStream.range(0, 4).map(i -> -1).toArray();
        for (int i1 = 0; i1 < 36; i1++) {
            ItemStack itemstack = mc.thePlayer.inventory.getStackInSlot(i1);
            if (itemstack != null && itemstack.getItem() instanceof ItemArmor) {
                ItemArmor armor = (ItemArmor) itemstack.getItem();
                if (armor.damageReduceAmount > bestArmor1[(3 - armor.armorType)])
                    bestArmor1[(3 - armor.armorType)] = i1;
            }
        }
        for (int i = 0; i < 4; i++) {
            ItemStack itemstack = mc.thePlayer.inventory.armorItemInSlot(i);
            ItemArmor currentArmor;
            if (itemstack != null && itemstack.getItem() instanceof ItemArmor)
                currentArmor = (ItemArmor) itemstack.getItem();
            else currentArmor = null;
            ItemArmor bestArmor;
            try {
                bestArmor = (ItemArmor) mc.thePlayer.inventory.getStackInSlot(bestArmor1[i]).getItem();
            } catch (Exception exception) {
                bestArmor = null;
            }
            if (delay >= 10) delay = 0;
            if (bestArmor != null && (currentArmor == null || bestArmor.damageReduceAmount > currentArmor.damageReduceAmount) && (mc.thePlayer.inventory.getFirstEmptyStack() != -1 || currentArmor == null) && timer.hasReached((long) MathUtils.setRandom(100, 500))) {
                mc.playerController.windowClick(0, 8 - i, 0, 1, mc.thePlayer);
                mc.playerController.windowClick(0, bestArmor1[i] < 9 ? 36 + bestArmor1[i] : bestArmor1[i], 0, 1, mc.thePlayer);
                timer.reset();
            }
        }
    }
}