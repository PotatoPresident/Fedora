package us.potatoboy.fedora.client.FeatureRenderers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.LivingEntity;
import us.potatoboy.fedora.Hat;
import us.potatoboy.fedora.HatManager;

public class HatFeatureRenderer<T extends LivingEntity, M extends EntityModel<T> & ModelWithHead> extends FeatureRenderer<T, M> {
    BakedModel bakedModel;

    public HatFeatureRenderer(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (bakedModel == null) {
            Hat hat = HatManager.getRandomHat();
            bakedModel = MinecraftClient.getInstance().getBakedModelManager().getModel(hat.identifier);
        }

        matrices.push();
        getContextModel().getHead().rotate(matrices);
        matrices.translate(0.0D, -0.5D, 0.0D);
        matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180.0F));
        MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(matrices.peek(), vertexConsumers.getBuffer(RenderLayer.getCutout()), null, bakedModel, 0.0F, 0.0F, 0.0F, light, 0);
        matrices.pop();
    }
}
