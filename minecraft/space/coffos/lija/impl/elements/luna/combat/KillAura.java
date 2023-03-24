package space.coffos.lija.impl.elements.luna.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import space.coffos.lija.LiJA;
import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.element.ElementStructure;
import space.coffos.lija.api.event.Event;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.api.setting.BooleanHandler;
import space.coffos.lija.api.setting.DoubleHandler;
import space.coffos.lija.api.setting.ModeHandler;
import space.coffos.lija.api.setting.Setting;
import space.coffos.lija.impl.elements.luna.combat.antibot.AntiBot;
import space.coffos.lija.impl.elements.luna.combat.antibot.structure.Advanced;
import space.coffos.lija.impl.events.EventMotion;
import space.coffos.lija.impl.managers.FriendManager;
import space.coffos.lija.util.entity.RotationUtils;
import space.coffos.lija.util.math.MathUtils;
import space.coffos.lija.util.math.Timer;
import space.coffos.lija.impl.elements.luna.movement.flight.Flight;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.IntFunction;

@ElementStructure(name = "KillAura", category = Category.COMBAT, description = "Attacks entities around you.", clientType = "Luna")
public class KillAura extends Element {

    public static boolean block;
    public static Timer time = new Timer();
    public static EntityLivingBase target;
    public static ArrayList entities = new ArrayList();
    @ModeHandler(name = "Sorting", currentOption = "Direct", options = {"Direct", "Optimal", "Random"}, locked = false)
    public static Setting sorting;
    @ModeHandler(name = "Priority", currentOption = "Angle", options = {"Angle", "Armor", "Range"}, locked = false)
    public static Setting priority;
    @DoubleHandler(name = "CPS", currentValue = 14.0D, minValue = 1.0D, maxValue = 20.0D, onlyInt = false, locked = false)
    public static Setting cps;
    @DoubleHandler(name = "Range", currentValue = 4.5D, minValue = 1.0D, maxValue = 10.0D, onlyInt = false, locked = false)
    public static Setting range;
    @DoubleHandler(name = "Ticks Existed", currentValue = 50.0D, minValue = 0.0D, maxValue = 500.0D, onlyInt = true, locked = false)
    public static Setting ticks;
    @BooleanHandler(name = "Invisible", booleanValue = false)
    public static Setting invisible;
    @BooleanHandler(name = "Non-Players", booleanValue = false)
    public static Setting creatures;
    @BooleanHandler(name = "AutoBlock", booleanValue = true)
    public static Setting autoBlock;
    public static ArrayList targets = new ArrayList();
    @BooleanHandler(name = "Inventory", booleanValue = false)
    private static Setting inventory;
    private static boolean isBlocking;
    @BooleanHandler(name = "Walls", booleanValue = false)
    private static Setting walls;
    @BooleanHandler(name = "Friend", booleanValue = false)
    private static Setting friend;

    private static boolean canBlock() {
        bone:
        {
            if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword & autoBlock.getValBoolean() && mc.thePlayer.isSwingInProgress)
                break bone;
            if (!mc.thePlayer.isBlocking()) return false;
        }
        return true;
    }

    private static int compare(EntityLivingBase o1, EntityLivingBase o2) {
        float[] rot1 = RotationUtils.getRotations(o1);
        float[] rot2 = RotationUtils.getRotations(o2);
        return (int) (mc.thePlayer.rotationYaw - rot1[0] - mc.thePlayer.rotationYaw - rot2[0]);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        time.reset();
        timer.reset();
        target = null;
    }

    @Override
    public void onDisable() {
        if (mc.thePlayer != null) mc.playerController.releaseItem();
        mc.gameSettings.keyBindUseItem.pressed = true;
        mc.gameSettings.keyBindUseItem.unpressKey();
        isBlocking = false;
        target = null;
        mc.gameSettings.keyBindUseItem.pressed = false;
        super.onDisable();
    }

    @EventRegister
    public void onUpdate(EventMotion ev) {
        if (LiJA.INSTANCE.elementManager.getElement(Flight.class).isToggled()) return;
        block = autoBlock.getValBoolean() && canBlock() && target != null;
        if (entities.isEmpty()) {
            IntFunction<Object> mapper = mc.theWorld.loadedEntityList::get;
            int bound = mc.theWorld.loadedEntityList.size();
            for (int i = 0; i < bound; i++) {
                Object object = mapper.apply(i);
                if (object instanceof EntityLivingBase) {
                    EntityLivingBase entity = (EntityLivingBase) object;
                    if (isValid(entity)) entities.add(entity);
                }
            }
        }

        if (!entities.isEmpty()) {
            EntityLivingBase entity = null;

            for (int i = 0; i < entities.size(); ++i) {
                Entity e = (Entity) entities.get(i);
                if (!isValid(e)) entities.remove(e);

                if (e.getDistanceToEntity(mc.thePlayer) <= range.getValDouble())
                    entity = (EntityLivingBase) e;

                target = sorting.getValString().equalsIgnoreCase("Direct") ? entity : mc.thePlayer.isBlocking() ? entity : getOptimalTarget();
            }

            if (target != null) {
                if (mc.currentScreen != null & inventory.getValBoolean()) return;
                float[] rotations = RotationUtils.getRotations(target);
                ev.setYaw((rotations[0]));
                ev.setPitch((rotations[1]));
            }

            float kill = (float) (MathUtils.setRandom(1005.0D, 1025.0D) / cps.getValDouble());
            if (isValid(target) & time.hasReached((long) kill) & ev.getType() == Event.Type.PRE & !isBlocking) {
                if (autoBlock.getValBoolean()) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
                            C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    block = false;
                }
                attack(target);
                time.reset();
                if (!sorting.getValString().equalsIgnoreCase("Random")) return;
                target = null;
                targets.clear();
            } else if (ev.getType() == Event.Type.POST) {
                block = autoBlock.getValBoolean() && canBlock() && target != null;
                if (autoBlock.getValBoolean() && canBlock()) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-.410153517, -.4083644, -.4186343), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                } else {
                    mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                }
            }
        }
    }

    public void attack(EntityLivingBase entity) {
        if (Criticals.canCrit() && LiJA.INSTANCE.elementManager.getElement(Criticals.class).isToggled() && timer.hasReached(500)) {
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0625, mc.thePlayer.posZ, false));
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
            timer.reset();
        }
        mc.thePlayer.swingItem();
        mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
    }

    private EntityLivingBase getOptimalTarget() {
        List loadedEntityList = mc.theWorld.getLoadedEntityList();
        IntFunction<Object> mapper = loadedEntityList::get;
        List<EntityLivingBase> load = new ArrayList<>();
        for (int i = 0; i < loadedEntityList.size(); i++) {
            Object o = mapper.apply(i);
            if (o instanceof EntityLivingBase) {
                EntityLivingBase entityLivingBase = (EntityLivingBase) o;
                if (isValid(entityLivingBase)) load.add(entityLivingBase);
            }
        }
        return load.isEmpty() ? null : getTarget(load);
    }

    private void sortList(List<EntityLivingBase> entityList) {
        String current = priority.getValString();
        switch (current) {
            case "Range":
                entityList.sort((o1, o2) -> (int) (o1.getDistanceToEntity(mc.thePlayer) - o2.getDistanceToEntity(mc.thePlayer)));
                break;
            case "Angle":
                entityList.sort(KillAura::compare);
                break;
            case "Health":
                entityList.sort((o1, o2) -> (int) (o1.getHealth() - o2.getHealth()));
                break;
            case "Armor":
                entityList.sort(Comparator.comparingInt(o -> (o instanceof EntityPlayer ? ((EntityPlayer) o).inventory.getTotalArmorValue() : (int) o.getHealth())));
                break;
        }
    }

    private EntityLivingBase getTarget(List<EntityLivingBase> list) {
        sortList(list);
        return list.isEmpty() ? null : list.get(0);
    }

    private boolean isValid(Entity entity) {
        if (!(entity instanceof EntityLivingBase)) return false;

        if ((double) mc.thePlayer.getDistanceToEntity(entity) > range.getValDouble() | entity.posY < 1 | !entity.isEntityAlive() | Advanced.invalidID.contains(entity.getEntityId()) | entity == mc.thePlayer | !mc.thePlayer.canEntityBeSeen(entity) & !walls.getValBoolean() | FriendManager.isFriend(entity.getName()) & friend.getValBoolean() | entity instanceof EntityArmorStand | (double) entity.ticksExisted < ticks.getValDouble() | entity.isInvisible() & !invisible.getValBoolean() | AntiBot.invalid.contains(entity))
            return false;
        return !(entity instanceof EntityMob) & !(entity instanceof EntityAnimal) & !(entity instanceof EntityVillager) & !(entity instanceof EntityAmbientCreature) & !(entity instanceof EntityCreature) | creatures.getValBoolean();
    }
}