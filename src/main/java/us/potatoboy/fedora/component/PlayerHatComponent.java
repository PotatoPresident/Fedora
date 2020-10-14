package us.potatoboy.fedora.component;

import dev.onyxstudios.cca.api.v3.component.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import us.potatoboy.fedora.Fedora;
import us.potatoboy.fedora.Hat;
import us.potatoboy.fedora.HatManager;

public class PlayerHatComponent implements ComponentV3, AutoSyncedComponent {
    private Hat currentHat = null;
    private PlayerEntity playerEntity;

    public PlayerHatComponent (PlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
    }

    public Hat getCurrentHat() {
        return currentHat;
    }

    public void setCurrentHat(Hat currentHat) {
        this.currentHat = currentHat;
        Fedora.HAT_COMPONENT.sync(playerEntity);
    }

    @Override
    public void readFromNbt(CompoundTag compoundTag) {
        if (compoundTag.contains("currenthat")) {
            String hatName = compoundTag.getString("currenthat");

            if(!HatManager.getHats().containsKey(currentHat)) return;
            setCurrentHat(new Hat(hatName));
        } else {
            setCurrentHat(null);
        }
    }

    @Override
    public void writeToNbt(CompoundTag compoundTag) {
        if (currentHat != null) {
            compoundTag.putString("currenthat", currentHat.name);
        }
    }
}