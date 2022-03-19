package us.potatoboy.fedora.component;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import us.potatoboy.fedora.Fedora;
import us.potatoboy.fedora.Hat;
import us.potatoboy.fedora.HatManager;
import us.potatoboy.fedora.client.FedoraClient;
import us.potatoboy.fedora.packets.CommonPackets;

import java.util.ArrayList;

public class PlayerHatComponent implements ComponentV3, AutoSyncedComponent {
    private Hat currentHat;
    private ArrayList<Hat> unlockedHats;
    private final PlayerEntity playerEntity;

    public PlayerHatComponent (PlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
        unlockedHats = new ArrayList<>();
    }

    public Hat getCurrentHat() {
        return currentHat;
    }

    public void setCurrentHat(Hat currentHat) {
        this.currentHat = currentHat;
        Fedora.PLAYER_HAT_COMPONENT.sync(playerEntity);
    }

    public ArrayList<Hat> getUnlockedHats() {
        if (playerEntity.isCreative()) {
            ArrayList<Hat> hats;
            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
                hats = new ArrayList<>(FedoraClient.currentSession.getSessionHats());
            } else {
                hats = new ArrayList<>(HatManager.getHatRegistry());
            }

            return hats;
        }

        ArrayList<Hat> hats;
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            if (playerEntity.isCreative()) {
                hats = new ArrayList<>(FedoraClient.currentSession.getSessionHats());
              
                return hats;
            }

            if (!FedoraClient.currentSession.isOnServer()) {
                hats = new ArrayList<>(HatManager.getHatRegistry());

                return hats;
            }
        } else {
            if (playerEntity.isCreative()) {
                hats = new ArrayList<>(HatManager.getHatRegistry());

                return hats;
            }
        }

        return unlockedHats;
    }

    public void unlockHat(Hat hat) {
        if (!unlockedHats.contains(hat)) {
            unlockedHats.add(hat);

            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            passedData.writeString(hat.id, hat.id.length());
            ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerEntity, CommonPackets.UNLOCK_HAT, passedData);

            Fedora.PLAYER_HAT_COMPONENT.sync(playerEntity);
        }
    }

    public void removeHat(Hat hat) {
        unlockedHats.remove(hat);
        if (currentHat == hat) {
            currentHat = null;
        }
        Fedora.PLAYER_HAT_COMPONENT.sync(playerEntity);
    }

    @Override
    public void readFromNbt(NbtCompound compoundTag) {
        if (compoundTag.contains("currenthat")) {
            String hatId = compoundTag.getString("currenthat");

            Hat hat = HatManager.getFromID(hatId);
            if (hat != null) {
                setCurrentHat(hat);
            } else {
                setCurrentHat(null);
            }
        } else {
            setCurrentHat(null);
        }

        if (compoundTag.contains("unlockedHats")) {
            unlockedHats = new ArrayList<>();

            NbtList listTag = compoundTag.getList("unlockedHats", 10);
            for(int i = 0; i < listTag.size(); ++i) {
                String hatId = listTag.getCompound(i).getString("name");
                Hat hat = HatManager.getFromID(hatId);
                if (hat != null) {
                    unlockedHats.add(hat);
                }
            }
        } else {
            unlockedHats = new ArrayList<>();
        }
    }

    @Override
    public void writeToNbt(NbtCompound compoundTag) {
        if (currentHat != null) {
            compoundTag.putString("currenthat", currentHat.id);
        } else {
            compoundTag.putString("currenthat", "");
        }

        if (!unlockedHats.isEmpty()) {
            NbtList listTag = new NbtList();
            unlockedHats.forEach((hat -> {
                NbtCompound compoundTag1 = new NbtCompound();
                compoundTag1.putString("name", hat.id);
                listTag.add(compoundTag1);
            }));

            compoundTag.put("unlockedHats", listTag);
        }
    }
}