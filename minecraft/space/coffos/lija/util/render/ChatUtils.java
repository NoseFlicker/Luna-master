package space.coffos.lija.util.render;

import static space.coffos.lija.api.element.Element.mc;

public class ChatUtils {

    public static String chatHistoryString;
    private static boolean doesExist;
    public static boolean cancel;

    public static void printMessage(String requestMSG, String printMSG) {
        if (chatHistoryString.contains(requestMSG)) {
            doesExist = true;
            if (mc.thePlayer.ticksExisted % 5 == 0)
                mc.thePlayer.sendChatMessage(printMSG);
            doesExist = false;
        }
    }

    public static void printMessage(String requestMSG, String printMSG, String secondMSG) {
        if (chatHistoryString.contains(requestMSG)) {
            doesExist = true;
            if (mc.thePlayer.ticksExisted % 5 == 0)
                mc.thePlayer.sendChatMessage(printMSG);
            if (doesExist & mc.thePlayer.ticksExisted % 5 == 0)
                mc.thePlayer.sendChatMessage(secondMSG);
            doesExist = false;
        }
    }
}