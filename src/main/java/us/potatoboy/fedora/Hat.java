package us.potatoboy.fedora;

import net.minecraft.client.util.ModelIdentifier;
import sun.tools.java.Identifier;

public class Hat {
    public final String name;
    public final ModelIdentifier identifier;

    public Hat(String name, ModelIdentifier identifier) {
        this.name = name;
        this.identifier = identifier;
    }

    public Hat(String name) {
        this(name, HatManager.getHats().get(name));
    }
}
