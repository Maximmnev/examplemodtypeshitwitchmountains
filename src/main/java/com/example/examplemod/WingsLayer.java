package com.example.examplemod;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerSkin;
import org.joml.Quaternionf;

public class WingsLayer extends RenderLayer<AvatarRenderState,
        PlayerModel> {

    private final Wingss model;

    public WingsLayer(AvatarRenderer<AbstractClientPlayer> parent,
                      EntityModelSet models) {
        super(parent);

        this.model = new Wingss(
                models.bakeLayer(NoModelLayers.MY_LAYER)
        );
    }

//    @Override
//    public void render(
//            PoseStack poseStack,
//            MultiBufferSource buffer,
//            int packedLight,
//            AvatarRenderState state,
//            float yRot,
//            float xRot) {
//
//        if (!shouldRenderWings(state))
//            return;
//
//        VertexConsumer consumer =
//                buffer.getBuffer(RenderType.entityCutout(TEXTURE));
//
//        model.setupAnim(state);
//
//        model.renderToBuffer(
//                poseStack,
//                consumer,
//                packedLight,
//                OverlayTexture.NO_OVERLAY
//        );fiifiifiiffifiifififififififififififififififififiififififififififififififififififififififififiifififififfifiifififififififififififiififififififififiifififififiififififififififi
//    }
    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, AvatarRenderState state, float yRot, float xRot) {
        if (!state.isInvisible && state.showCape) {
            PlayerSkin skin = state.skin;

            poseStack.pushPose();
            //poseStack.translate(0.2F, 0.0F, 0.35F);
            poseStack.mulPose(new Quaternionf().rotationZYX(-0.68F,1.48F,0.0F));
            poseStack.mulPose(new Quaternionf().rotationZYX(0.2F,0F,0.0F));




            submitNodeCollector.submitModel(this.model, state, poseStack, RenderTypes.entitySolid(Identifier.fromNamespaceAndPath(ExampleMod20.MODID, "textures/entity/texture1.png")), lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, (ModelFeatureRenderer.CrumblingOverlay)null);poseStack.popPose();

        }

    }
}