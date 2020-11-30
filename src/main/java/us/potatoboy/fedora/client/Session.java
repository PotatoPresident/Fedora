package us.potatoboy.fedora.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import us.potatoboy.fedora.Hat;
import us.potatoboy.fedora.HatManager;

import java.util.HashSet;

@Environment(EnvType.CLIENT)
public class Session {
    private HashSet<String> serverHats;

    public Session() {
        serverHats = null;
    }

    public Session(HashSet<String> hats) {
        serverHats = hats;
    }

    public HashSet<Hat> getSessionHats() {
        if (serverHats == null) {
            return HatManager.getHatRegistry();
        }

        HashSet<Hat> sessionHats = new HashSet<>();

        for (Hat hat : HatManager.getHatRegistry()) {
            if (serverHats.contains(hat.id)) {
                sessionHats.add(hat);
            }
        }
        return sessionHats;
    }

    public boolean isOnServer() {
        return serverHats != null;
    }
}
