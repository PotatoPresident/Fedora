package us.potatoboy.fedora.packets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.TranslatableText;
import us.potatoboy.fedora.Fedora;
import us.potatoboy.fedora.Hat;
import us.potatoboy.fedora.HatLoader;
import us.potatoboy.fedora.HatManager;
import us.potatoboy.fedora.client.GUI.HatToast;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

@Environment(EnvType.CLIENT)
public class ClientPackets {


    public static void init() {
        ClientSidePacketRegistry.INSTANCE.register(CommonPackets.UNLOCK_HAT, ((packetContext, packetByteBuf) -> {

            String hatId = packetByteBuf.readString(30);
            packetContext.getTaskQueue().execute(() -> {
                Hat hat = HatManager.getFromID(hatId);

                MinecraftClient.getInstance().getToastManager().add(new HatToast(
                        new TranslatableText("toast.fedora.hatunlocked"),
                        hat.id,
                        30,
                        hat.rarity
                ));
            });
        }));

        ClientSidePacketRegistry.INSTANCE.register(CommonPackets.HAT_LIST, ((packetContext, packetByteBuf) -> {
            if (Fedora.config.serverHats == false) return;

            HashSet<String> serverHats = new HashSet<>();

            String hatId = packetByteBuf.readString();
            while (!hatId.equals("END")) {
                serverHats.add(hatId);
                hatId = packetByteBuf.readString();
            }

            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            Boolean needsHats = false;
            for (String serverHatId : serverHats) {

                if (HatManager.getFromID(serverHatId) == null) {
                    passedData.writeString(serverHatId);
                    needsHats = true;
                }
            }

            if (needsHats) {
                passedData.writeString("END");

                MinecraftClient.getInstance().execute(() -> {
                    ClientSidePacketRegistry.INSTANCE.sendToServer(CommonPackets.REQUEST_HATS, passedData);
                });
            }
        }));

        ClientSidePacketRegistry.INSTANCE.register(CommonPackets.HAT_FILE, ((packetContext, packetByteBuf) -> {
            int packetType = packetByteBuf.readInt();
            switch (packetType) {
                case 0:
                    String hatId = packetByteBuf.readString();

                    try {
                        byte[] modelBytes = packetByteBuf.readByteArray();
                        Fedora.LOGGER.info("Saving hat " + hatId + " from server");

                        String modelString = new String(modelBytes);

                        JsonObject modelJson = new JsonParser().parse(modelString).getAsJsonObject();


                        File modelFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "fedora/hats/models/" + hatId + ".json");
                        FileWriter fileWriter = new FileWriter(modelFile);
                        new Gson().toJson(modelJson, fileWriter);
                        fileWriter.flush();
                        fileWriter.close();;
                    } catch (IOException e) {
                        Fedora.LOGGER.severe("Failed to save hat from server: " + hatId);
                    }
                    break;
                case 1:
                    String textureName = packetByteBuf.readString();

                    try {
                        byte[] textureBytes = packetByteBuf.readByteArray();
                        Fedora.LOGGER.info("Saving texture " + textureName + " from server");

                        BufferedImage image = ImageIO.read(new ByteArrayInputStream(textureBytes));
                        File textureFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "fedora/hats/textures/" + textureName + ".png");
                        ImageIO.write(image, "png", textureFile);

                    } catch (IOException e) {
                        Fedora.LOGGER.severe("Failed to save texture from server: " + textureName);
                    }

                    break;
                case -1:
                    HatLoader.loadFiles();
                    MinecraftClient.getInstance().reloadResources();
                    MinecraftClient.getInstance().getToastManager().add(new SystemToast(
                            SystemToast.Type.WORLD_BACKUP,
                            new TranslatableText("toast.fedora.serverHats"),
                            new TranslatableText("toast.fedora.serverHats.desc")
                    ));
            }
        }));
    }
}
