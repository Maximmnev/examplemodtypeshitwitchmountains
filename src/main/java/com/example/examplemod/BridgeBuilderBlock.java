package com.example.examplemod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BridgeBuilderBlock extends Block {

    public BridgeBuilderBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        // Only run logic on the server side to prevent ghost blocks
        if (!level.isClientSide()) {

            // Check all 6 directions around the block
            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = pos.relative(direction);
                BlockState neighborState = level.getBlockState(neighborPos);

                // Check if the adjacent block is an Oak Plank
                if (neighborState.is(Blocks.OAK_PLANKS)) {
                    generateBridge(level, pos, direction, 20);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }

    /**
     * Generates a 20-block line of oak planks stretching out in the specified direction.
     */
    private void generateBridge(Level level, BlockPos startPos, Direction direction, int length) {
        for (int i = 1; i <= length; i++) {
            // Move further out in the target direction
            BlockPos targetPos = startPos.relative(direction, i);

            // Only place blocks if the target space is air or replaceable (e.g., water, grass)
            if (level.getBlockState(targetPos).canBeReplaced()) {
                level.setBlockAndUpdate(targetPos, Blocks.OAK_PLANKS.defaultBlockState());
            }
        }
    }
}
