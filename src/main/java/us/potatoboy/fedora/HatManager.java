package us.potatoboy.fedora;

import net.minecraft.client.util.ModelIdentifier;

import java.util.HashMap;
import java.util.HashSet;

public class HatManager {
    private static HashMap<String, ModelIdentifier> hats = new HashMap<>();

    public static HashMap<String, ModelIdentifier> getHats() {
        return hats;
    }

    public static void registerHats() {
        hats.put("Top Hat", new ModelIdentifier("fedora:tophat"));
        hats.put("Pig", new ModelIdentifier("fedora:pig"));
    }
}
