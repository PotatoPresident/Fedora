package us.potatoboy.fedora.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
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
    public int hatChance = 15;

    public boolean moddedHats = true;

    public List<String> hatBlacklist = Arrays.asList("minecraft:armor_stand", "minecraft:axolotl", "minecraft:witch", "minecraft:shulker");

    public Boolean isBlacklisted(Identifier identifier) {
        for (String entity : hatBlacklist) {
            if (identifier.toString().equals(entity)) return true;
        }

        if (moddedHats == false && !identifier.getNamespace().equals("minecraft")) return true;

        return false;
    }
}
