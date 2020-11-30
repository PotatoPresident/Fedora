package us.potatoboy.fedora.mixin;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import us.potatoboy.fedora.Hat;
import us.potatoboy.fedora.HatManager;
import us.potatoboy.fedora.packets.CommonPackets;

@Mixin(PlayerManager.class)
@Environment(EnvType.SERVER)
public abstract class PlayerManagerMixin {

    @Inject(method = "onPlayerConnect", at = @At("RETURN"))
    private void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
        for (Hat hat : HatManager.getHatRegistry()) {
            passedData.writeString(hat.id);
        }
        passedData.writeString("END");
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, CommonPackets.HAT_LIST, passedData);
    }
}
