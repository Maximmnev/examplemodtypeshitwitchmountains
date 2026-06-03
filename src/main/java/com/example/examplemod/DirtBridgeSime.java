package com.example.examplemod;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = "examplemod20")
public class DirtBridgeSime {

    private static final int SEARCH_RADIUS = 50;
    private static final int EXCLUSION_ZONE = 5;

    // Fixed dimension tracking map
    private static final Map<ResourceKey<Level>, Map<BlockPos, BlockPos>> WORLD_LINKS = new HashMap<>();

    private static Map<BlockPos, BlockPos> getLinkMap(ServerLevel level) {
        return WORLD_LINKS.computeIfAbsent(level.dimension(), k -> new HashMap<>());
    }

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            WORLD_LINKS.remove(serverLevel.dimension());
        }
    }

    // ==========================================
    // THE BRIDGE LOGIC BEGINS HERE
    // ==========================================
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();

        // Run only on the server side
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) {
            return;
        }

        BlockPos posUnder = player.blockPosition().below();
        BlockState stateUnder = level.getBlockState(posUnder);

        // Step 1: Check if the player is standing on a Slime Block
        if (!stateUnder.is(Blocks.SLIME_BLOCK)) {
            return;
        }

        Map<BlockPos, BlockPos> links = getLinkMap(serverLevel);

        // 1-to-1 Check: Skip if this specific slime block is already paired up
        if (links.containsKey(posUnder)) {
            return;
        }

        // Step 2: Search for the nearest valid destination block
        BlockPos destination = findNearestValidSlime(serverLevel, posUnder, links);
        if (destination == null) {
            return;
        }

        // Step 3: Register mutual connection in memory (1-to-1 lock)
        links.put(posUnder, destination);
        links.put(destination, posUnder);

        // Step 4: Build the physical dirt bridge
        buildDirtBridge(serverLevel, posUnder, destination);
    }

    private static BlockPos findNearestValidSlime(ServerLevel level, BlockPos start, Map<BlockPos, BlockPos> links) {
        BlockPos nearest = null;
        double shortestDistance = Double.MAX_VALUE;

        int startX = start.getX();
        int startY = start.getY();
        int startZ = start.getZ();

        for (int x = -SEARCH_RADIUS; x <= SEARCH_RADIUS; x++) {
            for (int y = -SEARCH_RADIUS; y <= SEARCH_RADIUS; y++) {
                for (int z = -SEARCH_RADIUS; z <= SEARCH_RADIUS; z++) {

                    if (x == 0 && y == 0 && z == 0) continue;

                    int targetY = startY + y;
                    if (targetY == startY) continue; // Same Y axis check

                    int targetX = startX + x;
                    int targetZ = startZ + z;
                    if (targetX == startX && targetZ == startZ) continue; // Same column check

                    // 5-block exclusion zone check
                    if (Math.abs(x) <= EXCLUSION_ZONE ||
                            Math.abs(y) <= EXCLUSION_ZONE ||
                            Math.abs(z) <= EXCLUSION_ZONE) {
                        continue;
                    }

                    BlockPos checkPos = new BlockPos(targetX, targetY, targetZ);

                    // Skip if the destination slime block already has a bridge
                    if (links.containsKey(checkPos)) {
                        continue;
                    }

                    if (level.getBlockState(checkPos).is(Blocks.SLIME_BLOCK)) {
                        double dist = start.distSqr(checkPos);
                        if (dist < shortestDistance) {
                            shortestDistance = dist;
                            nearest = checkPos;
                        }
                    }
                }
            }
        }
        return nearest;
    }

    private static void buildDirtBridge(Level level, BlockPos start, BlockPos end) {
        Vec3 startVec = Vec3.atCenterOf(start);
        Vec3 endVec = Vec3.atCenterOf(end);

        Vec3 direction = endVec.subtract(startVec);
        double distance = direction.length();
        Vec3 stepVector = direction.normalize();

        for (int i = 1; i < distance; i++) {
            Vec3 currentStep = startVec.add(stepVector.scale(i));
            BlockPos bridgeBlockPos = BlockPos.containing(currentStep.x, currentStep.y, currentStep.z);

            BlockState currentState = level.getBlockState(bridgeBlockPos);
            if (currentState.isAir() || !currentState.getFluidState().isEmpty()) {
                level.setBlockAndUpdate(bridgeBlockPos, Blocks.DIRT.defaultBlockState());
            }
        }
    }
}
