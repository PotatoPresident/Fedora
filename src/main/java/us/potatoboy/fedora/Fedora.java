package us.potatoboy.fedora;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import nerdhub.cardinal.components.api.util.RespawnCopyStrategy;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.minecraft.util.Identifier;
import us.potatoboy.fedora.component.PlayerHatComponent;

public class Fedora implements ModInitializer, EntityComponentInitializer {
    public static final ComponentKey<PlayerHatComponent> HAT_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier("fedora", "hats"), PlayerHatComponent.class);
    public static final String MOD_ID = "fedora";

    @Override
    public void onInitialize() {
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry entityComponentFactoryRegistry) {
        entityComponentFactoryRegistry.registerForPlayers(HAT_COMPONENT, PlayerHatComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
