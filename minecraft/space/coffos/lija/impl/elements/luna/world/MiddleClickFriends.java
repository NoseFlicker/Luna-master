package space.coffos.lija.impl.elements.luna.world;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import space.coffos.lija.LiJA;
import space.coffos.lija.impl.managers.FriendManager;
import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.element.ElementStructure;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.impl.events.EventMiddleClick;
import space.coffos.lija.util.entity.PlayerUtils;

@ElementStructure(name = "MCF", category = Category.WORLD, description = "Allows you to middle click on players to add them as friends.", clientType = "Luna")
public class MiddleClickFriends extends Element {

    @EventRegister
    public void onMiddle(EventMiddleClick e) {
        if (mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mc.objectMouseOver.entityHit instanceof EntityPlayer) {
            String mouseOverName = mc.objectMouseOver.entityHit.getName();
            if (!FriendManager.isFriend(mouseOverName)) {
                LiJA.INSTANCE.friendManager.addFriend(mouseOverName, mouseOverName);
                PlayerUtils.tellPlayer("Added \"" + mouseOverName + "\".", false);
            } else {
                LiJA.INSTANCE.friendManager.deleteFriend(mouseOverName);
                PlayerUtils.tellPlayer("Deleted \"" + mouseOverName + "\".", false);
            }
        }
    }
}