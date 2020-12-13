package us.potatoboy.fedora;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import us.potatoboy.fedora.packets.CommonPackets;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;

public class HatManager {
    private static HashSet<Hat> hatRegistry = new HashSet<>();

    public static HashSet<Hat> getHatRegistry() {
        return hatRegistry;
    }

    public static void registerHat(String id, String creator, Hat.Rarity rarity, boolean translucent, boolean ignoreHelmet) {
        hatRegistry.add(new Hat(id, creator, rarity, translucent, ignoreHelmet));
    }

    public static void resetRegistry() {
        hatRegistry = new HashSet<>();
    }

    public static Hat getRandomHat() {
        if (hatRegistry.isEmpty()) return null;

        Random generator = new Random();
        Object[] hatNames = hatRegistry.toArray();
        return (Hat) hatNames[generator.nextInt(hatNames.length)];
    }

    public static Hat getWeightedRandomHat() {
        if (hatRegistry.isEmpty()) return null;

        int totalWeight = 0;
        for (Hat hat : hatRegistry) {
            totalWeight += hat.rarity.getWeight();
        }

        int randomIndex = -1;
        Object[] hatArray = hatRegistry.toArray();
        double random = Math.random() * totalWeight;
        for (int i = 0; i < hatRegistry.size(); ++i)
        {
            random -= ((Hat) hatArray[i]).rarity.getWeight();
            if (random <= 0.0d)
            {
                randomIndex = i;
                break;
            }
        }

        return (Hat) hatArray[randomIndex];
    }

    public static boolean isRegistered(String id) {
        for (Hat hat : hatRegistry) {
            if (hat.id.equals(id)) {
                return true;
            }
        }

        return false;
    }

    public static Hat getFromID(String id) {
        for (Hat hat : hatRegistry) {
            if (hat.id.equals(id)) {
                return hat;
            }
        }

        return null;
    }

    public static void sendHats(PlayerEntity player, String... hatIds) {
        for (String hatId : hatIds) {
            Fedora.LOGGER.info("Sending " + player.getName().asString() + " hat: " + hatId);

            try {
                File model = new File(FabricLoader.getInstance().getConfigDir().toFile(), "fedora/hats/models/" + hatId + ".json");

                JsonElement jsonElement = new JsonParser().parse(new FileReader(model));

                byte[] modelBytes = jsonElement.toString().getBytes();

                PacketByteBuf modelData = new PacketByteBuf(Unpooled.buffer());
                modelData.writeInt(0);
                modelData.writeString(hatId);
                modelData.writeByteArray(modelBytes);

                ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, CommonPackets.HAT_FILE, modelData);

                jsonElement.getAsJsonObject().get("textures").getAsJsonObject().entrySet().forEach(stringJsonElementEntry -> {
                    String key = stringJsonElementEntry.getKey();
                    if (!key.equals("particle")) {
                        try {
                            String textureName = stringJsonElementEntry.getValue().getAsString().replaceAll("fedora:block/", "");
                            File textureFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "fedora/hats/textures/" + textureName + ".png");

                            PacketByteBuf textureData = new PacketByteBuf(Unpooled.buffer());
                            textureData.writeInt(1);
                            textureData.writeString(textureName);

                            BufferedImage in = ImageIO.read(textureFile);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            ImageIO.write(in, "png", baos);
                            byte[] textureBytes = baos.toByteArray();

                            textureData.writeByteArray(textureBytes);

                            ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, CommonPackets.HAT_FILE, textureData);
                        } catch (IOException e) {
                            Fedora.LOGGER.warning("Failed to read texture: " + stringJsonElementEntry.getValue());
                        }
                    }
                });
            } catch (Exception e) {
                Fedora.LOGGER.severe("Something went very wrong sending hat to client " + hatId);
                e.printStackTrace();
            }
        }

        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
        passedData.writeInt(-1);
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, CommonPackets.HAT_FILE, passedData);
    }
}
