package com.example.examplemod;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.slime.SlimeModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

public class PinkSlimeThingThatIDountUnderstandRendere extends MobRenderer<PinkSlimeEntity, LivingEntityRenderState,Modelshit<PinkSlimeEntity>> {
    // In our constructor, we just forward to super.
    public PinkSlimeThingThatIDountUnderstandRendere(EntityRendererProvider.Context context) {
        super(context, new Modelshit<>(context.bakeLayer(Modelshit.LAYER_LOCATION)), 0.25F);
    }

    // Tell the render engine how to create a new entity render state.
    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState livingEntityRenderState) {
        return TEXTURE;
    }

    // Update the render state by copying the needed values from the passed entity to the passed state.
    // Both Entity and EntityRenderState may be replaced with more concrete types,
    // based on the generic types that have been passed to the supertype.
    @Override
    public void extractRenderState(PinkSlimeEntity entity, LivingEntityRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        // Extract and store any additional values in the state here.
    }
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(ExampleMod20.MODID, "textures/entity/redpurplesomething.png");



}

