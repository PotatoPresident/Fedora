package us.potatoboy.fedora;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.util.RespawnCopyStrategy;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import us.potatoboy.fedora.command.HatCommand;
import us.potatoboy.fedora.component.EntityHatComponent;
import us.potatoboy.fedora.component.PlayerHatComponent;
import us.potatoboy.fedora.packets.CommonPackets;

public class Fedora implements ModInitializer, EntityComponentInitializer {

    public static final ComponentKey<PlayerHatComponent> PLAYER_HAT_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier("fedora", "hats"), PlayerHatComponent.class);
    public static final ComponentType<EntityHatComponent> ENTITY_HAT_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(new Identifier("fedora", "entityhat"), EntityHatComponent.class);
    public static final String MOD_ID = "fedora";

    @Override
    public void onInitialize() {
        HatCommand.init();
        CommonPackets.init();
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry entityComponentFactoryRegistry) {
        entityComponentFactoryRegistry.registerForPlayers(PLAYER_HAT_COMPONENT, PlayerHatComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        entityComponentFactoryRegistry.registerFor(LivingEntity.class, ENTITY_HAT_COMPONENT, e -> new EntityHatComponent());
    }
}
