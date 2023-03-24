package space.coffos.lija.util.entity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldSettings;
import space.coffos.lija.LiJA;

import static space.coffos.lija.api.element.Element.mc;

/**
 * @author Zhn17
 * <-> 2018-04-21 <-> 11:27
 **/
public class BlockUtils {

    public static boolean onPlayerRightClick(EntityPlayer player, WorldClient worldIn, ItemStack heldStack, BlockPos hitPos, EnumFacing side, Vec3 hitVec) {
        mc.playerController.syncCurrentPlayItem();

        EnumFacing facing = EnumFacing.fromAngle(mc.thePlayer.rotationYawHead);

        float f = facing.getFrontOffsetX() == 1 ? 1f : 0;
        float f1 = 0.5f;
        float f2 = facing.getFrontOffsetZ() == 1 ? 1f : 0;

        boolean flag = false;

        if (!mc.theWorld.getWorldBorder().contains(hitPos)) return false;
        else {
            if (mc.playerController.currentGameType != WorldSettings.GameType.SPECTATOR) {
                IBlockState iblockstate = worldIn.getBlockState(hitPos);

                if ((!player.isSneaking() | player.getHeldItem() == null) & iblockstate.getBlock().onBlockActivated(worldIn, hitPos, iblockstate, player, side, f, f1, f2))
                    flag = true;

                assert heldStack != null;
                if (!flag & heldStack.getItem() instanceof ItemBlock) {
                    ItemBlock itemblock = (ItemBlock) heldStack.getItem();

                    if (!itemblock.canPlaceBlockOnSide(worldIn, hitPos, side, player, heldStack)) return false;
                }
            }

            assert mc.getNetHandler() != null;
            mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(hitPos, side.getIndex(), heldStack, f, f1, f2));
            if (!flag & mc.playerController.currentGameType != WorldSettings.GameType.SPECTATOR) {
                if (heldStack == null) return false;
                else if (mc.playerController.currentGameType.isCreative()) {
                    int i = heldStack.getMetadata();
                    int j = heldStack.stackSize;
                    boolean flag1 = heldStack.onItemUse(player, worldIn, hitPos, side, f, f1, f2);
                    heldStack.setItemDamage(i);
                    heldStack.stackSize = j;
                    return flag1;
                }
                return heldStack.onItemUse(player, worldIn, hitPos, side, f, f1, f2);
            }
            return true;
        }
    }

    public static Material getMaterial(BlockPos pos) {
        return getBlock(pos).getMaterial();
    }

    public static Block getBlockUnderPlayer(EntityPlayer inPlayer, double height) {
        if (LiJA.INSTANCE.isLoading) return null;
        return BlockUtils.getBlock(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ));
    }

    public static Block getBlock(int x, int y, int z) {
        return mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
    }

    public static Block getBlock(BlockPos pos) {
        return mc.theWorld.getBlockState(pos).getBlock();
    }
}