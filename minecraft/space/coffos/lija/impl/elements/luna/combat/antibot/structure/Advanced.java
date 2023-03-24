package space.coffos.lija.impl.elements.luna.combat.antibot.structure;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.Entity;
import space.coffos.lija.impl.elements.luna.combat.antibot.AntiBot;
import space.coffos.lija.impl.managers.FriendManager;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.impl.events.EventUpdate;
import space.coffos.lija.util.entity.PlayerUtils;

import java.util.ArrayList;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

import static space.coffos.lija.api.element.Element.mc;

/**
 * @author Zhn17
 * <-> 2018-04-18 <-> 13:15
 **/
public class Advanced {

    /**
     * TODO; Fix Invalid-Entity.
     */

    public static ArrayList<Integer> invalidID = new ArrayList<>();

    private static void accept(Entity entity) {
        if (AntiBot.notify.getValBoolean())
            PlayerUtils.tellPlayer(ChatFormatting.GRAY + "Removed a bot with the ID: " + entity.getEntityId() + ", name: " + entity.getName(), false);
        if (AntiBot.remove.getValBoolean()) mc.theWorld.removeEntity(entity);
    }

    @EventRegister
    public void onUpdate(EventUpdate event) {
        IntStream.range(0, mc.theWorld.loadedEntityList.size()).mapToObj((IntFunction<Object>) mc.theWorld.loadedEntityList::get).map(o -> (Entity) o).filter(entity -> entity.isInvisible() & !FriendManager.isFriend(entity.getName())).filter(entity -> !(entity == mc.thePlayer) & mc.thePlayer.ticksExisted > 290 & entity.ticksExisted > 120).forEachOrdered(Advanced::accept);
    }
}