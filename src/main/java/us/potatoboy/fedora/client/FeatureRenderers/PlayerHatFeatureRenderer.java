package us.potatoboy.fedora.client.FeatureRenderers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import us.potatoboy.fedora.Fedora;
import us.potatoboy.fedora.Hat;
import us.potatoboy.fedora.component.PlayerHatComponent;

@Environment(EnvType.CLIENT)
public class PlayerHatFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    private FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context;

    public PlayerHatFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
        super(context);
        this.context = context;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        PlayerHatComponent hatComponent = Fedora.PLAYER_HAT_COMPONENT.get(entity);
        Hat currentHat = hatComponent.getCurrentHat();
        if (currentHat == null) {
            return;
        }

        ModelIdentifier id = currentHat.getModelId();
        BakedModel bakedModel = MinecraftClient.getInstance().getBakedModelManager().getModel(id);

        matrices.push();
        context.getModel().getHead().rotate(matrices);
        matrices.translate(0.0D, -0.5D, 0.0D);
        ItemStack itemStack = entity.getEquippedStack(EquipmentSlot.HEAD);
        if (!itemStack.isEmpty()) {
            matrices.translate(0D, -0.05D, 0D);
        }

        matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180.0F));
        MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(matrices.peek(), vertexConsumers.getBuffer(RenderLayer.getCutout()), null, bakedModel, 0.0F, 0.0F, 0.0F, light, 0);
        matrices.pop();
    }
}
