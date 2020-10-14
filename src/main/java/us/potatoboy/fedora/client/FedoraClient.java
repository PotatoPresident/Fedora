package us.potatoboy.fedora.client;

import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import nerdhub.cardinal.components.api.util.RespawnCopyStrategy;
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
import org.lwjgl.glfw.GLFW;
import us.potatoboy.fedora.GUI.HatGUI;
import us.potatoboy.fedora.GUI.HatScreen;
import us.potatoboy.fedora.Hat;
import us.potatoboy.fedora.HatFeatureRenderer;
import us.potatoboy.fedora.HatManager;
import us.potatoboy.fedora.component.PlayerHatComponent;

@Environment(EnvType.CLIENT)
public class FedoraClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HatManager.registerHats();

        KeyBinding hatKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fedora.hatmenu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                "category.fedora.hats"
        ));

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(((entityType, livingEntityRenderer, registrationHelper) -> {
            if (livingEntityRenderer instanceof PlayerEntityRenderer) {
                registrationHelper.register(new HatFeatureRenderer((PlayerEntityRenderer) livingEntityRenderer));
            }
        }));

        ModelLoadingRegistry.INSTANCE.registerAppender((manager, out) -> {
            HatManager.getHats().forEach((((name, modelIdentifier) -> {
                out.accept(modelIdentifier);
            })));
        });

        ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
            while (hatKey.wasPressed()) {
                minecraftClient.openScreen(new HatScreen(new HatGUI(minecraftClient.player)));
            }
        });
    }
}
