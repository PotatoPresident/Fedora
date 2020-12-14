package us.potatoboy.fedora.client.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import us.potatoboy.fedora.Fedora;
import us.potatoboy.fedora.Hat;
import us.potatoboy.fedora.client.FedoraClient;
import us.potatoboy.fedora.client.HatHelper;

import java.lang.reflect.Field;

public class HatRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    private Field headField;
    private HatHelper helper;

    public HatRenderer(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity instanceof PlayerEntity) return;
        Identifier entityId = Registry.ENTITY_TYPE.getId(entity.getType());
        if (Fedora.config.isBlacklisted(entityId)) return;

        Hat hat = Fedora.ENTITY_HAT_COMPONENT.get(entity).getCurrentHat();
        if (hat == null) return;

        ModelIdentifier modelIdentifier = Fedora.ENTITY_HAT_COMPONENT.get(entity).getCurrentHat().getModelId();
        BakedModel bakedModel = MinecraftClient.getInstance().getBakedModelManager().getModel(modelIdentifier);
        if (helper == null) {
            helper = FedoraClient.getHelper(entity.getClass());
        }

        matrices.push();

        M model = getContextModel();

        if (entity.isBaby()) {
            matrices.pop();
            return;

            /*
            if (model instanceof ModelWithHead) {
                matrices.translate(0.0D, 0.03125D, 0.0D);
                matrices.scale(0.7F, 0.7F, 0.7F);
                matrices.translate(0.0D, 1.0D, 0.0D);
            } else {
                //TODO deal with baby's
            }
             */
        }

        try {
            if (headField == null) {
                headField = model.getClass().getDeclaredField("head");
                headField.setAccessible(true);
            }
        } catch (NoSuchFieldException e) {
            //No head field
        }


        ModelPart head = null;

        if (headField != null) {
            try {
                head = (ModelPart) headField.get(model);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (head == null) {
            if (model instanceof ModelWithHead) {
                head = ((ModelWithHead) model).getHead();
            } else if (model instanceof BeeEntityModel) {
                head = ((BeeEntityModel<?>) model).getBodyParts().iterator().next();
            } else if (model instanceof AnimalModel) {
                head = ((AnimalModel<?>) model).getHeadParts().iterator().next();
            } else if (model instanceof CompositeEntityModel) {
                int i = 0;
                for (ModelPart part : ((CompositeEntityModel<?>) model).getParts()) {
                    if (helper != null) {
                        if (i == helper.getHeadIndex()) {
                            head = part;
                            break;
                        }
                    } else {
                        head = part;
                        break;
                    }

                    i++;
                }
            }
        }

        if (head != null) {
            head.rotate(matrices);
        }

        matrices.translate(0.0D, -0.25D, 0.0D);
        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
        matrices.scale(0.625F, -0.625F, -0.625F);

        if (helper != null) {
            matrices.scale(helper.getScale(), helper.getScale(), helper.getScale());
            matrices.translate(helper.getSideOffset(), helper.getHeightOffset(), -helper.getForwardOffset());
        }

        Transformation transformation = bakedModel.getTransformation().getTransformation(ModelTransformation.Mode.HEAD);
        transformation.apply(false, matrices);

        matrices.translate(-0.5D, -0.5D, -0.5D);

        MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(matrices.peek(), vertexConsumers.getBuffer(hat.translucent ? RenderLayer.getTranslucentMovingBlock() : RenderLayer.getCutout()), null, bakedModel, 0.0F, 0.0F, 0.0F, light, 0);
        matrices.pop();
    }
}
