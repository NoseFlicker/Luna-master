package space.coffos.lija.impl.elements.luna.render;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.element.ElementStructure;

@ElementStructure(name = "Chams", category = Category.RENDER, clientType = "Luna")
public class Chams extends Element {

    /**
     * @see net.minecraft.client.renderer.entity.RendererLivingEntity
     */

    public static boolean isCorrect(EntityLivingBase entity) {
        return entity instanceof EntityPlayer & entity != mc.thePlayer;
    }
}