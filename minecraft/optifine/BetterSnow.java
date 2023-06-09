package optifine;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class BetterSnow {
    private static IBakedModel modelSnowLayer = null;

    public static void update() {
        modelSnowLayer = Config.getMinecraft().getBlockRendererDispatcher().func_175023_a().func_178125_b(Blocks.snow_layer.getDefaultState());
    }

    public static IBakedModel getModelSnowLayer() {
        return modelSnowLayer;
    }

    public static IBlockState getStateSnowLayer() {
        return Blocks.snow_layer.getDefaultState();
    }

    public static boolean shouldRender(IBlockAccess blockAccess, Block block, IBlockState blockState, BlockPos blockPos) {
        return checkBlock(block, blockState) && hasSnowNeighbours(blockAccess, blockPos);
    }

    private static boolean hasSnowNeighbours(IBlockAccess blockAccess, BlockPos pos) {
        Block blockSnow = Blocks.snow_layer;
        return (blockAccess.getBlockState(pos.offsetNorth()).getBlock() == blockSnow || blockAccess.getBlockState(pos.offsetSouth()).getBlock() == blockSnow || blockAccess.getBlockState(pos.offsetWest()).getBlock() == blockSnow || blockAccess.getBlockState(pos.offsetEast()).getBlock() == blockSnow) && blockAccess.getBlockState(pos.offsetDown()).getBlock().isOpaqueCube();
    }

    private static boolean checkBlock(Block block, IBlockState blockState) {
        if (block.isFullCube()) return false;
        else if (block.isOpaqueCube()) return false;
        else if (block instanceof BlockSnow) return false;
        else if ((block instanceof BlockDoublePlant || block instanceof BlockFlower || block instanceof BlockMushroom || block instanceof BlockSapling || block instanceof BlockTallGrass))
            return true;
        else if (!(block instanceof BlockFence) && !(block instanceof BlockFenceGate) && !(block instanceof BlockFlowerPot) && !(block instanceof BlockPane) && !(block instanceof BlockReed) && !(block instanceof BlockWall))
            if (block instanceof BlockRedstoneTorch && blockState.getValue(BlockTorch.FACING_PROP) == EnumFacing.UP)
                return true;
            else {
                if (block instanceof BlockLever) {
                    Comparable orient = blockState.getValue(BlockLever.FACING);

                    return orient == BlockLever.EnumOrientation.UP_X || orient == BlockLever.EnumOrientation.UP_Z;
                }

                return false;
            }
        else return true;
    }
}