package com.example.examplemod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.Random;

@EventBusSubscriber(modid = "examplemod20")
public class PlatformGeneratorHandler {

    public static int tickCounter = 0;

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        tickCounter++;
        if (tickCounter % 4 != 0) return;

        for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
            ServerLevel level = (ServerLevel) player.level();
            // Список обработанных чанков для ЭТОГО измерения. Сам сохраняется в файл мира
            // и сам подгружается при перезаходе — повторно платформы не заспавнятся.
            PlatformChunkData data = PlatformChunkData.get(level);
            ChunkPos playerChunk = player.chunkPosition();

            int renderDistance = event.getServer().getPlayerList().getViewDistance();
            // Scan the square of chunks within the player's view distance
            for (int x = -renderDistance; x <= renderDistance; x++) {
                for (int z = -renderDistance; z <= renderDistance; z++) {
                    ChunkPos currentChunk = new ChunkPos(playerChunk.x() + x, playerChunk.z() + z);

                    // 2. Performance optimization: Skip if already generated
                    if (data.isGenerated(currentChunk)) continue;

                    // 3. Select every 2nd chunk using a checkerboard pattern
                    if ((Math.abs(currentChunk.x()) + Math.abs(currentChunk.z())) % 2 == 0) {
                        generatePlatformInChunk(level, currentChunk);
                        data.markGenerated(currentChunk);
                    }
                }
            }
        }
    }

    private static void generatePlatformInChunk(ServerLevel level, ChunkPos chunk) {
        int middleX = chunk.getMinBlockX() + 8;
        int middleZ = chunk.getMinBlockZ() + 8;

        int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE, middleX, middleZ);
        BlockPos targetPos = new BlockPos(middleX, surfaceY, middleZ);



        java.util.Random notfun = new Random();
        int notthatfun =notfun.nextInt(0,21);
        int notthatun =notfun.nextInt(30,41);
        int notthatfn =notfun.nextInt(43,100);

        var one = new PlacingABasicPlatformIGuess(targetPos.offset(0,notthatfun,0),"low",level);
        var two = new PlacingABasicPlatformIGuess(targetPos.offset(0,notthatun,0),"middle",level);
        var numbertree = new PlacingABasicPlatformIGuess(targetPos.offset(0,notthatfn,0),"high",level);
    }


}
