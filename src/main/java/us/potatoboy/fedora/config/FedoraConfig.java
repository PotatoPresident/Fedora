package us.potatoboy.fedora.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;

@Config(name = "fedora/config")
public class FedoraConfig implements ConfigData {
    @ConfigEntry.Gui.Excluded
    public int hatVer = 0;

    public boolean serverHats = true;

    public boolean autoDownload = true;

    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    public int hatChance = 25;

    public boolean moddedHats = true;

    public List<String> hatBlacklist = Arrays.asList("minecraft:armor_stand", "minecraft:witch");

    public Boolean isBlacklisted(Identifier identifier) {
        for (String entity : hatBlacklist) {
            if (identifier.toString().equals(entity)) return true;
        }

        if (moddedHats == false && !identifier.getNamespace().equals("minecraft")) return true;

        return false;
    }
}
