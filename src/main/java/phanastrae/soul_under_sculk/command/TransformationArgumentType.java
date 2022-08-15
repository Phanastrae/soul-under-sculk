package phanastrae.soul_under_sculk.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandBuildContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.HolderLookup;
import phanastrae.soul_under_sculk.SoulUnderSculk;
import phanastrae.soul_under_sculk.transformation.TransformationType;

import java.util.concurrent.CompletableFuture;

public class TransformationArgumentType implements ArgumentType<TransformationArgument> {

	private final HolderLookup<TransformationType> transformations;

	public TransformationArgumentType(CommandBuildContext commandBuildContext) {
		this.transformations = commandBuildContext.holderLookup(SoulUnderSculk.TRANSFORMATIONS.getKey());
	}

	@Override
	public TransformationArgument parse(StringReader reader) throws CommandSyntaxException {
		TransformationArgumentParser.TransformationResult transformationResult = TransformationArgumentParser.parseForTransformation(this.transformations, reader);
		return new TransformationArgument(transformationResult.transformationType()); //TODO: add nbt stuff
	}


	public static TransformationArgumentType transformation(CommandBuildContext context) {
		return new TransformationArgumentType(context);
	}

	public static TransformationArgument getTransformation(CommandContext<ServerCommandSource> context, String name) {
		return context.getArgument(name, TransformationArgument.class);
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContext, SuggestionsBuilder suggestionsBuilder) {
		return TransformationArgumentParser.getSuggestions(this.transformations, suggestionsBuilder);
	}
}
