package us.potatoboy.fedora;

import java.util.HashSet;
import java.util.Random;

public class HatManager {
    private static HashSet<Hat> hats = new HashSet<>();

    public static HashSet<Hat> getHats() {
        return hats;
    }

    public static void registerHat(String id, String creator, Boolean obtainable) {
        hats.add(new Hat(id, creator, obtainable));
    }

    public static Hat getRandomHat() {
        if (hats.isEmpty()) return null;

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
