package us.potatoboy.fedora;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Formatting;

public class Hat {
    public static final Hat NONE = new Hat("none", null, null, false, false);

    public final String id;
    public final String creator;
    public final Rarity rarity;
    public final boolean
            translucent,
            ignoreHelmets;


    public Hat(String id, String creator, Rarity rarity, boolean translucent, boolean ignoreHelmets) {
        this.id = id;
        this.creator = creator;
        this.rarity = rarity;
        this.translucent = translucent;
        this.ignoreHelmets = ignoreHelmets;
    }

    @Environment(EnvType.CLIENT)
    public ModelIdentifier getModelId() {
        return new ModelIdentifier(Fedora.MOD_ID + ":" + id);
    }

    public enum Rarity {
        COMMON(10, Formatting.WHITE),
        UNCOMMON(8, Formatting.YELLOW),
        RARE(4, Formatting.AQUA),
        EPIC(1, Formatting.DARK_PURPLE);

        private int weight;
        private Formatting formatting;

        Rarity(int weight, Formatting formatting) {
            this.weight = weight;
            this.formatting = formatting;
        }

        public int getWeight() {
            return weight;
        }

        public Formatting getFormatting() {
            return formatting;
        }
    }
}
