package us.potatoboy.fedora.client;

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
    private static HashMap<Class, HatHelper> HAT_HELPERS = new HashMap<>();
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
            if (livingEntityRenderer instanceof PlayerEntityRenderer) {
                registrationHelper.register(new PlayerHatFeatureRenderer((PlayerEntityRenderer) livingEntityRenderer));
            }
            registrationHelper.register(new HatRenderer<>(livingEntityRenderer));
        }));

        ModelLoadingRegistry.INSTANCE.registerAppender((manager, out) -> {
            HatManager.getHatRegistry().forEach((hat -> {
                out.accept(hat.getModelId());
            }));
        });

        ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
            while (hatKey.wasPressed()) {
                minecraftClient.openScreen(new HatScreen(new HatGUI(minecraftClient.player)));

                HAT_HELPERS = new HashMap<>();
                registerVanillaHelpers();
            }
        });

        registerVanillaHelpers();
    }

    public static void registerHelper(Class entityClass, HatHelper hatHelper) {
        if (LivingEntity.class.isAssignableFrom(entityClass)) {
            HAT_HELPERS.put(entityClass, hatHelper);
        }
    }

    public static HatHelper getHelper(Class entityClass) {
        if (LivingEntity.class.isAssignableFrom(entityClass)) {
            if (HAT_HELPERS.get(entityClass) != null) return HAT_HELPERS.get(entityClass);
        }

        for (Class registeredClass : HAT_HELPERS.keySet()) {
            if (registeredClass.isAssignableFrom(entityClass)) {
                return HAT_HELPERS.get(registeredClass);
            }
        }

        return null;
    }

    private void registerVanillaHelpers() {
        registerHelper(BeeEntity.class, new HatHelper(-0.4, 0, 0, 0.8F));
        registerHelper(BlazeEntity.class, new HatHelper(-0.4, 0, 0, 1F));
        registerHelper(CatEntity.class, new HatHelper(-0.8, 0.1, 0, 0.6F));
        registerHelper(SpiderEntity.class, new HatHelper(-0.3, 0.3, 0, 1F));
        registerHelper(ChickenEntity.class, new HatHelper(0, 0, 0, 0.5F));
        registerHelper(CodEntity.class, new HatHelper(-0.6, 0, 0, 1F));
        registerHelper(CowEntity.class, new HatHelper(-0.4, 0.2, 0, 1F));
        registerHelper(DolphinEntity.class, new HatHelper(-0.1, 0, 0, 1F));
        registerHelper(LlamaEntity.class, new HatHelper(0.8, 0.3, 0, 1F));
        registerHelper(HorseBaseEntity.class, new HatHelper(0.3, 0, 0, 1F));
        registerHelper(GuardianEntity.class, new HatHelper(-1.6, 0, 0, 1F));
        registerHelper(EndermiteEntity.class, new HatHelper(-2.5, 0, 0, 0.2F));
        registerHelper(IllagerEntity.class, new HatHelper(0.2, 0, 0, 1F));
        registerHelper(MerchantEntity.class, new HatHelper(0.2, 0, 0, 1F));
        registerHelper(VillagerEntity.class, new HatHelper(0.2, 0, 0, 1F));
        registerHelper(ZombieVillagerEntity.class, new HatHelper(0.0625, 0, 0, 1F));
        registerHelper(FoxEntity.class, new HatHelper(-0.6, 0.1, -0.1, 1F));
        registerHelper(GhastEntity.class, new HatHelper(-0.2, 0, 0, 2F));
        registerHelper(HoglinEntity.class, new HatHelper(-0.5, 0.4, 0, 1F));
        registerHelper(MagmaCubeEntity.class, new HatHelper(-2.4, 0, 0, 1F));
        registerHelper(OcelotEntity.class, new HatHelper(-0.8, 0.1, 0, 0.6F));
        registerHelper(PandaEntity.class, new HatHelper(-0.3, 0, 0, 1F));
        registerHelper(ParrotEntity.class, new HatHelper(-0.9, 0.4, 0, 0.3F, 4));
        registerHelper(PhantomEntity.class, new HatHelper(-0.6, 0.2, 0, 1F));
        registerHelper(PigEntity.class, new HatHelper(-0.4, 0.2, 0, 1F));
        registerHelper(PolarBearEntity.class, new HatHelper(-0.5, 0, 0, 1F));
        registerHelper(PufferfishEntity.class, new HatHelper(-1.4, 0, 0, 0.2F));
        registerHelper(RabbitEntity.class, new HatHelper(-1.4, 0.2, 0, 0.5F));
        registerHelper(RavagerEntity.class, new HatHelper(0.7, 0, 0, 2F));
        registerHelper(SalmonEntity.class, new HatHelper(-0.6, 0, 0, 1F));
        registerHelper(SheepEntity.class, new HatHelper(-0.4, 0.1, 0, 1F));
        registerHelper(SilverfishEntity.class, new HatHelper(-2.5, 0, 0, 0.2F));
        registerHelper(SlimeEntity.class, new HatHelper(-2.4, 0, 0, 1F));
        registerHelper(StriderEntity.class, new HatHelper(-0.1, 0, 0, 1F));
        registerHelper(TropicalFishEntity.class, new HatHelper(-0.8, 0, 0, 0.5F));
        registerHelper(TurtleEntity.class, new HatHelper(-0.9, 0, 0, 0.5F));
        registerHelper(WolfEntity.class, new HatHelper(-0.5, 0, -0.1, 0.7F));
        registerHelper(ZoglinEntity.class, new HatHelper(-0.5, 0.4, 0, 1F));
        registerHelper(WolfEntity.class, new HatHelper(-0.6, 0, -0.1, 0.7F));
        registerHelper(BatEntity.class, new HatHelper(-0.5, 0, 0, 1F));
        registerHelper(IronGolemEntity.class, new HatHelper(0.4, 0.2, 0, 1F));
        registerHelper(TraderLlamaEntity.class, new HatHelper(0.5, 0.2, 0, 1F));
        registerHelper(WitherEntity.class, new HatHelper(-0.4, 0, 0, 1F));
        registerHelper(SnowGolemEntity.class, new HatHelper(-0.1, 0 , 0, 1F, 2));
    }
}
