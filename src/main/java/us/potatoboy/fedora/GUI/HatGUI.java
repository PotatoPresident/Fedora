package us.potatoboy.fedora.GUI;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import us.potatoboy.fedora.Fedora;
import us.potatoboy.fedora.Hat;
import us.potatoboy.fedora.HatManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.BiConsumer;

public class HatGUI extends LightweightGuiDescription {
    private static String currentHatName = null;

    public HatGUI(ClientPlayerEntity playerEntity) {
        Hat currentHat = Fedora.HAT_COMPONENT.get(playerEntity).getCurrentHat();
        if (currentHat != null) {
            currentHatName = Fedora.HAT_COMPONENT.get(playerEntity).getCurrentHat().name;
        }

        WPlainPanel root = new WPlainPanel();
        root.setSize(200, 200);
        this.setRootPanel(root);

        WEntity player = new WEntity(playerEntity);
        root.add(player, 0, 0, 100, 200);

        ArrayList<Hat> hats = Fedora.HAT_COMPONENT.get(playerEntity).getUnlockedHats();
        BiConsumer<Hat, HatChoice> configurator = (Hat hat, HatChoice hatChoice) -> {
            hatChoice.setLabel(new LiteralText(hat.name));
            hatChoice.setSize(75, 5);
            ModelIcon icon = new ModelIcon(hat.identifier);
            hatChoice.setIcon(icon);

            if (currentHat != null && currentHatName.equals(hatChoice.getLabel().asString())) {
                hatChoice.setEnabled(false);
            } else {
                hatChoice.setEnabled(true);
            }

            hatChoice.setOnClick(() -> {
                currentHatName = hat.name;

                PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                passedData.writeString(hat.name);
                ClientSidePacketRegistry.INSTANCE.sendToServer(Fedora.SET_HAT_PACKET_ID, passedData);
            });
        };
        WListPanel<Hat, HatChoice> list = new WListPanel<>(hats, HatChoice::new, configurator);
        root.add(list, 100, 0, 100, 200);
    }

    private static class HatChoice extends WButton {
        public HatChoice() {
            this.setLabel(new LiteralText("Temp"));
        }

        @Override
        public boolean canResize() {
            return false;
        }

        @Override
        public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
            if (currentHatName != null && currentHatName.equals(getLabel().asString())) {
                setEnabled(false);
            } else {
                setEnabled(true);
            }
            super.paint(matrices, x, y, mouseX, mouseY);
        }
    }
}
