package space.coffos.lija.impl.elements.luna.world;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import space.coffos.lija.LiJA;
import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.element.ElementStructure;
import space.coffos.lija.api.event.Event.Type;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.api.setting.DoubleHandler;
import space.coffos.lija.api.setting.Setting;
import space.coffos.lija.impl.events.EventMotion;
import space.coffos.lija.impl.events.EventUpdate;
import space.coffos.lija.util.entity.MoveUtils;
import space.coffos.lija.util.entity.PlayerUtils;
import space.coffos.lija.util.math.MathUtils;
import space.coffos.lija.util.math.Timer;

import java.util.Arrays;
import java.util.List;

import static space.coffos.lija.util.entity.BlockUtils.onPlayerRightClick;

@ElementStructure(name = "Scaffold", category = Category.WORLD, description = "Places blocks below your feats while walking.", clientType = "Luna")
public class Scaffold extends Element {

    @DoubleHandler(name = "Distance", currentValue = 0.4D, minValue = 0.01D, maxValue = 1.5D, onlyInt = false, locked = false)
    public static Setting distance;

    private List<Block> invalidBlocks = Arrays.asList(Blocks.air, Blocks.water, Blocks.tnt, Blocks.chest, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.tnt, Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore, Blocks.chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt, Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.enchanting_table);
    private BlockData blockData;
    private Timer timer;
    private int slot, newSlot, oldSlot;
    private boolean usesFastMode;

    public Scaffold() {
        this.timer = new Timer();
        this.slot = -1;
    }

    @Override
    public void onEnable() {
        if (LiJA.INSTANCE.isLoading) return;
        oldSlot = mc.thePlayer.inventory.currentItem;
        if (!mc.gameSettings.keyBindSprint.pressed) return;
        usesFastMode = true;
        mc.thePlayer.jump();
        PlayerUtils.tpRel(0, 0.42 * MathUtils.setRandom(0.96, 0.999999999), 0);
        MoveUtils.motionBoostScaffold(1.7, 100, true);
        super.onEnable();
    }

    private static float[] getAngles(Entity e) {
        return new float[]{getYawChangeToEntity(e) + mc.thePlayer.rotationYaw, getPitchChangeToEntity(e) + mc.thePlayer.rotationPitch};
    }

    private static float getYawChangeToEntity(Entity entity) {
        double deltaX = entity.posX - mc.thePlayer.posX;
        double deltaZ = entity.posZ - mc.thePlayer.posZ;
        double yawToEntity;
        if (deltaZ < 0.0D && deltaX < 0.0D) yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
        else
            yawToEntity = deltaZ < 0.0D && deltaX > 0.0D ? -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX)) : Math.toDegrees(-Math.atan(deltaX / deltaZ));

        return MathHelper.wrapAngleTo180_float(-(mc.thePlayer.rotationYaw - (float) yawToEntity));
    }

    private static float getPitchChangeToEntity(Entity entity) {
        double deltaX = entity.posX - mc.thePlayer.posX;
        double deltaZ = entity.posZ - mc.thePlayer.posZ;
        double deltaY = entity.posY - 1.6D + (double) entity.getEyeHeight() - 0.4D - mc.thePlayer.posY;
        double distanceXZ = (double) MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
        return -MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch - (float) pitchToEntity);
    }

    private void swap(int slot, int hotBarNumber) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotBarNumber, 2, mc.thePlayer);
    }

    private boolean grabBlock() {
        for (int i = 0; i < 36; ++i) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && stack.getItem() instanceof ItemBlock) {
                for (int x = 36; x < 45; ++x)
                    try {
                        mc.thePlayer.inventoryContainer.getSlot(x).getStack();
                    } catch (NullPointerException var5) {
                        System.out.println(x - 36);
                        swap(i, x - 36);
                        return true;
                    }
                swap(i, 1);
                return true;
            }
        }
        return false;
    }

    private int isBlockInHotBar() {
        for (int i = 36; i < 45; ++i) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && stack.getItem() instanceof ItemBlock) return i;
        }
        return 0;
    }

    private static float[] getBlockRotations(int x, int y, int z) {
        Entity temp = new EntitySnowball(mc.theWorld);
        temp.posX = (double) x + 0.5D;
        temp.posY = (double) y + 0.5D;
        temp.posZ = (double) z + 0.5D;
        return getAngles(temp);
    }

    private BlockData getBlockData(BlockPos pos) {
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock()))
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock()))
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock()))
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock()))
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock()))
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        BlockPos add = pos.add(-1, 0, 0);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(add.add(-1, 0, 0)).getBlock()))
            return new BlockData(add.add(-1, 0, 0), EnumFacing.EAST);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(add.add(1, 0, 0)).getBlock()))
            return new BlockData(add.add(1, 0, 0), EnumFacing.WEST);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(add.add(0, 0, -1)).getBlock()))
            return new BlockData(add.add(0, 0, -1), EnumFacing.SOUTH);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(add.add(0, 0, 1)).getBlock()))
            return new BlockData(add.add(0, 0, 1), EnumFacing.NORTH);
        BlockPos add2 = pos.add(1, 0, 0);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(add2.add(-1, 0, 0)).getBlock()))
            return new BlockData(add2.add(-1, 0, 0), EnumFacing.EAST);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(add2.add(1, 0, 0)).getBlock()))
            return new BlockData(add2.add(1, 0, 0), EnumFacing.WEST);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(add2.add(0, 0, -1)).getBlock()))
            return new BlockData(add2.add(0, 0, -1), EnumFacing.SOUTH);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(add2.add(0, 0, 1)).getBlock()))
            return new BlockData(add2.add(0, 0, 1), EnumFacing.NORTH);
        BlockPos add3 = pos.add(0, 0, -1);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(add3.add(-1, 0, 0)).getBlock()))
            return new BlockData(add3.add(-1, 0, 0), EnumFacing.EAST);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(add3.add(1, 0, 0)).getBlock()))
            return new BlockData(add3.add(1, 0, 0), EnumFacing.WEST);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(add3.add(0, 0, -1)).getBlock()))
            return new BlockData(add3.add(0, 0, -1), EnumFacing.SOUTH);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(add3.add(0, 0, 1)).getBlock()))
            return new BlockData(add3.add(0, 0, 1), EnumFacing.NORTH);
        BlockPos add4 = pos.add(0, 0, 1);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(add4.add(-1, 0, 0)).getBlock()))
            return new BlockData(add4.add(-1, 0, 0), EnumFacing.EAST);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(add4.add(1, 0, 0)).getBlock()))
            return new BlockData(add4.add(1, 0, 0), EnumFacing.WEST);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(add4.add(0, 0, -1)).getBlock()))
            return new BlockData(add4.add(0, 0, -1), EnumFacing.SOUTH);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(add4.add(0, 0, 1)).getBlock()))
            return new BlockData(add4.add(0, 0, 1), EnumFacing.NORTH);
        return null;
    }

    public static Block getBlock(int x, int y, int z) {
        return mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
    }

    private class BlockData {

        public BlockPos position;
        public EnumFacing face;

        private BlockData(BlockPos position, EnumFacing face) {
            this.position = position;
            this.face = face;
        }
    }

    private int getBlockSlot() {
        for (int i = 36; i < 45; ++i) {
            ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null && itemStack.getItem() instanceof ItemBlock) return i - 36;
        }
        return -1;
    }

    @EventRegister
    public void onUpdate(EventUpdate e) {
        if (!isToggled() || !mc.gameSettings.keyBindSprint.pressed || !usesFastMode) return;
        mc.thePlayer.motionY = 0;
        mc.thePlayer.onGround = true;
        if (mc.thePlayer.ticksExisted % 2 == 0) PlayerUtils.tpRel(0, MathUtils.setRandom(1.0E-12D, 1.0E-5D), 0);
        else PlayerUtils.tpRel(0, -MathUtils.setRandom(1.0E-12D, 1.0E-5D), 0);
    }

    @EventRegister
    public void onMotionUpdate(EventMotion event) {
        if (isToggled()) {
            if (event.getType() == Type.PRE) {
                int block;
                for (block = 0; block < 45; ++block)
                    mc.thePlayer.inventoryContainer.getSlot(block).getStack();

                if (event.getType() == Type.PRE) {
                    int tempSlot = getBlockSlot();
                    if (isBlockInHotBar() == 0) grabBlock();
                    blockData = null;
                    slot = -1;
                    if (tempSlot != -1) {
                        newSlot = getBlockSlot();
                        oldSlot = mc.thePlayer.inventory.currentItem;
                        mc.thePlayer.inventory.currentItem = newSlot;
                        double x2 = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
                        double z2 = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
                        double xOffset = mc.thePlayer.movementInput.moveForward * 0.2 * x2 + mc.thePlayer.movementInput.moveStrafe * 0.35D * z2;
                        double zOffset = mc.thePlayer.movementInput.moveForward * 0.2 * z2 - mc.thePlayer.movementInput.moveStrafe * 0.35D * x2;
                        double x = mc.thePlayer.posX + xOffset;
                        double y = mc.thePlayer.posY - 1.0D;
                        double z = mc.thePlayer.posZ + zOffset;
                        BlockPos blockBelow1 = new BlockPos(x, y, z);
                        mc.thePlayer.inventory.currentItem = oldSlot;
                        /* Randomize the jump-height while you're moving and jumping */
                        if (mc.gameSettings.keyBindJump.pressed & mc.thePlayer.onGround)
                            mc.thePlayer.motionY = MathUtils.setRandom(0.39D, 0.40D);

                        if (mc.theWorld.getBlockState(blockBelow1).getBlock() == Blocks.air) {
                            blockData = getBlockData(blockBelow1);
                            slot = tempSlot;
                            if (blockData != null) {
                                /* Change the players packet yaw and pitch */
                                event.setYaw(getBlockRotations(blockData.position.getX(), blockData.position.getY(), blockData.position.getZ())[0]);
                                event.setPitch(getBlockRotations(blockData.position.getX(), blockData.position.getY(), blockData.position.getZ())[1]);
                            }
                        }
                    }
                }
            } else if (event.getType() == Type.POST && this.blockData != null && this.timer.hasReached(75L) && this.slot != -1) {
                /* Switch to the block slot */
                mc.thePlayer.inventory.currentItem = newSlot;

                /* Place the block and perform a swing-animation server-side */
                if (onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventoryContainer.getSlot(36 + slot).getStack(), blockData.position, blockData.face, new Vec3(blockData.position.getX(), blockData.position.getY(), blockData.position.getZ())))
                    mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());

                /* Switch back to the previous slot */
                mc.thePlayer.inventory.currentItem = oldSlot;

                /* Tower up if the user is holding SPACE */
                if (mc.gameSettings.keyBindJump.pressed & !mc.thePlayer.isMoving()) {
                    mc.thePlayer.motionY = 0.42;
                }
            }
        }
    }

    public void onDisable() {
        MoveUtils.disableBoost();
        usesFastMode = false;
        /* Verify the players slot to prevent any ghost-items */
        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
    }
}