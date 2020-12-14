package us.potatoboy.fedora.client.gui;

import io.github.cottonmc.cotton.gui.widget.icon.Icon;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;

public class ModelIcon implements Icon {
    private final BakedModel model;

    public ModelIcon(ModelIdentifier modelId) {
        this.model = MinecraftClient.getInstance().getBakedModelManager().getModel(modelId);
    }

    @Override
    public void paint(MatrixStack matrixStack, int x, int y, int size) {
        VertexConsumer vertexConsumer = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getCutout());

        matrixStack.push();
        matrixStack.scale(100,100,100);
        matrixStack.translate(0,0, 1000);
        MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(matrixStack.peek(), vertexConsumer, null, model, 0.0F, 0.0F, 0.0F, 12, 0);
        matrixStack.pop();
    }
}
