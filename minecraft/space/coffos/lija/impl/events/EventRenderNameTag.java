package space.coffos.lija.impl.events;

import net.minecraft.entity.Entity;
import space.coffos.lija.api.event.Event;

/**
 * @author Zhn17
 * <-> 2018-04-15 <-> 20:02
 **/
public class EventRenderNameTag extends Event {

    public static boolean cancel;
    private Entity entity;

    public EventRenderNameTag(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return this.entity;
    }
}