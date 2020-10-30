package us.potatoboy.fedora.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;

@Config(name = "fedora/config")
public class FedoraConfig implements ConfigData {
    @Comment("Don't change. Current version of the installed hats")
    @ConfigEntry.Gui.Excluded
    public int hatVer = 0;

    @ConfigEntry.Gui.Tooltip
    @Comment("Entity hat chance. 1 in x. Default: 4 (25%)")
    public int hatChance = 4;

    @Comment("Entity hat blacklist")
    public List<String> hatBlacklist = Arrays.asList("minecraft:armor_stand");

    public Boolean isBlacklisted(Identifier identifier) {
        for (String entity : hatBlacklist) {
            if (identifier.toString().equals(entity)) return true;
        }

        return false;
    }
}
