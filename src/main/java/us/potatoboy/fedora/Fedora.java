package us.potatoboy.fedora;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import us.potatoboy.fedora.command.HatCommand;
import us.potatoboy.fedora.component.EntityHatComponent;
import us.potatoboy.fedora.component.PlayerHatComponent;
import us.potatoboy.fedora.config.FedoraConfig;
import us.potatoboy.fedora.packets.CommonPackets;

import java.util.logging.Logger;

public class Fedora implements ModInitializer, EntityComponentInitializer {
    public static final String MOD_ID = "fedora";
    public static final int major_ver = 1;

    public static final ComponentKey<PlayerHatComponent> PLAYER_HAT_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(MOD_ID, "player_hat"), PlayerHatComponent.class);
    public static final ComponentKey<EntityHatComponent> ENTITY_HAT_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(MOD_ID, "entity_hat"), EntityHatComponent.class);

    public static FedoraConfig config;
    public static final Logger LOGGER = Logger.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        AutoConfig.register(FedoraConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(FedoraConfig.class).getConfig();

        HatCommand.init();
        CommonPackets.init();

        HatLoader hatLoader = new HatLoader();
        hatLoader.loadHats();
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry entityComponentFactoryRegistry) {
        entityComponentFactoryRegistry.registerForPlayers(PLAYER_HAT_COMPONENT, PlayerHatComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        entityComponentFactoryRegistry.registerFor(LivingEntity.class, ENTITY_HAT_COMPONENT, e -> new EntityHatComponent());
    }
}
