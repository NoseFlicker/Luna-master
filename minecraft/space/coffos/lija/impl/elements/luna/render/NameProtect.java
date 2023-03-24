package space.coffos.lija.impl.elements.luna.render;

import space.coffos.lija.util.math.MathUtils;
import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.element.ElementStructure;

/**
 * @author Zhn17
 * <-> 2018-05-06 <-> 15:55
 **/
@ElementStructure(name = "NameProtect", category = Category.RENDER, clientType = "Luna")
public class NameProtect extends Element {

    public static String name = "LiJA " + MathUtils.getRandom(258932);
}