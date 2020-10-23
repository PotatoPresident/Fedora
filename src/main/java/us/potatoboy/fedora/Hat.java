package us.potatoboy.fedora;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.ModelIdentifier;

public class Hat {
    public final String id;
    public final String creator;
    public final boolean obtainable;

    public Hat(String id, String creator, boolean obtainable) {
        this.id = id;
        this.creator = creator;
        this.obtainable = obtainable;
    }

    @Environment(EnvType.CLIENT)
    public ModelIdentifier getModelId() {
        return new ModelIdentifier("fedora:" + id);
    }
}
