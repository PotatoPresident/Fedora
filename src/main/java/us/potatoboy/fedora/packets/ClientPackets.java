package us.potatoboy.fedora.packets;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import us.potatoboy.fedora.Fedora;
import us.potatoboy.fedora.Hat;
import us.potatoboy.fedora.HatManager;
import us.potatoboy.fedora.client.GUI.HatToast;

@Environment(EnvType.CLIENT)
public class ClientPackets {
    public static final Identifier UNLOCK_HAT = new Identifier(Fedora.MOD_ID, "unlock_hat");

    public static void init() {
        ClientSidePacketRegistry.INSTANCE.register(UNLOCK_HAT, ((packetContext, packetByteBuf) -> {

            String hatId = packetByteBuf.readString(30);
            packetContext.getTaskQueue().execute(() -> {
                Hat hat = HatManager.getFromID(hatId);

                MinecraftClient.getInstance().getToastManager().add(new HatToast(
                        new TranslatableText("toast.fedora.hatunlocked"),
                        hat.id,
                        30,
                        hat.rarity
                ));
            });
        }));
    }
}
