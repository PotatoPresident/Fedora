package us.potatoboy.fedora;

import net.minecraft.client.util.ModelIdentifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class HatManager {
    private static HashMap<String, ModelIdentifier> hats = new HashMap<>();

    public static HashMap<String, ModelIdentifier> getHats() {
        return hats;
    }

    public static void registerHats() {
        hats.put("Top Hat", new ModelIdentifier("fedora:top_hat"));
        hats.put("Pig", new ModelIdentifier("fedora:pig"));
        hats.put("Tiny Potato", new ModelIdentifier("fedora:lil_tater"));
        hats.put("Fedora", new ModelIdentifier("fedora:fedora"));
    }

    public static Hat getRandomHat() {
        Random generator = new Random();
        Object[] hatNames = hats.keySet().toArray();
        return new Hat((String) hatNames[generator.nextInt(hatNames.length)]);
    }
}
