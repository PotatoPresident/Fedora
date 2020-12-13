package us.potatoboy.fedora.client;

import com.google.common.collect.Maps;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import org.lwjgl.glfw.GLFW;
import us.potatoboy.fedora.HatManager;
import us.potatoboy.fedora.client.FeatureRenderers.HatRenderer;
import us.potatoboy.fedora.client.FeatureRenderers.PlayerHatFeatureRenderer;
import us.potatoboy.fedora.client.GUI.HatGUI;
import us.potatoboy.fedora.client.GUI.HatScreen;
import us.potatoboy.fedora.packets.ClientPackets;

import java.util.HashMap;

@Environment(EnvType.CLIENT)
public class FedoraClient implements ClientModInitializer {
    private static HashMap<Class<? extends LivingEntity>, HatHelper> HAT_HELPERS = Maps.newHashMap();
    public static Session currentSession;

    @Override
    public void onInitializeClient() {
        ClientPackets.init();

        KeyBinding hatKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fedora.hatmenu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                "category.fedora.hats"
        ));

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(((entityType, livingEntityRenderer, registrationHelper) -> {
            if (livingEntityRenderer instanceof PlayerEntityRenderer)
                registrationHelper.register(new PlayerHatFeatureRenderer<>((PlayerEntityRenderer) livingEntityRenderer));
            registrationHelper.register(new HatRenderer<>(livingEntityRenderer));
        }));

        ModelLoadingRegistry.INSTANCE.registerAppender((manager, out) ->
                HatManager.getHatRegistry().forEach((hat -> out.accept(hat.getModelId()))));

        ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
            while (hatKey.wasPressed()) {
                minecraftClient.openScreen(new HatScreen(new HatGUI(minecraftClient.player)));

                HAT_HELPERS = new HashMap<>();
                registerVanillaHelpers();
            }
        });

        registerVanillaHelpers();
    }

    public static <T extends LivingEntity> HatHelper registerHelper(Class<T> clazz, double height, double forward, double side, float scale) {
        return registerHelper(clazz, new HatHelper(height, forward, side, scale));
    }

    public static <T extends LivingEntity> HatHelper registerHelper(Class<T> clazz, HatHelper hatHelper) {
        return HAT_HELPERS.put(clazz, hatHelper);
    }

    public static <T extends LivingEntity> HatHelper getHelper(Class<T> clazz) {
        return HAT_HELPERS.getOrDefault(clazz, registerHelper(clazz, -0.4, 0, 0, 0F));
    }

    private void registerVanillaHelpers() {
        registerHelper(BeeEntity.class, -0.4, 0, 0, 0.8F);
        registerHelper(BlazeEntity.class, -0.4, 0, 0, 1F);
        registerHelper(CatEntity.class, -0.8, 0.1, 0, 0.6F);
        registerHelper(SpiderEntity.class, -0.3, 0.3, 0, 1F);
        registerHelper(ChickenEntity.class, 0, 0, 0, 0.5F);
        registerHelper(CodEntity.class, -0.6, 0, 0, 1F);
        registerHelper(CowEntity.class, -0.4, 0.2, 0, 1F);
        registerHelper(DolphinEntity.class, -0.1, 0, 0, 1F);
        registerHelper(LlamaEntity.class, 0.8, 0.3, 0, 1F);
        registerHelper(HorseBaseEntity.class, 0.3, 0, 0, 1F);
        registerHelper(GuardianEntity.class, -1.6, 0, 0, 1F);
        registerHelper(EndermiteEntity.class, -2.5, 0, 0, 0.2F);
        registerHelper(IllagerEntity.class, 0.2, 0, 0, 1F);
        registerHelper(MerchantEntity.class, 0.2, 0, 0, 1F);
        registerHelper(VillagerEntity.class, 0.2, 0, 0, 1F);
        registerHelper(ZombieVillagerEntity.class, 0.0625, 0, 0, 1F);
        registerHelper(FoxEntity.class, -0.6, 0.1, -0.1, 1F);
        registerHelper(GhastEntity.class, -0.2, 0, 0, 2F);
        registerHelper(HoglinEntity.class, -0.5, 0.4, 0, 1F);
        registerHelper(MagmaCubeEntity.class, -2.4, 0, 0, 1F);
        registerHelper(OcelotEntity.class, -0.8, 0.1, 0, 0.6F);
        registerHelper(PandaEntity.class, -0.3, 0, 0, 1F);
        registerHelper(ParrotEntity.class, -0.9, 0.4, 0, 0.3F, 4);
        registerHelper(PhantomEntity.class, -0.6, 0.2, 0, 1F);
        registerHelper(PigEntity.class, -0.4, 0.4, 0, 1F);
        registerHelper(PolarBearEntity.class, -0.5, 0, 0, 1F);
        registerHelper(PufferfishEntity.class, -1.4, 0, 0, 0.2F);
        registerHelper(RabbitEntity.class, -1.4, 0.2, 0, 0.5F);
        registerHelper(RavagerEntity.class, 0.7, 0, 0, 2F);
        registerHelper(SalmonEntity.class, -0.6, 0, 0, 1F);
        registerHelper(SheepEntity.class, -0.4, 0.1, 0, 1F);
        registerHelper(SilverfishEntity.class, -2.5, 0, 0, 0.2F);
        registerHelper(SlimeEntity.class, -2.4, 0, 0, 1F);
        registerHelper(StriderEntity.class, -0.1, 0, 0, 1F);
        registerHelper(TropicalFishEntity.class, -0.8, 0, 0, 0.5F);
        registerHelper(TurtleEntity.class, -0.9, 0, 0, 0.5F);
        registerHelper(WolfEntity.class, -0.5, 0, -0.1, 0.7F);
        registerHelper(ZoglinEntity.class, -0.5, 0.4, 0, 1F);
        registerHelper(WolfEntity.class, -0.6, 0, -0.1, 0.7F);
        registerHelper(BatEntity.class, -0.5, 0, 0, 1F);
        registerHelper(IronGolemEntity.class, 0.4, 0.2, 0, 1F);
        registerHelper(TraderLlamaEntity.class, 0.5, 0.2, 0, 1F);
        registerHelper(WitherEntity.class, -0.4, 0, 0, 1F);
        registerHelper(SnowGolemEntity.class, -0.1, 0 , 0, 1F, 2);
    }
}
