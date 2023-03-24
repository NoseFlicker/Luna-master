package space.coffos.lija.impl.managers;

import net.minecraft.util.StringUtils;
import space.coffos.lija.LiJA;
import space.coffos.lija.api.friend.Friend;
import space.coffos.lija.api.manager.Handler;
import space.coffos.lija.impl.files.FriendsFile;

public class FriendManager extends Handler<Friend> {

    public void addFriend(String name, String alias) {
        getContents().add(new Friend(name, alias));
        saveFile();
    }

    public void deleteFriend(String name) {
        for (Friend friend : getContents())
            if (friend.getName().equalsIgnoreCase(StringUtils.stripControlCodes(name)) || friend.getAlias().equalsIgnoreCase(StringUtils.stripControlCodes(name))) {
                getContents().remove(friend);
                saveFile();
                break;
            }
    }

    public static boolean isFriend(String name) {
        return LiJA.INSTANCE.friendManager.getContents().stream().anyMatch(friend -> friend.getName().equalsIgnoreCase(StringUtils.stripControlCodes(name)) || friend.getAlias().equalsIgnoreCase(StringUtils.stripControlCodes(name)));
    }

    private void saveFile() {
        try {
            LiJA.INSTANCE.fileManager.getFile(FriendsFile.class).saveFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}