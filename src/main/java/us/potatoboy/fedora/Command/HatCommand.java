package us.potatoboy.fedora.Command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import us.potatoboy.fedora.Fedora;
import us.potatoboy.fedora.Hat;
import us.potatoboy.fedora.HatManager;

public class HatCommand {
    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            LiteralCommandNode<ServerCommandSource> hatNode = CommandManager
                    .literal("hat")
                    .requires(source -> source.hasPermissionLevel(2))
                    .build();

            LiteralCommandNode<ServerCommandSource> unlockNode = CommandManager
                    .literal("unlock")
                    .then(CommandManager.argument("target", EntityArgumentType.player())
                    .then(CommandManager.argument("hat", StringArgumentType.greedyString())
                    .suggests(new HatSuggestionProvider())
                    .executes(HatCommand::unlock)))
                    .build();

            LiteralCommandNode<ServerCommandSource> removeNode = CommandManager
                    .literal("remove")
                    .then(CommandManager.argument("target", EntityArgumentType.player())
                            .then(CommandManager.argument("hat", StringArgumentType.greedyString())
                                    .suggests(new HatSuggestionProvider())
                                    .executes(HatCommand::remove)))
                    .build();

            dispatcher.getRoot().addChild(hatNode);
            hatNode.addChild(unlockNode);
            hatNode.addChild(removeNode);
        });
    }

    private static int remove(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        PlayerEntity player = EntityArgumentType.getPlayer(context, "target");
        String hatName = StringArgumentType.getString(context, "hat");

        for (String hat : HatManager.getHats().keySet()) {
            if (hat.equalsIgnoreCase(hatName)) {
                Fedora.HAT_COMPONENT.get(player).removeHat(new Hat(hat));
                //TODO make translatable
                context.getSource().sendFeedback(new LiteralText("Removed %s for %s"), true);
                return 1;
            }
        }

        context.getSource().sendError(new LiteralText("Invalid Hat"));
        return 0;
    }

    private static int unlock(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        PlayerEntity player = EntityArgumentType.getPlayer(context, "target");
        String hatName = StringArgumentType.getString(context, "hat");

        for (String hat : HatManager.getHats().keySet()) {
            if (hat.equalsIgnoreCase(hatName)) {
                Fedora.HAT_COMPONENT.get(player).unlockHat(new Hat(hat));
                //TODO make translatable
                context.getSource().sendFeedback(new LiteralText("Unlocked %s for %s"), true);
                return 1;
            }
        }

        context.getSource().sendError(new LiteralText("Invalid Hat"));
        return 0;
    }
}
