package us.potatoboy.fedora.Command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.command.ServerCommandSource;
import us.potatoboy.fedora.HatManager;

import java.util.concurrent.CompletableFuture;

public class HatSuggestionProvider implements SuggestionProvider<ServerCommandSource> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        String hatName = builder.getRemaining().toLowerCase();

        HatManager.getHats().forEach(hat -> {
            if (hat.id.contains(hatName.toLowerCase())) {
                builder.suggest(hat.id);
            }
        });

        return builder.buildFuture();
    }
}
