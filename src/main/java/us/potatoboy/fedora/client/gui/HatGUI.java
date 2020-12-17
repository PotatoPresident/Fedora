package us.potatoboy.fedora.client.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WDynamicLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import us.potatoboy.fedora.Fedora;
import us.potatoboy.fedora.Hat;
import us.potatoboy.fedora.HatManager;
import us.potatoboy.fedora.client.FedoraClient;
import us.potatoboy.fedora.packets.CommonPackets;

import java.util.ArrayList;

public class HatGUI extends LightweightGuiDescription {
    private Hat currentHat = null;
    private int currentPage = 0;
    private final int pages;
    private ArrayList<Hat> unlockedHats;

    WButton backwards;
    WButton forwards;

    private final Identifier INFO = new Identifier(Fedora.MOD_ID, "textures/gui/info_icon.png");

    public HatGUI(ClientPlayerEntity playerEntity) {
        currentHat = Fedora.PLAYER_HAT_COMPONENT.get(playerEntity).getCurrentHat();
        unlockedHats = (ArrayList<Hat>) Fedora.PLAYER_HAT_COMPONENT.get(playerEntity).getUnlockedHats().clone();
        unlockedHats.add(0, Hat.NONE);

        int tempPages = (unlockedHats.size() + 6) / 7;
        if (tempPages == 0) tempPages = 1;
        pages = tempPages;

        WPlainPanel root = new WPlainPanel();
        root.setSize(200, 200);
        this.setRootPanel(root);

        WEntity player = new WEntity(playerEntity);
        root.add(player, 0, 0, 80, 200);

        WDynamicLabel pageNumber = new WDynamicLabel(() -> (currentPage + 1) + "/" + pages);
        root.add(pageNumber, 135, 5, 30, 30);

        backwards = new WButton();
        backwards.setLabel(new LiteralText("<"));
        backwards.setOnClick(() -> {
            changePage(false);
        });
        backwards.setEnabled(currentPage > 0);
        root.add(backwards, 85, 0, 20, 20);

        forwards = new WButton();
        forwards.setLabel(new LiteralText(">"));
        forwards.setOnClick(() -> {
            changePage(true);
        });
        forwards.setEnabled(currentPage + 1 != pages);
        root.add(forwards, 185, 0, 20, 20);

        for (int i = 0; i < 7; i++) {
            if (unlockedHats.size() <= i) return;
            Hat hat = unlockedHats.get(i);
            HatChoice hatChoice = new HatChoice(i);
            hatChoice.setSize(120, 5);

            hatChoice.setOnClick(() -> {
                int hatIndex = hatChoice.offset + (currentPage * 7);
                currentHat = unlockedHats.get(hatIndex);
                if (!currentHat.id.equals("none")) {
                    currentHat = HatManager.getFromID(unlockedHats.get(hatIndex).id);
                }

                PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                passedData.writeString(currentHat.id);
                ClientPlayNetworking.send(CommonPackets.SET_HAT, passedData);

                if (!FedoraClient.currentSession.isOnServer()) {
                    Fedora.PLAYER_HAT_COMPONENT.get(MinecraftClient.getInstance().player).setCurrentHat(currentHat);
                }
            });

            root.add(hatChoice, 85, 25 + (i * 25));
        }
    }

    private void changePage(boolean forward) {
        if (forward) {
            ++currentPage;
        } else {
            --currentPage;
        }

        forwards.setEnabled(currentPage + 1 != pages);
        backwards.setEnabled(currentPage > 0);
    }

    private class HatChoice extends WButton {
        private final int offset;
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
            if (!isVisable()) return;
            hat = unlockedHats.get(hatIndex);

            setLabel(new TranslatableText("fedora.hat." + hat.id));

            if (this.isFocused()) {
                this.renderTooltip(matrices, x, y, mouseX, mouseY);
            }

            if (currentHat != null && currentHat.equals(hat) || currentHat == null && hat.id.equals("none")) {
                setEnabled(false);
            } else {
                setEnabled(true);
            }
            super.paint(matrices, x, y, mouseX, mouseY);
        }

        @Override
        public void renderTooltip(MatrixStack matrices, int x, int y, int tX, int tY) {
            if (!isVisable()) return;
            if (hat.id.equals("none")) return;

            ArrayList<Text> textList = new ArrayList<>();
            textList.add(new TranslatableText("fedora.text.creator", hat.creator));
            textList.add(new TranslatableText("fedora.text.rarity", new LiteralText(hat.rarity.name()).formatted(hat.rarity.getFormatting())));

            Screen screen = MinecraftClient.getInstance().currentScreen;
            screen.renderTooltip(matrices, textList, tX + x, tY + y);
        }

        @Override
        public void onClick(int x, int y, int button) {
            if (!isVisable()) return;

            super.onClick(x, y, button);
        }

        private boolean isVisable() {
            int hatIndex = offset + (currentPage * 7);
            if (hatIndex > unlockedHats.size() - 1) return false;
            return true;
        }
    }
}
