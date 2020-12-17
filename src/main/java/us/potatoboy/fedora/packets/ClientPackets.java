package us.potatoboy.fedora.packets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import us.potatoboy.fedora.Fedora;
import us.potatoboy.fedora.Hat;
import us.potatoboy.fedora.HatLoader;
import us.potatoboy.fedora.HatManager;
import us.potatoboy.fedora.client.FedoraClient;
import us.potatoboy.fedora.client.Session;
import us.potatoboy.fedora.client.gui.HatToast;

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
        ClientPlayNetworking.registerGlobalReceiver(CommonPackets.UNLOCK_HAT, (minecraftClient, clientPlayNetworkHandler, packetByteBuf, packetSender) ->  {

            String hatId = packetByteBuf.readString(30);
            minecraftClient.execute(() -> {
                Hat hat = HatManager.getFromID(hatId);

                minecraftClient.getToastManager().add(new HatToast(
                        new TranslatableText("toast.fedora.hatunlocked").formatted(hat.rarity == Hat.Rarity.COMMON ? Formatting.RESET : hat.rarity.getFormatting()),
                        hat.id,
                        30,
                        hat.rarity
                ));
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(CommonPackets.HAT_LIST, ((minecraftClient, clientPlayNetworkHandler, packetByteBuf, packetSender) -> {
            HashSet<String> serverHats = new HashSet<>();

            String hatId = packetByteBuf.readString();
            while (!hatId.equals("END")) {
                serverHats.add(hatId);
                hatId = packetByteBuf.readString();
            }

            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            Boolean needsHats = false;
            for (String serverHatId : serverHats) {
                Hat hat = HatManager.getFromID(serverHatId);

                if (hat == null) {
                    passedData.writeString(serverHatId);
                    needsHats = true;
                }
            }
            FedoraClient.currentSession = new Session(serverHats);

            if (Fedora.config.serverHats == false) return;

            if (needsHats) {
                passedData.writeString("END");

                minecraftClient.execute(() -> {
                    MinecraftClient.getInstance().openScreen(new ConfirmScreen((accepted) -> {
                        if (accepted) {
                            ClientPlayNetworking.send(CommonPackets.REQUEST_HATS, passedData);
                        } else {
                            //Server Hats declined
                        }

                        minecraftClient.openScreen(null);
                    }, new TranslatableText("server.hatPrompt.title"), new TranslatableText("server.hatPrompt.desc")));

                });
            }
        }));

        ClientPlayNetworking.registerGlobalReceiver(CommonPackets.HAT_FILE, ((minecraftClient, clientPlayNetworkHandler, packetByteBuf, packetSender) -> {
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
