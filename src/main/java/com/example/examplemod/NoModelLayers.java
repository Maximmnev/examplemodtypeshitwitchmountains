package com.example.examplemod;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerModelType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = "examplemod20")

public class NoModelLayers {


    @SubscribeEvent // on the mod event bus only on the physical client
    public static void addPlayerLayers(EntityRenderersEvent.AddLayers event) {
        // Iterate over all possible player models.
        for (PlayerModelType type : event.getSkins()) {
            // Get the associated AvatarRenderer.
            AvatarRenderer<AbstractClientPlayer> playerRenderer = event.getPlayerRenderer(type);
            if (playerRenderer != null) {
                // Add the layer to the renderer. This assumes that the render layer
                // has proper generics to support players and player renderers.
                playerRenderer.addLayer(new WingsLayer(playerRenderer, event.getEntityModels()));
            }
        }
    }

    // Our ModelLayerLocation.
    public static final ModelLayerLocation MY_LAYER = new ModelLayerLocation(
            // Should be the name of the entity this layer belongs to.
            // May be more generic if this layer can be used on multiple entities.
            Identifier.fromNamespaceAndPath("examplemod20", "player"),
            // The name of the layer itself. Should be main for the entity's base model,
            // and a more descriptive name (e.g. "wings") for more specific layers.
            "Cool_wings"
    );

    @SubscribeEvent // on the mod event bus only on the physical client
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        // Add our layer here.no
        event.registerLayerDefinition(MY_LAYER, Wingss::createBodyLayer);// this shit probobly breaks
    }//o?h no












}

