package us.potatoboy.fedora.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import us.potatoboy.fedora.Fedora;
import us.potatoboy.fedora.Hat;

public class HatToast implements Toast {
    private Text title;
    private Text desc;
    private long startTime;
    private boolean justUpdated;
    private Hat.Rarity rarity;

    private Identifier TEXT = new Identifier(Fedora.MOD_ID, "textures/gui/hat_toast.png");

    public HatToast(Text title, String hatId, int width, Hat.Rarity rarity) {
        this.title = title;
        this.desc = new TranslatableText("fedora.hat." + hatId);
        this.rarity = rarity;
    }

    @Override
    public Visibility draw(MatrixStack matrices, ToastManager manager, long startTime) {
        if (this.justUpdated) {
            this.startTime = startTime;
            this.justUpdated = false;
        }

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXT);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        if (rarity.equals(Hat.Rarity.EPIC)) {
            manager.drawTexture(matrices, 0, 0, 0, 32, this.getWidth(), this.getHeight());
        } else {
            manager.drawTexture(matrices, 0, 0, 0, 0, this.getWidth(), this.getHeight());
        }

        manager.getClient().textRenderer.draw(matrices, title, 32.0F, 7.0F, 0);
        manager.getClient().textRenderer.draw(matrices, desc, 32.0F, 18.0F, 0);

        return startTime - this.startTime < 5000L ? Toast.Visibility.SHOW : Toast.Visibility.HIDE;
    }
}
