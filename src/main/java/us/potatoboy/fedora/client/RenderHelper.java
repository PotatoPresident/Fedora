package us.potatoboy.fedora.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import us.potatoboy.fedora.Hat;

@Environment(EnvType.CLIENT)
public class RenderHelper {
    private static final MinecraftClient WRAPPER = MinecraftClient.getInstance();

    private RenderHelper() {
    }

    public static BakedModel getHatModel(Hat hat) {
        return WRAPPER.getBakedModelManager().getModel(hat.getModelId());
    }

    public static void renderHat(Hat hat, MatrixStack matrices, VertexConsumerProvider consumers, BakedModel model, int light) {
        WRAPPER.getBlockRenderManager().getModelRenderer()
                .render(matrices.peek(),
                        consumers.getBuffer(hat.translucent ? TexturedRenderLayers.getEntityTranslucentCull() : TexturedRenderLayers.getEntityCutout()),
                        null,
                        model,
                        1.0F, 1.0F, 1.0F, light,
                        OverlayTexture.DEFAULT_UV);
    }
}
