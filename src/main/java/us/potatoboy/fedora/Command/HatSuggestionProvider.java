package us.potatoboy.fedora.Command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.command.ServerCommandSource;
import us.potatoboy.fedora.Hat;
import us.potatoboy.fedora.HatManager;

import java.util.concurrent.CompletableFuture;

public class HatSuggestionProvider implements SuggestionProvider<ServerCommandSource> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        String hatName = builder.getRemaining().toLowerCase();

        HatManager.getHats().keySet().forEach(hat -> {
            if (hat.toLowerCase().contains(hatName.toLowerCase())) {
                builder.suggest(hat);
            }
        });

        return builder.buildFuture();
    }
}
