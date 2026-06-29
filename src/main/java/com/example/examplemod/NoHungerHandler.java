package com.example.examplemod;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

// Держим голод игроков всегда на максимуме — игрок не голодает.
@EventBusSubscriber(modid = "examplemod20")
public class NoHungerHandler {

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
            FoodData food = player.getFoodData();
            // 20 — максимум шкалы голода. Насыщение тоже на максимуме,
            // чтобы здоровье регенерировало и голод не начинал падать.
            food.setFoodLevel(20);
            food.setSaturation(0.0f);
        }
    }
}
