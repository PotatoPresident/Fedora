package us.potatoboy.fedora;

import com.google.gson.*;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RRPPreGenEntrypoint;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.lang.JLang;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.logging.Logger;

public class HatLoader implements RRPPreGenEntrypoint {
    private static final Logger LOGGER = Logger.getLogger("fedora");
    private static final FileFilter POSSIBLE_HAT = (file) -> {
        boolean isJson = file.isFile() && file.getName().endsWith(".json");
        return isJson;
    };
    private static final FileFilter POSSIBLE_TEXTURE = (file) -> {
        boolean isPng = file.isFile() && file.getName().endsWith(".png");
        return isPng;
    };
    private File hatsFolder;
    private File modelsFolder;
    private File textFolder;
    public static final RuntimeResourcePack RESOURCE_PACK = RuntimeResourcePack.create("fedora:hats");

    @Override
    public void pregen() {
        hatsFolder = new File(FabricLoader.getInstance().getGameDirectory(), "hats");
        modelsFolder = new File(hatsFolder, "models");
        textFolder = new File(hatsFolder, "textures");

        if (!modelsFolder.isDirectory()) {
            modelsFolder.mkdirs();
        }
        if (!textFolder.isDirectory()) {
            textFolder.mkdirs();
        }

        File[] modelFiles = this.modelsFolder.listFiles(POSSIBLE_HAT);
        File[] textureFiles = this.textFolder.listFiles(POSSIBLE_TEXTURE);

        if (modelFiles != null) {
            HashMap<String, JLang> lang = new HashMap<>();

            for (File file : modelFiles) {
                String hatName = FilenameUtils.removeExtension(file.getName());
                String creator = "Unknown";
                try {
                    JsonObject jsonObject = new JsonObject();

                    try {
                        JsonParser parser = new JsonParser();
                        JsonElement jsonElement = parser.parse(new FileReader(file));
                        jsonObject = jsonElement.getAsJsonObject();

                        creator = jsonObject.get("credit").getAsString();
                    } catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {

                    }

                    RESOURCE_PACK.addAsset(new Identifier("fedora", "models/block/" + hatName + ".json"), jsonObject.toString().getBytes());
                    RESOURCE_PACK.addBlockState(JState.state(JState.variant(JState.model("fedora:block/" + hatName))), new Identifier("fedora:" + hatName));

                    try {
                        jsonObject.get("lang").getAsJsonObject().entrySet().forEach(stringJsonElementEntry -> {
                            lang.putIfAbsent(stringJsonElementEntry.getKey(), JLang.lang());
                            lang.get(stringJsonElementEntry.getKey()).translate("fedora.hat." + hatName, stringJsonElementEntry.getValue().getAsString());
                        });
                    } catch (Exception e) {
                        LOGGER.severe("Failed to read lang for hat: " + file.getPath());
                    }

                } catch (IllegalStateException e) {
                    LOGGER.severe("Something went wrong loading hat: " + file.getPath());
                }

                lang.forEach((id, jLang) -> {
                    RESOURCE_PACK.addLang(new Identifier("fedora", id), jLang);
                });

                HatManager.registerHat(hatName, creator, true);
            }
        }

        if (textureFiles != null) {
            for (File file : textureFiles) {
                try {
                    String textureName = FilenameUtils.removeExtension(file.getName());

                    BufferedImage in = ImageIO.read(file);

                    RESOURCE_PACK.addTexture(new Identifier("fedora", "block/" + textureName), in);
                } catch (IOException e) {
                    LOGGER.severe("Something went wrong loading texture: " + file.getPath());
                }
            }
        }

        RRPCallback.EVENT.register(a -> a.add(RESOURCE_PACK));
    }
}
