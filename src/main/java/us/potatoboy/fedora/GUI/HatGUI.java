package us.potatoboy.fedora.GUI;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.LiteralText;
import us.potatoboy.fedora.Fedora;
import us.potatoboy.fedora.Hat;
import us.potatoboy.fedora.HatManager;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class HatGUI extends LightweightGuiDescription {
    public HatGUI(ClientPlayerEntity playerEntity) {
        WPlainPanel root = new WPlainPanel();
        root.setSize(200, 200);
        this.setRootPanel(root);

        WEntity player = new WEntity(playerEntity);
        root.add(player, 0, 0, 100, 200);

        ArrayList<String> hats = new ArrayList<>(HatManager.getHats().keySet());
        BiConsumer<String, HatChoice> configurator = (String s, HatChoice hatChoice) -> {
            hatChoice.setLabel(new LiteralText(s));
            hatChoice.setSize(75, 5);

            hatChoice.setOnClick(() -> {
                Hat hat = new Hat(s);
                Fedora.HAT_COMPONENT.get(playerEntity).setCurrentHat(hat);
            });
        };
        WListPanel<String, HatChoice> list = new WListPanel<>(hats, HatChoice::new, configurator);
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
    }
}
