package com.example.examplemod;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;

import java.util.Objects;
import java.util.Random;
//import net.minecraft.world.phys.Vec3;


public class PlacingABasicPlatformIGuess {
    java.util.Random notfun = new Random();
    int chestbutstillnotfun =notfun.nextInt(0,5);

    public BlockPos cordsmiddleblockpos;
    public String platformtipe;
    PlacingABasicPlatformIGuess(BlockPos cordsmiddleblockpos,String platformtipe,Level level){
    this.platformtipe = platformtipe;
    this.cordsmiddleblockpos = cordsmiddleblockpos;
    this.platformmakingshit(cordsmiddleblockpos,level);
    };



    public void platformmakingshit(BlockPos cords, Level level) {
        BlockState block =Blocks.GRASS_BLOCK.defaultBlockState();
        BlockState bloc =Blocks.GRASS_BLOCK.defaultBlockState();
        if (this.chestbutstillnotfun == 4){
            level.setBlock(cords.offset(0 ,1 ,0),Blocks.CHEST.defaultBlockState(), 3);
            if (level.isLoaded(cords.offset(0 ,1 ,0))) {
                int randomslot = notfun.nextInt(0,27);
                int randomsitemcount = notfun.nextInt(0,65);
                int itemrandomizer = notfun.nextInt(0,4);
                Item randomitem = Items.CHEST;
                switch (itemrandomizer)
                {
                    case 0:
                        randomitem = Items.GOLDEN_APPLE;
                        break;

                    case 1:
                        randomitem = Items.STICK;
                        break;

                    case 2:
                        randomitem = Items.IRON_SWORD;
                        break;

                    case 3:
                        randomitem = Items.IRON_SPEAR;
                        break;

                    default:
                        randomitem = Items.CHEST;
                        break;
                }

                BlockEntity blockEntity = level.getBlockEntity(cords.offset(0 ,1 ,0));
                ChestBlockEntity Cbe =(ChestBlockEntity)blockEntity;
                ItemStack iteMstack = new ItemStack(randomitem,randomsitemcount);
                Cbe.setItem(randomslot,iteMstack);
            }

        }


        if (Objects.equals(platformtipe, "low")){
            block = Blocks.DEEPSLATE.defaultBlockState();
            bloc = Blocks.SLIME_BLOCK.defaultBlockState();

        }
        if (Objects.equals(platformtipe, "high")){
            block = Blocks.POWDER_SNOW.defaultBlockState();
             bloc = Blocks.SLIME_BLOCK.defaultBlockState();
        }
        if (Objects.equals(platformtipe, "middle")){
            block = Blocks.GRAY_CONCRETE.defaultBlockState();
             bloc = Blocks.SLIME_BLOCK.defaultBlockState();

        }


        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                BlockPos platformPos = cords.offset(x,0, z);
                level.setBlock(platformPos,block, 3);
            }


        }
        BlockPos platformPos = cords.offset(-2,0, 0);
        level.setBlock(platformPos,bloc, 3);
        BlockPos platformPo = cords.offset(2,0, 0);
        level.setBlock(platformPo,bloc, 3);
        BlockPos platformP = cords.offset(0,0, 2);
        level.setBlock(platformP,bloc, 3);
        BlockPos platform = cords.offset(0,0, -2);
        level.setBlock(platform,bloc, 3);

    }






}

