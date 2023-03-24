package space.coffos.lija.impl.elements.luna.player;

import net.minecraft.client.gui.inventory.GuiContainer;
import org.lwjgl.input.Keyboard;
import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.element.ElementStructure;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.impl.events.EventUpdate;

import java.util.stream.Stream;

@ElementStructure(name = "InvMove", category = Category.PLAYER, description = "Allows you to move in your inventory.", clientType = "Luna")
public class InvMove extends Element {

    @EventRegister
    public void onUpdate(EventUpdate e) {
        if (!(mc.currentScreen instanceof GuiContainer)) return;
        Stream.of(mc.gameSettings.keyBindRight, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindForward, mc.gameSettings.keyBindJump).forEachOrdered(keyBinding -> keyBinding.pressed = Keyboard.isKeyDown(keyBinding.getKeyCode()));
    }
}