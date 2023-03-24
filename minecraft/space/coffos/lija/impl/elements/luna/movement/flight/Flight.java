package space.coffos.lija.impl.elements.luna.movement.flight;

import net.minecraft.server.MinecraftServer;
import space.coffos.lija.LiJA;
import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.element.ElementStructure;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.api.setting.BooleanHandler;
import space.coffos.lija.api.setting.DoubleHandler;
import space.coffos.lija.api.setting.ModeHandler;
import space.coffos.lija.api.setting.Setting;
import space.coffos.lija.impl.elements.luna.movement.flight.structure.HypixelPacket;
import space.coffos.lija.impl.elements.luna.movement.flight.structure.Vanilla;
import space.coffos.lija.impl.events.EventUpdate;
import space.coffos.lija.util.entity.MoveUtils;
import space.coffos.lija.util.entity.PlayerUtils;
import space.coffos.lija.util.math.MathUtils;
import space.coffos.lija.util.math.Timer;
import space.coffos.lija.impl.elements.luna.movement.flight.structure.CubeCraft;
import space.coffos.lija.impl.elements.luna.movement.flight.structure.Hypixel;
import space.coffos.lija.impl.elements.luna.movement.flight.structure.list.Boost;
import space.coffos.lija.util.packets.PacketUtils;

@ElementStructure(name = "Flight", category = Category.MOVEMENT, description = "Allows you to fly.", clientType = "Luna")
public class Flight extends Element {

    public static double startX, startY, startZ;
    public static boolean lock;
    public static int init;

    @ModeHandler(name = "Mode", currentOption = "Hypixel", options = {"Vanilla", "Hypixel", "Hypixel Packet", "Hypixel Test", "CubeCraft"}, locked = false)
    public static Setting mode;

    @BooleanHandler(name = "Hypixel Boost", booleanValue = false)
    public static Setting fast;

    @DoubleHandler(name = "Vanilla Speed", currentValue = 1.0D, minValue = 0.1D, maxValue = 10.0D, onlyInt = false, locked = true)
    public static Setting speed;

    private CubeCraft cubeCraft = new CubeCraft();
    private Hypixel hypixel = new Hypixel();
    private Vanilla vanilla = new Vanilla();
    private HypixelPacket packet = new HypixelPacket();
    private Boost boost = new Boost();

    private Timer timer = new Timer();

    @Override
    public void onEnable() {
        if (LiJA.INSTANCE.isLoading || mc.thePlayer == null || LiJA.INSTANCE.subManager.getClient().equalsIgnoreCase("LiJA")) return;
        setMode(mode.getValString());
        switch (mode.getValString().toLowerCase()) {
            case "hypixel packet":
                packet.oldPitch = mc.thePlayer.rotationPitch;
                packet.newPitch = 70;
                packet.jumps = (int) mc.thePlayer.rotationYaw;
                packet.shouldContinue = false;
                mc.thePlayer.jump();
                PlayerUtils.tpRel(0, 0.42 * MathUtils.setRandom(0.96, 0.999999999), 0);
                if (timer.hasReached(200)) {
                    MoveUtils.motionBoost(2, 90);
                    timer.reset();
                }
                LiJA.INSTANCE.eventManager.register(packet);
                init = 60;
                break;
            case "hypixel":
                startX = mc.thePlayer.posX;
                startY = mc.thePlayer.posY;
                startZ = mc.thePlayer.posZ;
                System.out.println(MinecraftServer.getServer());
                if (fast.getValBoolean()) {
                    mc.thePlayer.jump();
                    PlayerUtils.tpRel(0, 0.42 * MathUtils.setRandom(0.96, 0.999999999), 0);
                    if (timer.hasReached(200)) {
                        MoveUtils.motionBoost(2, 20);
                        timer.reset();
                    }
                } else {
                    mc.thePlayer.jump();
                    PlayerUtils.tpRel(0, 0.42 * MathUtils.setRandom(0.96, 0.999999999), 0);
                }
                LiJA.INSTANCE.eventManager.register(hypixel);
                init = 60;
                break;
            case "cubecraft":
                LiJA.INSTANCE.eventManager.register(cubeCraft);
                break;
            case "vanilla":
                LiJA.INSTANCE.eventManager.register(vanilla);
                break;
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.thePlayer.capabilities.isFlying = false;
        mc.thePlayer.stepHeight = 0.6f;
        mc.timer.resetTimer();
        mc.thePlayer.motionX *= 0.0D;
        mc.thePlayer.motionZ *= 0.0D;
        MoveUtils.disableBoost();
        mc.thePlayer.speedInAir = 0.02F;
        if (mode.getValString().equalsIgnoreCase("Hypixel Packet")) PacketUtils.setState(false);
        LiJA.INSTANCE.eventManager.unregister(hypixel);
        LiJA.INSTANCE.eventManager.unregister(boost);
        LiJA.INSTANCE.eventManager.unregister(cubeCraft);
        LiJA.INSTANCE.eventManager.unregister(vanilla);
        LiJA.INSTANCE.eventManager.unregister(packet);
        super.onDisable();
    }

    @EventRegister
    public void onUpdate(EventUpdate event) {
        if (!isToggled()) return;
        if (speed.isLockedDouble() & mode.getValString().equalsIgnoreCase("Vanilla"))
            speed.setLockedDouble(false);
        else if (!speed.isLockedDouble() & !mode.getValString().equalsIgnoreCase("Vanilla")) {
            speed.setLockedDouble(true);
            toggle();
            toggle();
        }
        if (!getMode().contains(mode.getValString())) {
            toggle();
            toggle();
        }
    }
}