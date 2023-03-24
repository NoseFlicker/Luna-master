package space.coffos.lija.api.element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.network.Packet;
import net.minecraft.util.StringUtils;
import space.coffos.lija.LiJA;
import space.coffos.lija.impl.elements.luna.render.hud.HUD;
import space.coffos.lija.impl.gui.notifications.ClientNotification;
import space.coffos.lija.impl.gui.notifications.NotificationUtil;
import space.coffos.lija.util.math.Timer;

public abstract class Element {

    public static Minecraft mc = Minecraft.getMinecraft();
    public static FontRenderer fr = mc.fontRendererObj;
    public final Timer timer = new Timer();

    private int transition;

    private Category category = getClass().getAnnotation(ElementStructure.class).category();
    private String name = getClass().getAnnotation(ElementStructure.class).name();
    private int keyCode = getClass().getAnnotation(ElementStructure.class).keyCode();
    private String description = getClass().getAnnotation(ElementStructure.class).description();
    private boolean toggled = getClass().getAnnotation(ElementStructure.class).toggled();
    private String clientType = getClass().getAnnotation(ElementStructure.class).clientType();
    private String mode = "";

    public int getTransition() {
        return this.transition;
    }

    public void setTransition(int transition) {
        this.transition = transition;
    }

    public void onEnable() {
        if (HUD.notifications.getValBoolean())
            NotificationUtil.sendClientMessage("Enabled " + this.getName(), 4000, ClientNotification.Type.SUCCESS);
        timer.reset();
        LiJA.INSTANCE.eventManager.register(this);
        mc.timer.resetTimer();
        this.transition = mc.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(this.getName())) - 5;
    }


    public void onDisable() {
        if (getName().contains("Longjump"))
            mc.gameSettings.keyBindJump.pressed = false;
        if (HUD.notifications.getValBoolean())
            NotificationUtil.sendClientMessage("Disabled " + this.getName(), 4000, ClientNotification.Type.ERROR);
        timer.reset();
        LiJA.INSTANCE.eventManager.unregister(this);
        mc.timer.allowSyncModify(true);
        mc.timer.setLastSync(1, 1);
        mc.timer.allowSyncModify(false);
        mc.timer.resetTimer();
        this.transition = mc.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(this.getName())) + 10;
    }

    public void sendPacket(Packet packet) {
        mc.thePlayer.sendQueue.addToSendQueue(packet);
    }

    public void toggle() {
        if (LiJA.INSTANCE.isLoading && mc.thePlayer == null) return;
        if (!isCompatible()) {
            onDisable();
            toggled = false;
            return;
        }
        if (toggled) onDisable();
        else onEnable();
        toggled = !toggled;
    }

    public String getDisplayName() {
        return mode.equalsIgnoreCase("") ? name : name + " " + mode;
    }

    public void setMode(String mode) {
        this.mode = "§7" + mode;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keBind) {
        this.keyCode = keBind;
    }

    public boolean isToggled() {
        return toggled;
    }

    public String getMode() {
        return mode;
    }

    public String getClientType() {
        return clientType;
    }

    public boolean isCompatible() {
        return clientType.equalsIgnoreCase(LiJA.INSTANCE.subManager.getClient()) || clientType.equalsIgnoreCase("All");
    }
}