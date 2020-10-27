package us.potatoboy.fedora.client.GUI;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WDynamicLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import us.potatoboy.fedora.Fedora;
import us.potatoboy.fedora.Hat;
import us.potatoboy.fedora.packets.CommonPackets;

import java.util.ArrayList;

public class HatGUI extends LightweightGuiDescription {
    private Hat currentHat = null;
    private int currentPage = 0;
    private final int pages;
    private PlayerEntity playerEntity;
    private ArrayList<Hat> unlockedHats;

    private final Identifier INFO = new Identifier(Fedora.MOD_ID, "textures/gui/info_icon.png");

    public HatGUI(ClientPlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
        currentHat = Fedora.PLAYER_HAT_COMPONENT.get(playerEntity).getCurrentHat();
        unlockedHats = Fedora.PLAYER_HAT_COMPONENT.get(playerEntity).getUnlockedHats();

        int tempPages = (unlockedHats.size() +6) / 7;
        if (tempPages == 0) tempPages = 1;
        pages = tempPages;

        WPlainPanel root = new WPlainPanel();
        root.setSize(200, 200);
        this.setRootPanel(root);

        WEntity player = new WEntity(playerEntity);
        root.add(player, 0, 0, 80, 200);

        WDynamicLabel pageNumber = new WDynamicLabel(() -> (currentPage + 1) + "/" + pages);
        root.add(pageNumber, 125, 5, 30, 30);

        WButton backwards = new WButton();
        backwards.setLabel(new LiteralText("<"));
        backwards.setOnClick(() -> {
            if (currentPage > 0) {
                --currentPage;
            }
        });
        root.add(backwards, 85, 0, 20, 20);

        WButton forwards = new WButton();
        forwards.setLabel(new LiteralText(">"));
        forwards.setOnClick(() -> {
            if (currentPage + 1 != pages) {
                ++currentPage;
            }
        });
        root.add(forwards, 165, 0, 20, 20);

        ArrayList<Hat> hats = Fedora.PLAYER_HAT_COMPONENT.get(playerEntity).getUnlockedHats();


        for (int i = 0; i < 7; i++) {
            if (hats.size() <= i) return;
            Hat hat = hats.get(i);
            HatChoice hatChoice = new HatChoice(i);
            hatChoice.setSize(100, 5);

            hatChoice.setOnClick(() -> {
                int hatIndex = hatChoice.offset + (currentPage * 7);
                currentHat = unlockedHats.get(hatIndex);

                PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                passedData.writeString(currentHat.id);
                ClientSidePacketRegistry.INSTANCE.sendToServer(CommonPackets.SET_HAT_PACKET_ID, passedData);
            });

            root.add(hatChoice, 85, 25 + (i * 25));
        }
    }

    private class HatChoice extends WButton {
        private int offset;
        private Hat hat;

        public HatChoice(int offset) {
            this.offset = offset;
        }

        @Override
        public boolean canResize() {
            return false;
        }

        @Override
        public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
            int hatIndex = offset + (currentPage * 7);
            if (hatIndex > unlockedHats.size() - 1) return;
            hat = unlockedHats.get(hatIndex);
            setLabel(new TranslatableText("fedora.hat." + hat.id).formatted(hat.rarity.getFormatting()));

            if (this.isFocused()) {
                this.renderTooltip(matrices, x, y, mouseX, mouseY);
            }

            if (currentHat != null && currentHat.equals(hat)) {
                setEnabled(false);
            } else {
                setEnabled(true);
            }
            super.paint(matrices, x, y, mouseX, mouseY);
        }

        @Override
        public void renderTooltip(MatrixStack matrices, int x, int y, int tX, int tY) {
            ArrayList<Text> textList = new ArrayList<>();
            textList.add(new TranslatableText("fedora.text.creator", hat.creator));
            textList.add(new TranslatableText("fedora.text.rarity", new LiteralText(hat.rarity.name()).formatted(hat.rarity.getFormatting())));

            Screen screen = MinecraftClient.getInstance().currentScreen;
            screen.renderTooltip(matrices, textList, tX + x, tY + y);
        }
    }
}
