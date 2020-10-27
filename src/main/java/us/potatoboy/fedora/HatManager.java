package us.potatoboy.fedora;

import java.util.HashSet;
import java.util.Random;

public class HatManager {
    private static HashSet<Hat> hats = new HashSet<>();

    public static HashSet<Hat> getHats() {
        return hats;
    }

    public static void registerHat(String id, String creator, Boolean obtainable, Hat.Rarity rarity) {
        hats.add(new Hat(id, creator, obtainable, rarity));
    }

    public static Hat getRandomHat() {
        if (hats.isEmpty()) return null;

        Random generator = new Random();
        Object[] hatNames = hats.toArray();
        return (Hat) hatNames[generator.nextInt(hatNames.length)];
    }

    public static Hat getWeightedRandomHat() {
        if (hats.isEmpty()) return null;

        int totalWeight = 0;
        for (Hat hat : hats) {
            totalWeight += hat.rarity.getWeight();
        }

        int randomIndex = -1;
        Object[] hatArray = hats.toArray();
        double random = Math.random() * totalWeight;
        for (int i = 0; i < hats.size(); ++i)
        {
            random -= ((Hat) hatArray[i]).rarity.getWeight();
            if (random <= 0.0d)
            {
                randomIndex = i;
                break;
            }
        }

        return (Hat) hatArray[randomIndex];
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
