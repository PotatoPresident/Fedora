package us.potatoboy.fedora.packets;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
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
        ServerPlayNetworking.registerGlobalReceiver(SET_HAT, (minecraftServer, serverPlayerEntity, serverPlayNetworkHandler, packetByteBuf, packetSender) ->  {
            String hatId = packetByteBuf.readString(30);
            minecraftServer.execute(() -> {

                Hat hat = HatManager.getFromID(hatId);
                PlayerHatComponent hatComponent = Fedora.PLAYER_HAT_COMPONENT.get(serverPlayerEntity);
                if (hat != null) {
                    if (hatComponent.getUnlockedHats().contains(hat)) {
                        hatComponent.setCurrentHat(hat);
                    }
                } else {
                    if (hatId.equals("none")) {
                        hatComponent.setCurrentHat(Hat.NONE);
                    }
                }

            });
        });

        ServerPlayNetworking.registerGlobalReceiver(REQUEST_HATS, (minecraftServer, serverPlayerEntity, serverPlayNetworkHandler, packetByteBuf, packetSender) ->  {
            HashSet<String> requestedHats = new HashSet<>();

            String hatId = packetByteBuf.readString(30);
            while (!hatId.equals("END")) {
                requestedHats.add(hatId);
                hatId = packetByteBuf.readString(30);
            }

            HatManager.sendHats(serverPlayerEntity, requestedHats.toArray(new String[requestedHats.size()]));
        });
    }
}
