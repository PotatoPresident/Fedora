package us.potatoboy.fedora.client.FeatureRenderers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.EquipmentSlot;
import us.potatoboy.fedora.Fedora;
import us.potatoboy.fedora.Hat;
import us.potatoboy.fedora.client.RenderHelper;
import us.potatoboy.fedora.component.PlayerHatComponent;

@Environment(EnvType.CLIENT)
public class PlayerHatFeatureRenderer<T extends AbstractClientPlayerEntity> extends FeatureRenderer<T, PlayerEntityModel<T>> {
    private FeatureRendererContext<T, PlayerEntityModel<T>> context;

    public PlayerHatFeatureRenderer(FeatureRendererContext<T, PlayerEntityModel<T>> context) {
        super(context);
        this.context = context;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider consumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        PlayerHatComponent hatComponent = Fedora.PLAYER_HAT_COMPONENT.get(entity);
        
        Hat hat = hatComponent.getCurrentHat();

        // having "(hat == null) return" causes a flash of a missing model box to be rendered
        if (hat != null && hat != Hat.NONE) {
            BakedModel bakedModel = RenderHelper.getHatModel(hat);

            matrices.push();

            context.getModel().getHead().rotate(matrices);

            matrices.translate(0.0D, -0.25D, 0.0D);
            matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
            matrices.scale(0.625F, -0.625F, -0.625F);

            bakedModel.getTransformation().getTransformation(ModelTransformation.Mode.HEAD).apply(false, matrices);

            double y = !hat.ignoreHelmets && entity.hasStackEquipped(EquipmentSlot.HEAD) ? -0.44D : -0.5D;
            matrices.translate(-0.5D, y, -0.5D);

            /*
            ItemStack itemStack = entity.getEquippedStack(EquipmentSlot.HEAD);

            if (!itemStack.isEmpty()) {
                matrices.translate(0D, -0.05D, 0D);
            }*/

            RenderHelper.renderHat(hat, matrices, consumers, bakedModel, light);

            matrices.pop();
        }
    }
}
