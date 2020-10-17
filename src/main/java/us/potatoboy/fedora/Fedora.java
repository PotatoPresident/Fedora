package us.potatoboy.fedora;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import nerdhub.cardinal.components.api.util.RespawnCopyStrategy;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.util.Identifier;
import us.potatoboy.fedora.Command.HatCommand;
import us.potatoboy.fedora.Command.HatSuggestionProvider;
import us.potatoboy.fedora.component.PlayerHatComponent;

public class Fedora implements ModInitializer, EntityComponentInitializer {
    public static final Identifier SET_HAT_PACKET_ID = new Identifier("fedora", "sethat");
    public static final ComponentKey<PlayerHatComponent> HAT_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier("fedora", "hats"), PlayerHatComponent.class);
    public static final String MOD_ID = "fedora";

    @Override
    public void onInitialize() {
        HatCommand.init();

        ServerSidePacketRegistry.INSTANCE.register(SET_HAT_PACKET_ID, (packetContext, attachedData) -> {
            String hatname = attachedData.readString(30);
            packetContext.getTaskQueue().execute(() -> {
                // Execute on the main thread

                Hat hat = new Hat(hatname);
                if (hat != null) {
                    HAT_COMPONENT.get(packetContext.getPlayer()).setCurrentHat(hat);
                }

            });
        });
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry entityComponentFactoryRegistry) {
        entityComponentFactoryRegistry.registerForPlayers(HAT_COMPONENT, PlayerHatComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
