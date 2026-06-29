package com.example.examplemod;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

// Тултип для DataComponent: ваниль не знает про наш произвольный компонент,
// поэтому рисуем строку сами (в отличие от атрибута, который рисуется автоматически).
@EventBusSubscriber(modid = ExampleMod20.MODID, value = Dist.CLIENT)
public class AirJumpCountTooltip {

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        Integer count = event.getItemStack().get(ModDataComponents.AIR_JUMP_COUNT.get());
        if (count == null || count <= 0) return;

        event.getToolTip().add(
                Component.translatable("tooltip.examplemod20.air_jumps_done", count)
                        .withStyle(ChatFormatting.DARK_GRAY));
    }
}
