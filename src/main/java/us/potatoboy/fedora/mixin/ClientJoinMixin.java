package us.potatoboy.fedora.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import us.potatoboy.fedora.client.FedoraClient;
import us.potatoboy.fedora.client.Session;

@Mixin(ClientPlayNetworkHandler.class)
@Environment(EnvType.CLIENT)
public abstract class ClientJoinMixin {

    @Inject(method = "onGameJoin", at = @At("RETURN"))
    public void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        if (FedoraClient.currentSession == null) {
            FedoraClient.currentSession = new Session();
        }
    }
}
