package keystrokesmod.utility;

import keystrokesmod.script.classes.Vec3;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BlockUtils {
    public static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean isSamePos(BlockPos blockPos, BlockPos blockPos2) {
        return blockPos == blockPos2 || (blockPos.getX() == blockPos2.getX() && blockPos.getY() == blockPos2.getY() && blockPos.getZ() == blockPos2.getZ());
    }

    public static boolean notFull(Block block,BlockPos blockPos) {
        return block instanceof BlockFenceGate || block instanceof BlockLadder || block instanceof BlockFlowerPot || block instanceof BlockBasePressurePlate || isFluid(block,blockPos) || block instanceof BlockFence || block instanceof BlockAnvil || block instanceof BlockEnchantmentTable || block instanceof BlockChest;
    }

    public static boolean isFluid(Block block,BlockPos blockPos) {
        return block.getMaterial(getBlockState(blockPos)) == Material.LAVA || block.getMaterial(getBlockState(blockPos)) == Material.WATER;
    }

    public static boolean isInteractable(Block block) {
        return block instanceof BlockFurnace || block instanceof BlockFenceGate || block instanceof BlockChest || block instanceof BlockEnderChest || block instanceof BlockEnchantmentTable || block instanceof BlockBrewingStand || block instanceof BlockBed || block instanceof BlockDropper || block instanceof BlockDispenser || block instanceof BlockHopper || block instanceof BlockAnvil || block == Blocks.CRAFTING_TABLE;
    }

    public static float getBlockHardness(final Block block, final ItemStack itemStack, boolean ignoreSlow, boolean ignoreGround,BlockPos blockPos) {
        final float getBlockHardness = block.getBlockHardness(mc.world.getBlockState(blockPos), null, blockPos);
        if (getBlockHardness < 0.0f) {
            return 0.0f;
        }
        return (block.getMaterial(getBlockState(blockPos)).isToolNotRequired() || (itemStack != null && itemStack.canHarvestBlock(getBlockState(blockPos))) ? (getToolDigEfficiency(itemStack, block, ignoreSlow, ignoreGround) / getBlockHardness / 30.0f) : (getToolDigEfficiency(itemStack, block, ignoreSlow, ignoreGround) / getBlockHardness / 100.0f));
    }

    public static float getToolDigEfficiency(ItemStack itemStack, Block block, boolean ignoreSlow, boolean ignoreGround) {
        float n = (itemStack == null) ? 1.0f : itemStack.getItem().getStrVsBlock(itemStack, block);
        if (n > 1.0f) {
            final int getEnchantmentLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, itemStack);
            if (getEnchantmentLevel > 0 && itemStack != null) {
                n += getEnchantmentLevel * getEnchantmentLevel + 1;
            }
        }
        if (mc.player.isPotionActive(Potion.digSpeed)) {
            n *= 1.0f + (mc.player.getActivePotionEffect(Potion.digSpeed).getAmplifier() + 1) * 0.2f;
        }
        if (!ignoreSlow) {
            if (mc.player.isPotionActive(Potion.digSlowdown)) {
                float n2;
                switch (mc.player.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) {
                    case 0: {
                        n2 = 0.3f;
                        break;
                    }
                    case 1: {
                        n2 = 0.09f;
                        break;
                    }
                    case 2: {
                        n2 = 0.0027f;
                        break;
                    }
                    default: {
                        n2 = 8.1E-4f;
                        break;
                    }
                }
                n *= n2;
            }
            if (mc.player.isInsideOfMaterial(Material.WATER) && !EnchantmentHelper.getAquaAffinityModifier(mc.player)) {
                n /= 5.0f;
            }
            if (!mc.player.onGround && !ignoreGround) {
                n /= 5.0f;
            }
        }
        return n;
    }

    public static Block getBlock(BlockPos blockPos) {
        return getBlockState(blockPos).getBlock();
    }

    public static Block getBlock(double x, double y, double z) {
        return getBlock(new BlockPos(x, y, z));
    }

    public static IBlockState getBlockState(BlockPos blockPos) {
        return mc.world.getBlockState(blockPos);
    }

    public static boolean isBlockUnderNoCollisions() {
        for (int offset = 0; offset < mc.player.posY + mc.player.getEyeHeight(); offset += 2) {
            BlockPos blockPos = new BlockPos(mc.player.posX, offset, mc.player.posZ);

            if (mc.world.getBlockState(blockPos).getBlock() != Blocks.AIR) {
                return true;
            }
        }
        return false;
    }

    public static boolean check(final BlockPos blockPos, final Block block) {
        return getBlock(blockPos) == block;
    }

    public static boolean replaceable(BlockPos blockPos) {
        if (!Utils.nullCheck()) {
            return true;
        }
        return getBlock(blockPos).isReplaceable(mc.theWorld, blockPos);
    }

    public static boolean isBlockUnder() {
        if (mc.player.posY < 0.0) {
            return false;
        } else {
            for(int offset = 0; offset < (int)mc.player.posY + 2; offset += 2) {
                AxisAlignedBB bb = mc.player.getEntityBoundingBox().offset(0.0, (double)(-offset), 0.0);
                if (!mc.theWorld.getCollidingBoundingBoxes(mc.player, bb).isEmpty()) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean isBlockUnder(int distance) {
        for(int y = (int)mc.player.posY; y >= (int)mc.player.posY - distance; --y) {
            if (!(mc.theWorld.getBlockState(new BlockPos(mc.player.posX, y, mc.player.posZ)).getBlock() instanceof BlockAir)) {
                return true;
            }
        }

        return false;
    }

    public static @NotNull List<BlockPos> getAllInBox(@NotNull BlockPos from, @NotNull BlockPos to) {
        final List<BlockPos> blocks = new ArrayList<>();

        BlockPos min = new BlockPos(Math.min(from.getX(), to.getX()),
                Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
        int a = from.getZ();
        int b = to.getZ();
        int a1 = from.getY();
        int b1 = to.getY();
        int a2 = from.getX();
        int b2 = to.getX();
        BlockPos max = new BlockPos(Math.max(a2, b2),
                Math.max(a1, b1), Math.max(a, b));

        for (int x = min.getX(); x <= max.getX(); x++)
            for (int y = min.getY(); y <= max.getY(); y++)
                for (int z = min.getZ(); z <= max.getZ(); z++)
                    blocks.add(new BlockPos(x, y, z));

        return blocks;
    }

    public static @NotNull List<BlockPos> getAllInSphere(@NotNull Vec3 from, double distance) {
        final int blockDistance = (int) Math.round(distance);
        final List<BlockPos> blocks = new ArrayList<>();

        for (BlockPos blockPos : getAllInBox(
                new BlockPos(from.x() - blockDistance, from.y() - blockDistance, from.z() - blockDistance),
                new BlockPos(from.x() + blockDistance, from.y() + blockDistance, from.z() + blockDistance)
        )) {
            AxisAlignedBB box = getCollisionBoundingBox(blockPos);
            if (box == null) continue;

            if (RotationUtils.getNearestPoint(box, from).distanceTo(from.toVec3()) <= distance)
                blocks.add(blockPos);
        }

        return blocks;
    }

    public static @Nullable AxisAlignedBB getCollisionBoundingBox(BlockPos blockPos) {
        final IBlockState blockState = getBlockState(blockPos);
        final Block block = blockState.getBlock();

        if (block instanceof BlockAir) {
            return null;
        }
        if (block instanceof BlockGlass) {
            return new AxisAlignedBB(
                    blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                    blockPos.getX() + 1, blockPos.getY() + 1, blockPos.getZ() + 1
            );
        }
        return block.getCollisionBoundingBox(mc.theWorld, blockPos, blockState);
    }


    public static @NotNull Set<BlockPos> getSurroundBlocks(@NotNull AbstractClientPlayer target) {
        return getSurroundBlocks(target.getEntityBoundingBox());
    }

    public static @NotNull Set<BlockPos> getSurroundBlocks(@NotNull AxisAlignedBB box) {
        int minX = MathHelper.floor_double(box.minX) - 1;
        int minY = MathHelper.floor_double(box.minY) - 1;
        int minZ = MathHelper.floor_double(box.minZ) - 1;
        int maxX = MathHelper.floor_double(box.maxX) + 1;
        int maxY = MathHelper.floor_double(box.maxY) + 1;
        int maxZ = MathHelper.floor_double(box.maxZ) + 1;

        return getAllInBox(new BlockPos(minX, minY, minZ), new BlockPos(maxX, maxY, maxZ))
                .stream()
                .filter(blockPos -> !box.intersectsWith(new AxisAlignedBB(
                        blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                        blockPos.getX() + 1, blockPos.getY() + 1, blockPos.getZ() + 1
                )))
                .filter(blockPos -> !((blockPos.getX() == minX || blockPos.getX() == maxX)
                        && (blockPos.getZ() == minZ || blockPos.getZ() == maxZ)))
//                .filter(blockPos -> !((blockPos.getY() == minY || blockPos.getY() == maxY)
//                        && (blockPos.getX() == minX || blockPos.getX() == maxX || blockPos.getZ() == minZ || blockPos.getZ() == maxZ)))
                .collect(Collectors.toSet());
    }

    public static boolean insideBlock() {
        if (mc.player.ticksExisted < 5) {
            return false;
        }

        return insideBlock(mc.player.getEntityBoundingBox());
    }

    private static boolean insideBlock(AxisAlignedBB entityBoundingBox) {
        return entityBoundingBox.intersects(mc.player.getEntityBoundingBox());
    }

    public static boolean insideBlock(@NotNull final AxisAlignedBB bb,final @NotNull Vec3 pos) {
        final WorldClient world = mc.theWorld;
        for (int x = MathHelper.floor(bb.minX); x < MathHelper.floor(bb.maxX) + 1; ++x) {
            for (int y = MathHelper.floor(bb.minY); y < MathHelper.floor(bb.maxY) + 1; ++y) {
                for (int z = MathHelper.floor(bb.minZ); z < MathHelper.floor(bb.maxZ) + 1; ++z) {
                    final Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
                    final AxisAlignedBB boundingBox;
                    BlockPos blockPos = new BlockPos(pos.x, pos.y, pos.z);
                    IBlockState blockState = getBlockState(blockPos);
                    IBlockAccess blockAccess = mc.world;
                    if (!(block instanceof BlockAir) && (boundingBox = block.getCollisionBoundingBox(blockState, blockAccess, blockPos)) != null && bb.intersects(boundingBox)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean insideBlock(final @NotNull Vec3 pos) {
        BlockPos blockPos = new BlockPos(pos.x, pos.y, pos.z);
        IBlockState blockState = getBlockState(blockPos);
        IBlockAccess blockAccess = mc.world;
        Block block = blockState.getBlock();
        return block.getCollisionBoundingBox(blockState, blockAccess, blockPos).isVecInside(pos.toVec3());
    }

    public static Block blockRelativeToPlayer(final double offsetX, final double offsetY, final double offsetZ) {
        return mc.world.getBlockState(new BlockPos(mc.player).add(offsetX, offsetY, offsetZ)).getBlock();
    }
}
