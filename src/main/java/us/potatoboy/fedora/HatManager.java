package us.potatoboy.fedora;

import java.util.HashSet;
import java.util.Random;

public class HatManager {
    private static HashSet<Hat> hats = new HashSet<>();

    public static HashSet<Hat> getHats() {
        return hats;
    }

    public static void registerHats() {
        hats.add(new Hat("top_hat", "Potatoboy9999", true));
        hats.add(new Hat("pig", "Potatoboy9999", true));
        hats.add(new Hat("lil_tater", "Potatoboy9999", true));
        hats.add(new Hat("fedora", "Potatoboy9999", true));
    }

    public static Hat getRandomHat() {
        Random generator = new Random();
        Object[] hatNames = hats.toArray();
        return (Hat) hatNames[generator.nextInt(hatNames.length)];
    }

    public static boolean isRegistered(String id) {
        for (Hat hat : hats) {
            if (hat.id.equals(id)) {
                return true;
            }
        }

        return false;
    }

    public static Hat getFromID(String id) {
        for (Hat hat : hats) {
            if (hat.id.equals(id)) {
                return hat;
            }
        }

        return null;
    }
}
