package us.potatoboy.fedora.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.entity.LivingEntity;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;
import us.potatoboy.fedora.Fedora;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;

@Environment(EnvType.CLIENT)
public class EntityHatHelpers implements SimpleSynchronousResourceReloadListener {
    private static HashMap<Class, HatHelper> HAT_HELPERS = new HashMap<>();

    @Override
    public Identifier getFabricId() {
        return new Identifier(Fedora.MOD_ID, "fedora");
    }

    @Override
    public void apply(ResourceManager manager) {
        Collection<Identifier> resources = manager.findResources("fedora", s -> s.equals("entity_hat.json"));
        resources.forEach(resourceIdentifier -> {
            Fedora.LOGGER.info(resourceIdentifier.toString());
            try {
                Resource resource = manager.getResource(resourceIdentifier);
                String json = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
                JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
                jsonObject.entrySet().forEach(entry -> {
                    String key = entry.getKey();
                    String value = entry.getValue().getAsString();


                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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
}
