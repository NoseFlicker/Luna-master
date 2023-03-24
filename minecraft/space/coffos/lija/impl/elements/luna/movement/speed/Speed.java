package space.coffos.lija.impl.elements.luna.movement.speed;

import net.minecraft.init.Blocks;
import space.coffos.lija.LiJA;
import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.element.ElementStructure;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.api.setting.DoubleHandler;
import space.coffos.lija.api.setting.ModeHandler;
import space.coffos.lija.api.setting.Setting;
import space.coffos.lija.impl.elements.luna.movement.speed.structure.*;
import space.coffos.lija.impl.events.EventUpdate;
import space.coffos.lija.util.entity.BlockUtils;
import space.coffos.lija.util.entity.MoveUtils;
import space.coffos.lija.util.entity.PlayerUtils;
import space.coffos.lija.util.math.MathUtils;
import space.coffos.lija.util.packets.PacketUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Zhn17
 * <-> 2018-04-16 <-> 09:08
 **/
@ElementStructure(name = "Speed", category = Category.MOVEMENT, description = "Allows you to run faster.", clientType = "Luna")
public class Speed extends Element {

    public static int yOffset;

    @ModeHandler(name = "Mode", currentOption = "Bhop", options = {"yPort", "Bhop", "OnGround", "Cubecraft", "Guardian", "New", "Test", "Hypixel Ground Boost"}, locked = false)
    public static Setting mode;

    @DoubleHandler(name = "yPort Speed", currentValue = 1.2D, minValue = 0.6D, maxValue = 5.0D, onlyInt = false, locked = false)
    public static Setting vSpeed;

    @DoubleHandler(name = "Bhop Speed", currentValue = 1.55D, minValue = 1.05D, maxValue = 2.53D, onlyInt = false, locked = false)
    public static Setting bSpeed;

    private yPort yport = new yPort();
    private Bhop bhop = new Bhop();
    private Test test = new Test();
    private Guardian guardian = new Guardian();
    private OnGround onGround = new OnGround();
    private Cubecraft cubecraft = new Cubecraft();
    private HypixelGroundBoost hg = new HypixelGroundBoost();
    private New aNew = new New();

    @Override
    public void onEnable() {
        if (LiJA.INSTANCE.isLoading || mc.thePlayer == null || mc.theWorld == null) return;
        if (mode.getValString().equalsIgnoreCase("yPort")) {
            setMode(mode.getValString());
            yPort.state.set(0);
            if (mc.thePlayer.onGround & !LiJA.INSTANCE.isLoading & mc.theWorld != null)
                yOffset = (int) mc.thePlayer.posY;
            LiJA.INSTANCE.eventManager.register(yport);
        } else if (mode.getValString().equalsIgnoreCase("Bhop")) {
            setMode(mode.getValString());
            Bhop.stage = 0;
            mc.thePlayer.motionX = 0.0D;
            mc.thePlayer.motionZ = 0.0D;
            LiJA.INSTANCE.eventManager.register(bhop);
        } else if (mode.getValString().equalsIgnoreCase("OnGround")) {
            setMode(mode.getValString());
            onGround.move = false;
            onGround.hop = false;
            LiJA.INSTANCE.eventManager.register(onGround);
        } else if (mode.getValString().equalsIgnoreCase("Guardian")) {
            setMode(mode.getValString());
            mc.thePlayer.motionX = 0.0D;
            mc.thePlayer.motionZ = 0.0D;
            LiJA.INSTANCE.eventManager.register(guardian);
        } else if (mode.getValString().equalsIgnoreCase("Cubecraft")) {
            setMode(mode.getValString());
            LiJA.INSTANCE.eventManager.register(cubecraft);
        } else if (mode.getValString().equalsIgnoreCase("New")) {
            setMode(mode.getValString());
            MoveUtils.motionBoost(3.0, 75);
            LiJA.INSTANCE.eventManager.register(aNew);
        } else if (mode.getValString().equalsIgnoreCase("Test")) {
            setMode(mode.getValString());
            LiJA.INSTANCE.eventManager.register(test);
        } else if (mode.getValString().equalsIgnoreCase("Hypixel Ground Boost")) {
            if (BlockUtils.getBlockUnderPlayer(mc.thePlayer, 0.82) != Blocks.air) {
                mc.thePlayer.jump();
                PlayerUtils.tpRel(0, 0.42 * MathUtils.setRandom(0.96, 0.999999999), 0);
                MoveUtils.motionBoost(2.2, 80);
                LiJA.INSTANCE.eventManager.register(hg);
            }
            mc.timer.resetTimer();
            super.onEnable();
        }
    }

    @Override
    public void onDisable() {
        mc.thePlayer.stepHeight = 0.6f;
        if (mode.getValString().equalsIgnoreCase("yPort"))
            mc.thePlayer.motionY = -0.4;
        mc.timer.resetTimer();
        MoveUtils.disableBoost();
        PacketUtils.setState(false);
        LiJA.INSTANCE.eventManager.unregister(bhop);
        LiJA.INSTANCE.eventManager.unregister(yport);
        LiJA.INSTANCE.eventManager.unregister(onGround);
        LiJA.INSTANCE.eventManager.unregister(guardian);
        LiJA.INSTANCE.eventManager.unregister(cubecraft);
        LiJA.INSTANCE.eventManager.unregister(aNew);
        LiJA.INSTANCE.eventManager.unregister(test);
        LiJA.INSTANCE.eventManager.unregister(hg);
        mc.thePlayer.distanceWalkedModified = 0;
    }

    @EventRegister
    public void onUpdate(EventUpdate event) {
        if (isToggled()) {
            if (!getMode().contains(mode.getValString())) {
                toggle();
                toggle();
            }
        } else this.setMode(mode.getValString());
    }

    public double round(double value, int places) {
        assert places >= 0;
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}