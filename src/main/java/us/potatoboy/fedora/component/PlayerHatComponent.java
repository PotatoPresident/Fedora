package us.potatoboy.fedora.component;

import dev.onyxstudios.cca.api.v3.component.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import us.potatoboy.fedora.Fedora;
import us.potatoboy.fedora.Hat;
import us.potatoboy.fedora.HatManager;

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
        Fedora.HAT_COMPONENT.sync(playerEntity);
    }

    public ArrayList<Hat> getUnlockedHats() {
        return unlockedHats;
    }

    public void unlockHat(Hat hat) {
        //TODO Do a toast or something
        if (getHatFromUnlocked(hat) == null) {
            unlockedHats.add(hat);
        }
        Fedora.HAT_COMPONENT.sync(playerEntity);
    }

    public void removeHat(Hat hat) {
        unlockedHats.remove(getHatFromUnlocked(hat));
        Fedora.HAT_COMPONENT.sync(playerEntity);
    }

    private Hat getHatFromUnlocked(Hat hat) {
        for (Hat unlockedHat : unlockedHats) {
            if (unlockedHat.name.equalsIgnoreCase(hat.name)) {
                return unlockedHat;
            }
        }

        return null;
    }

    @Override
    public void readFromNbt(CompoundTag compoundTag) {
        if (compoundTag.contains("currenthat")) {
            String hatName = compoundTag.getString("currenthat");

            if(!HatManager.getHats().containsKey(hatName)) return;
            setCurrentHat(new Hat(hatName));
        } else {
            setCurrentHat(null);
        }

        if (compoundTag.contains("unlockedHats")) {
            unlockedHats = new ArrayList<>();
            ListTag listTag = compoundTag.getList("unlockedHats", 10);
            for(int i = 0; i < listTag.size(); ++i) {
                String hatName = listTag.getCompound(i).getString("name");
                if(!HatManager.getHats().containsKey(hatName)) continue;
                unlockedHats.add(new Hat(hatName));
            }
        }
    }

    @Override
    public void writeToNbt(CompoundTag compoundTag) {
        if (currentHat != null) {
            compoundTag.putString("currenthat", currentHat.name);
        } else {
            compoundTag.putString("currenthat", "");
        }

        if (!unlockedHats.isEmpty()) {
            ListTag listTag = new ListTag();
            unlockedHats.forEach((hat -> {
                CompoundTag compoundTag1 = new CompoundTag();
                compoundTag1.putString("name", hat.name);
                listTag.add(compoundTag1);
            }));

            compoundTag.put("unlockedHats", listTag);
        }
    }
}