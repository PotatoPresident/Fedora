package us.potatoboy.fedora.component;

import dev.onyxstudios.cca.api.v3.component.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.nbt.CompoundTag;
import us.potatoboy.fedora.Fedora;
import us.potatoboy.fedora.Hat;
import us.potatoboy.fedora.HatManager;

import java.util.Random;

public class EntityHatComponent implements ComponentV3, AutoSyncedComponent {
    private Hat currentHat;

    public Hat getCurrentHat() {
        return currentHat;
    }

    public EntityHatComponent() {
        if (new Random().nextInt(100) < Fedora.config.hatChance) {
            currentHat = HatManager.getWeightedRandomHat();
        } else {
            currentHat = null;
        }
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        if (tag.contains("currentHat")) {
            String hatId = tag.getString("currentHat");

            if (hatId.isEmpty()) {
                currentHat = null;
            } else {
                Hat hat = HatManager.getFromID(hatId);
                if (hat != null) {
                    currentHat = hat;
                }
            }
        }
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        if (currentHat != null) {
            tag.putString("currentHat", currentHat.id);
        } else {
            tag.putString("currentHat", "");
        }
    }
}
