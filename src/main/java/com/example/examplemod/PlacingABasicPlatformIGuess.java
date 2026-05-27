package com.example.examplemod;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;
//import net.minecraft.world.phys.Vec3;


public class PlacingABasicPlatformIGuess {
    public BlockPos cordsmiddleblockpos;
    public String platformtipe;
    PlacingABasicPlatformIGuess(BlockPos cordsmiddleblockpos,String platformtipe,Level level){
    this.platformtipe = platformtipe;
    this.cordsmiddleblockpos = cordsmiddleblockpos;
    this.platformmakingshit(cordsmiddleblockpos,level);
    };



    public void platformmakingshit(BlockPos cords, Level level) {
        BlockState block =Blocks.GRASS_BLOCK.defaultBlockState();

        if (Objects.equals(platformtipe, "low")){
                block = Blocks.DEEPSLATE.defaultBlockState();
        }
        if (Objects.equals(platformtipe, "high")){
            block = Blocks.POWDER_SNOW.defaultBlockState();
        }
        if (Objects.equals(platformtipe, "middle")){
            block = Blocks.GRAY_CONCRETE.defaultBlockState();
        }


        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                BlockPos platformPos = cords.offset(x,0, z);
                level.setBlock(platformPos,block, 3);
            }


        }
    }






}

