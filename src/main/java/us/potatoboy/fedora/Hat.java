package us.potatoboy.fedora;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Formatting;

public class Hat {
    public final String id;
    public final String creator;
    public final boolean obtainable;
    public final Rarity rarity;

    public Hat(String id, String creator, boolean obtainable, Rarity rarity) {
        this.id = id;
        this.creator = creator;
        this.obtainable = obtainable;
        this.rarity = rarity;
    }

    @Environment(EnvType.CLIENT)
    public ModelIdentifier getModelId() {
        return new ModelIdentifier("fedora:" + id);
    }

    public enum Rarity {
        COMMON(10, Formatting.WHITE),
        UNCOMMON(8, Formatting.GREEN),
        RARE(4, Formatting.DARK_BLUE),
        LEGENDARY(1, Formatting.GOLD);

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
