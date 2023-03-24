package space.coffos.lija.impl.elements.luna.player;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S30PacketWindowItems;
import space.coffos.lija.util.math.Timer;
import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.element.ElementStructure;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.impl.events.EventUpdate;

import java.util.Objects;
import java.util.stream.IntStream;

/**
 * @author Zhn17
 * <-> 2018-04-15 <-> 20:30
 **/
@ElementStructure(name = "ChestStealer", category = Category.PLAYER, clientType = "Luna")
public class ChestStealer extends Element {

    public static S30PacketWindowItems packet;
    private Timer timer = new Timer();
    private int delay;

    @EventRegister
    public void onUpdate(EventUpdate event) {
        this.delay += 1;
        if ((mc.currentScreen instanceof GuiChest)) {
            GuiChest chest = (GuiChest) mc.currentScreen;
            if (isChestEmpty(chest) | isInvFull()) {
                mc.thePlayer.closeScreen();
                packet = null;
            }
            int bound = chest.lowerChestInventory.getSizeInventory();
            for (int index = 0; index < bound; index++) {
                ItemStack stack = chest.lowerChestInventory.getStackInSlot(index);
                //if(stack.getDisplayName().contains("Skywars") | stack.getDisplayName().contains("Default")) return;
                if ((stack != null) && timer.hasReached(120)) {
                    mc.playerController.windowClick(chest.inventorySlots.windowId, index, 0, 1, mc.thePlayer);
                    timer.reset();
                }
            }
        }
    }

    private boolean isChestEmpty(GuiChest chest) {
        return IntStream.rangeClosed(0, chest.lowerChestInventory.getSizeInventory()).mapToObj(index -> chest.lowerChestInventory.getStackInSlot(index)).noneMatch(Objects::nonNull);
    }

    private boolean isInvFull() {
        return IntStream.rangeClosed(0, mc.thePlayer.inventory.getSizeInventory()).mapToObj(index -> mc.thePlayer.inventory.getStackInSlot(index)).noneMatch(stack -> stack.stackSize < 36);
    }
}