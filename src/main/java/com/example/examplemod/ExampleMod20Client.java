package com.example.examplemod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import static com.example.examplemod.Modelshit.LAYER_LOCATION;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = ExampleMod20.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = ExampleMod20.MODID, value = Dist.CLIENT)
public class ExampleMod20Client {
    public ExampleMod20Client(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        ExampleMod20.LOGGER.info("HELLO FROM CLIENT not SETUP");
        ExampleMod20.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        EntityRenderers.register(ExampleMod20.MY_ENTITY.get(), PinkSlimeThingThatIDountUnderstandRendere::new);
    }
    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(LAYER_LOCATION, () -> Modelshit.createBodyLayer());
    }


}
