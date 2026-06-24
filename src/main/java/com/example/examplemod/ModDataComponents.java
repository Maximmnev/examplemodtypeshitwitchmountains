package com.example.examplemod;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ExampleMod20.MODID);

    // Маркер "эти ботинки дают двойной прыжок". Значение Boolean
    // (можно Unit для чистого маркера, но Boolean удобнее включать/выключать).
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> DOUBLE_JUMP =
            DATA_COMPONENTS.registerComponentType("double_jump", builder -> builder
                    .persistent(Codec.BOOL)                      // пишется в NBT предмета / на диск
                    .networkSynchronized(ByteBufCodecs.BOOL));   // синкается на клиент
}
