package us.potatoboy.fedora;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.util.RespawnCopyStrategy;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import us.potatoboy.fedora.Command.HatCommand;
import us.potatoboy.fedora.component.EntityHatComponent;
import us.potatoboy.fedora.component.PlayerHatComponent;

public class Fedora implements ModInitializer, EntityComponentInitializer {
    public static final Identifier SET_HAT_PACKET_ID = new Identifier("fedora", "sethat");
    public static final ComponentKey<PlayerHatComponent> PLAYER_HAT_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier("fedora", "hats"), PlayerHatComponent.class);
    public static final ComponentType<EntityHatComponent> ENTITY_HAT_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(new Identifier("fedora", "entityhat"), EntityHatComponent.class);
    public static final String MOD_ID = "fedora";

    @Override
    public void onInitialize() {
        HatManager.registerHats();
        HatCommand.init();

        ServerSidePacketRegistry.INSTANCE.register(SET_HAT_PACKET_ID, (packetContext, attachedData) -> {
            String hatId = attachedData.readString(30);
            packetContext.getTaskQueue().execute(() -> {
                // Execute on the main thread

                Hat hat = HatManager.getFromID(hatId);
                if (hat != null) {
                    PlayerHatComponent hatComponent = PLAYER_HAT_COMPONENT.get(packetContext.getPlayer());
                    if (hatComponent.getUnlockedHats().contains(hat)) {
                        hatComponent.setCurrentHat(hat);
                    }
                }

            });
        });
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry entityComponentFactoryRegistry) {
        entityComponentFactoryRegistry.registerForPlayers(PLAYER_HAT_COMPONENT, PlayerHatComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        entityComponentFactoryRegistry.registerFor(LivingEntity.class, ENTITY_HAT_COMPONENT, e -> new EntityHatComponent());
    }
}
