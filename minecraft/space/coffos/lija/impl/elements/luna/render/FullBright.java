package space.coffos.lija.impl.elements.luna.render;

import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.element.ElementStructure;

@ElementStructure(name = "FullBright", category = Category.RENDER, description = "Makes your brightness very bright.")
public class FullBright extends Element {

    private float gamaSetting;

    @Override
    public void onEnable() {
        super.onEnable();
        gamaSetting = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting = 200;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.gameSettings.gammaSetting = gamaSetting;
    }
}