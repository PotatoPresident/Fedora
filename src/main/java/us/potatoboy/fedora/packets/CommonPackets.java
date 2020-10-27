package us.potatoboy.fedora.packets;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.util.Identifier;
import us.potatoboy.fedora.Fedora;
import us.potatoboy.fedora.Hat;
import us.potatoboy.fedora.HatManager;
import us.potatoboy.fedora.component.PlayerHatComponent;

public class CommonPackets {
    public static final Identifier SET_HAT_PACKET_ID = new Identifier("fedora", "sethat");

    public static void init() {
        ServerSidePacketRegistry.INSTANCE.register(SET_HAT_PACKET_ID, (packetContext, attachedData) -> {
            String hatId = attachedData.readString(30);
            packetContext.getTaskQueue().execute(() -> {

                Hat hat = HatManager.getFromID(hatId);
                if (hat != null) {
                    PlayerHatComponent hatComponent = Fedora.PLAYER_HAT_COMPONENT.get(packetContext.getPlayer());
                    if (hatComponent.getUnlockedHats().contains(hat)) {
                        hatComponent.setCurrentHat(hat);
                    }
                }

            });
        });
    }
}
