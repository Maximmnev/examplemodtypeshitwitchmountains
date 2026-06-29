package com.example.examplemod;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(Registries.ATTRIBUTE, ExampleMod20.MODID);

    // Кол-во доп. прыжков в воздухе. descriptionId -> ключ перевода имени атрибута,
    // именно его ваниль печатает синей строкой "+N <имя>" под "When on Feet:".
    public static final DeferredHolder<Attribute, Attribute> AIR_JUMPS =
            ATTRIBUTES.register("air_jumps",
                    () -> new RangedAttribute("attribute.name.examplemod20.air_jumps", 0.0, 0.0, 256.0)
                            .setSyncable(true));
}
