package us.potatoboy.fedora.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import us.potatoboy.fedora.client.FedoraClient;

@Mixin(MinecraftClient.class)
@Environment(EnvType.CLIENT)
public abstract class ClientDisconnectMixin {

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At("RETURN"))
    public void disconnect(Screen screen, CallbackInfo ci) {
        FedoraClient.currentSession = null;
    }
}
