package space.coffos.lija.impl.elements.luna.render;

import org.lwjgl.input.Keyboard;
import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.element.ElementStructure;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.impl.events.EventUpdate;

@ElementStructure(name = "ClickGUI", category = Category.RENDER, description = "Shows a custom ClickGUI made by Jacobtread.", keyCode = Keyboard.KEY_RSHIFT)
public class ClickGui extends Element {

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new space.coffos.lija.impl.gui.clickgui.ClickGui());
        super.onEnable();
    }

    @EventRegister
    public void onUpdate(EventUpdate e) {
        toggle();
    }
}