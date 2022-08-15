package phanastrae.soul_under_sculk.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Holder;
import net.minecraft.util.HolderLookup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import org.jetbrains.annotations.Nullable;
import phanastrae.soul_under_sculk.SoulUnderSculk;
import phanastrae.soul_under_sculk.transformation.TransformationType;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class TransformationArgumentParser {

	private final HolderLookup<TransformationType> transformations;
	private final StringReader reader;
	@Nullable
	private TransformationType transformationType;
	private Identifier transformationId = new Identifier("");
	private static final Function<SuggestionsBuilder, CompletableFuture<Suggestions>> SUGGEST_DEFAULT = SuggestionsBuilder::buildFuture;
	private Function<SuggestionsBuilder, CompletableFuture<Suggestions>> suggestions = SUGGEST_DEFAULT;


	private TransformationArgumentParser(HolderLookup<TransformationType> holderLookup, StringReader stringReader) {
		this.transformations = holderLookup;
		this.reader = stringReader;
	}

	public static final DynamicCommandExceptionType INVALID_TRANSFORMATION_ID_EXCEPTION = new DynamicCommandExceptionType(
			id -> Text.translatable("argument.soul_under_sculk.transformation.id.invalid", id)
	);

	public static CompletableFuture<Suggestions> getSuggestions(HolderLookup<TransformationType> transformations, SuggestionsBuilder builder) {
		StringReader stringReader = new StringReader(builder.getInput());
		stringReader.setCursor(builder.getStart());
		TransformationArgumentParser transformationArgumentParser = new TransformationArgumentParser(transformations, stringReader);

		try {
			transformationArgumentParser.parse();
		} catch (CommandSyntaxException var7) {
		}

		return (CompletableFuture<Suggestions>)transformationArgumentParser.suggestions.apply(builder.createOffset(stringReader.getCursor()));
	}

	private CompletableFuture<Suggestions> suggestTransformation(SuggestionsBuilder builder) {
		return CommandSource.suggestIdentifiers(this.transformations.streamElements().map(RegistryKey::getValue), builder);
	}

	private void parse() throws CommandSyntaxException {
		this.suggestions = this::suggestTransformation;
		this.parseTransformationId();
	}

	private void parseTransformationId() throws CommandSyntaxException {
		int i = this.reader.getCursor();
		this.transformationId = Identifier.fromCommandInput(this.reader);
		TransformationType transformation = (TransformationType) ((Holder)this.transformations.get(RegistryKey.of(SoulUnderSculk.TRANSFORMATIONS.getKey(), this.transformationId)).orElseThrow(() -> {
			this.reader.setCursor(i);
			return INVALID_TRANSFORMATION_ID_EXCEPTION.createWithContext(this.reader, this.transformationId.toString());
		})).value();
		this.transformationType = transformation;
	}

	public static TransformationArgumentParser.TransformationResult parseForTransformation(HolderLookup<TransformationType> transformations, StringReader reader) throws CommandSyntaxException {
		int i = reader.getCursor();

		try {
			TransformationArgumentParser transformationArgumentParser = new TransformationArgumentParser(transformations, reader);
			transformationArgumentParser.parse();
			return new TransformationArgumentParser.TransformationResult(transformationArgumentParser.transformationType);
		} catch (CommandSyntaxException e) {
			reader.setCursor(i);
			throw e;
		}
	}


	public static record TransformationResult(TransformationType transformationType) {
	}
}
