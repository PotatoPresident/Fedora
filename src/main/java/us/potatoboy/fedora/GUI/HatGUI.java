package us.potatoboy.fedora.GUI;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.TranslatableText;
import us.potatoboy.fedora.Fedora;
import us.potatoboy.fedora.Hat;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class HatGUI extends LightweightGuiDescription {
    private static String currentHatId = null;

    public HatGUI(ClientPlayerEntity playerEntity) {
        Hat currentHat = Fedora.PLAYER_HAT_COMPONENT.get(playerEntity).getCurrentHat();
        if (currentHat != null) {
            currentHatId = Fedora.PLAYER_HAT_COMPONENT.get(playerEntity).getCurrentHat().id;
        }

        WPlainPanel root = new WPlainPanel();
        root.setSize(200, 200);
        this.setRootPanel(root);

        WEntity player = new WEntity(playerEntity);
        root.add(player, 0, 0, 100, 200);

        ArrayList<Hat> hats = Fedora.PLAYER_HAT_COMPONENT.get(playerEntity).getUnlockedHats();
        BiConsumer<Hat, HatChoice> configurator = (Hat hat, HatChoice hatChoice) -> {
            hatChoice.setLabel(new TranslatableText("fedora.hat." + hat.id));
            hatChoice.setSize(75, 5);
            hatChoice.setHatId(hat.id);

            if (currentHat != null && currentHat.id.equals(hat.id)) {
                hatChoice.setEnabled(false);
            } else {
                hatChoice.setEnabled(true);
            }

            hatChoice.setOnClick(() -> {
                currentHatId = hat.id;

                PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                passedData.writeString(hat.id);
                ClientSidePacketRegistry.INSTANCE.sendToServer(Fedora.SET_HAT_PACKET_ID, passedData);
            });
        };
        WListPanel<Hat, HatChoice> list = new WListPanel<>(hats, HatChoice::new, configurator);
        root.add(list, 100, 0, 100, 200);
    }

    private static class HatChoice extends WButton {
        private String hatId = null;

        public HatChoice() {
        }

        @Override
        public boolean canResize() {
            return false;
        }

        @Override
        public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
            if (currentHatId != null && currentHatId.equals(hatId)) {
                setEnabled(false);
            } else {
                setEnabled(true);
            }
            super.paint(matrices, x, y, mouseX, mouseY);
        }

        public void setHatId(String id) {
            hatId = id;
        }
    }
}
