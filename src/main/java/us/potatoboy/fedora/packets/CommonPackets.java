package us.potatoboy.fedora.packets;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.util.Identifier;
import us.potatoboy.fedora.Fedora;
import us.potatoboy.fedora.Hat;
import us.potatoboy.fedora.HatManager;
import us.potatoboy.fedora.component.PlayerHatComponent;

import java.util.HashSet;

public class CommonPackets {
    public static final Identifier UNLOCK_HAT = new Identifier(Fedora.MOD_ID, "unlock_hat");
    public static final Identifier HAT_LIST = new Identifier(Fedora.MOD_ID, "hat_list");
    public static final Identifier HAT_FILE = new Identifier(Fedora.MOD_ID, "hat_file");

    public static final Identifier SET_HAT = new Identifier(Fedora.MOD_ID, "set_hat");
    public static final Identifier REQUEST_HATS = new Identifier(Fedora.MOD_ID, "request_hats");

    public static void init() {
        ServerSidePacketRegistry.INSTANCE.register(SET_HAT, (packetContext, attachedData) -> {
            String hatId = attachedData.readString(30);
            packetContext.getTaskQueue().execute(() -> {

                Hat hat = HatManager.getFromID(hatId);
                PlayerHatComponent hatComponent = Fedora.PLAYER_HAT_COMPONENT.get(packetContext.getPlayer());
                if (hat != null) {
                    if (hatComponent.getUnlockedHats().contains(hat)) {
                        hatComponent.setCurrentHat(hat);
                    }
                } else {
                    if (hatId.equals("none")) {
                        hatComponent.setCurrentHat(new Hat("none", null, null));
                    }
                }


            });
        });

        ServerSidePacketRegistry.INSTANCE.register(REQUEST_HATS, ((packetContext, packetByteBuf) -> {
            HashSet<String> requestedHats = new HashSet<>();

            String hatId = packetByteBuf.readString(30);
            while (!hatId.equals("END")) {
                requestedHats.add(hatId);
                hatId = packetByteBuf.readString(30);
            }

            HatManager.sendHats(packetContext.getPlayer(), requestedHats.toArray(new String[requestedHats.size()]));
        }));
    }
}
