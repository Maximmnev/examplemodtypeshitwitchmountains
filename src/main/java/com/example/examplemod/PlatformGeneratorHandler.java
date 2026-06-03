package com.example.examplemod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.LevelResource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@EventBusSubscriber(modid = "examplemod20")
public class PlatformGeneratorHandler {

    public static int tickCounter = 0;
    // Keeps track of processed chunks so we only generate once
//    public static final Set<ChunkPos> GENERATED_CHUNKS = new HashSet<>();
public static String[] worldmemorysh$$ = new String[100];
public static Set[] setchunkpos = new Set[100];
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        tickCounter++;
        if (tickCounter % 4 != 0) return;
        int c =0;
        int notg = 101;
        var g = event.getServer() .getWorldPath(LevelResource.ROOT)
                .normalize()
                .getFileName()
                .toString();
        for (int I = 0;I <100;I++){

            if (worldmemorysh$$[I] != null){
                c++;
            }
            if (worldmemorysh$$[I]!=null && worldmemorysh$$[I].equals(g)){
                notg = I;
                break;
            }


        }
        if (notg ==101){
            notg = c;
            worldmemorysh$$[c] = g;
            setchunkpos[c] = new HashSet<ChunkPos>();
        }


        worldmemorysh$$[0] = event.getServer() .getWorldPath(LevelResource.ROOT)
                .normalize()
                .getFileName()
                .toString();

        for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
            ServerLevel level = (ServerLevel) player.level();
            ChunkPos playerChunk = player.chunkPosition();

            int renderDistance = event.getServer().getPlayerList().getViewDistance();
            // Scan the square of chunks within the player's view distance
            for (int x = -renderDistance; x <= renderDistance; x++) {
                for (int z = -renderDistance; z <= renderDistance; z++) {
                    ChunkPos currentChunk = new ChunkPos(playerChunk.x() + x, playerChunk.z() + z);


                    // 2. Performance optimization: Skip if already generated
                    if (setchunkpos[notg].contains(currentChunk)) continue;

                    // 3. Select every 2nd chunk using a checkerboard pattern
                    if ((Math.abs(currentChunk.x()) + Math.abs(currentChunk.z())) % 2 == 0) {
                        generatePlatformInChunk(level, currentChunk);
                        setchunkpos[notg].add(currentChunk);
                    }
                }
            }
        }


    }// that would mean u can not go out of the world when u see something interestin and the player data is same so u will just fall

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
