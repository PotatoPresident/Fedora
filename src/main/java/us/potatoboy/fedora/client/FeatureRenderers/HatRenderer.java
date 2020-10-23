package us.potatoboy.fedora.client.FeatureRenderers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import us.potatoboy.fedora.Fedora;
import us.potatoboy.fedora.Hat;
import us.potatoboy.fedora.client.FedoraClient;
import us.potatoboy.fedora.client.HatHelper;
import us.potatoboy.fedora.mixin.*;

public class HatRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    public HatRenderer(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        Hat hat = Fedora.ENTITY_HAT_COMPONENT.get(entity).getCurrentHat();
        if (hat == null) return;

        ModelIdentifier modelIdentifier = Fedora.ENTITY_HAT_COMPONENT.get(entity).getCurrentHat().getModelId();
        BakedModel bakedModel = MinecraftClient.getInstance().getBakedModelManager().getModel(modelIdentifier);
        HatHelper helper = FedoraClient.getHelper(entity.getClass());

        matrices.push();

        /*
        if (entity.isBaby()) {
            matrices.translate(0.0D, 0.03125D, 0.0D);
            matrices.scale(0.7F, 0.7F, 0.7F);
            matrices.translate(0.0D, 1.0D, 0.0D);
        }
         */

        ModelPart head = null;
        M model = getContextModel();
        if (model instanceof ModelWithHead) {
            head = ((ModelWithHead) model).getHead();
        } else if (model instanceof AnimalModel && !(model instanceof BeeEntityModel)) {
            head = ((AnimalModelHeadInvoker)model).getHeadParts().iterator().next();
        } else if (model instanceof LlamaEntityModel) {
            head = ((LlamaHeadAccessor)model).getHead();
        } else if (model instanceof RabbitEntityModel) {
            head = ((RabbitHeadAccessor) model).getHead();
        } else if (model instanceof SnowGolemEntityModel) {
            head = ((SnowHeadAccessor) model).getHead();
        } else if (model instanceof RavagerEntityModel) {
            head = ((RavagerHeadAccessor) model).getHead();
        } else if (model instanceof CompositeEntityModel) {
            for (ModelPart part : ((CompositeEntityModel<?>) model).getParts()) {
                head = part;
                break;
            }
        }
        if (head != null) {
            head.rotate(matrices);
        }

        matrices.translate(0.0D, -0.5D, 0.0D);
        matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180.0F));

        if (entity instanceof WitherEntity) {
            helper = new HatHelper(-0.25, 0, 0, 1F);
        }

        if (helper != null) {
            matrices.scale(helper.getScale(), helper.getScale(), helper.getScale());
            matrices.translate(helper.getSideOffset(), helper.getHeightOffset(), -helper.getForwardOffset());
        }

        MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(matrices.peek(), vertexConsumers.getBuffer(RenderLayer.getCutout()), null, bakedModel, 0.0F, 0.0F, 0.0F, light, 0);
        matrices.pop();
    }
}
