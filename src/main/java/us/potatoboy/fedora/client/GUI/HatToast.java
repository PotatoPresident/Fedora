package us.potatoboy.fedora.client.GUI;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import us.potatoboy.fedora.Fedora;
import us.potatoboy.fedora.Hat;

public class HatToast implements Toast {
    private Text title;
    private Text desc;
    private long startTime;
    private boolean justUpdated;
    private final int width;
    private Hat.Rarity rarity;

    private Identifier TEXT = new Identifier(Fedora.MOD_ID, "textures/gui/hat_toast.png");

    public HatToast(Text title, String hatId, int width, Hat.Rarity rarity) {
        this.title = title;
        this.desc = new TranslatableText("fedora.hat." + hatId).formatted(rarity == Hat.Rarity.COMMON ? Formatting.RESET : rarity.getFormatting());
        this.width = width;
        this.rarity = rarity;
    }

    @Override
    public Toast.Visibility draw(MatrixStack matrices, ToastManager manager, long startTime) {
        if (this.justUpdated) {
            this.startTime = startTime;
            this.justUpdated = false;
        }

        manager.getGame().getTextureManager().bindTexture(TEXT);
        RenderSystem.color3f(1.0F, 1.0F, 1.0F);
        if (rarity.equals(Hat.Rarity.LEGENDARY)) {
            manager.drawTexture(matrices, 0, 0, 0, 32, this.getWidth(), this.getHeight());
        } else {
            manager.drawTexture(matrices, 0, 0, 0, 0, this.getWidth(), this.getHeight());
        }

        manager.getGame().textRenderer.draw(matrices, title, 32.0F, 7.0F, -11534256);
        manager.getGame().textRenderer.draw(matrices, desc, 32.0F, 18.0F, -16777216);

        return startTime - this.startTime < 5000L ? Toast.Visibility.SHOW : Toast.Visibility.HIDE;
    }
}
