package com.notmarra.notcredits.util;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.notmarra.notcredits.NotCredits;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class Arguments implements CustomArgumentType.Converted<NotCredits.Arguments, String> {
    @Override
    public NotCredits.Arguments convert(String nativeType) throws CommandSyntaxException {
        try {
            return NotCredits.Arguments.valueOf(nativeType);
        } catch (IllegalArgumentException ignored) {
            @NotNull Message message = MessageComponentSerializer.message().serialize(Component.text("Invalid argument type: %s".formatted(nativeType), NamedTextColor.RED));

            throw new CommandSyntaxException(new SimpleCommandExceptionType(message), message);
        }
    }

    @Override
    public @NotNull ArgumentType getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S>CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (NotCredits.Arguments argument : NotCredits.Arguments.values()) {
            builder.suggest(argument.name());
        }

        return builder.buildFuture();
    }
}
