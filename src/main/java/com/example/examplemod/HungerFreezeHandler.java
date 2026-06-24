package com.example.examplemod;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

// Automatically registers this class to the NeoForge gameplay event bus
@EventBusSubscriber(modid = "examplemod20")
public class HungerFreezeHandler {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();

        if (!player.level().isClientSide()) {
            FoodData foodData = player.getFoodData();
            foodData.setFoodLevel(20);
            foodData.setSaturation(0.0F);

        }
    }
}