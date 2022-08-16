package phanastrae.soul_under_sculk.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandBuildContext;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import phanastrae.soul_under_sculk.SoulUnderSculk;
import phanastrae.soul_under_sculk.transformation.TransformationHandler;
import phanastrae.soul_under_sculk.transformation.TransformationType;
import phanastrae.soul_under_sculk.util.TransformableEntity;

import java.util.Collection;
import java.util.Collections;

public class SUSCommand {
	public static void register(CommandDispatcher dispatcher, CommandBuildContext commandBuildContext) {
		dispatcher.register(
				CommandManager.literal("soulundersculk")
						.requires(source -> source.hasPermissionLevel(2))
						.then(CommandManager.literal("transform")
								.then(
										CommandManager.literal("set")
												.then(
														CommandManager.argument("target", EntityArgumentType.players())
																.then(
																		CommandManager.argument("transformation", TransformationArgumentType.transformation(commandBuildContext))
																				.executes(context ->
																						executeSet(context, EntityArgumentType.getPlayers(context, "target"), TransformationArgumentType.getTransformation(context, "transformation"))
																				)
																)
																.then(
																		CommandManager.literal("none")
																				.executes(context ->
																						executeSet(context, EntityArgumentType.getPlayers(context, "target"), null)
																				)
																)
												)
								)
								.then(
										CommandManager.literal("query")
												.executes(context -> executeQuery(context, Collections.singleton(context.getSource().getPlayer())))
												.then(
														CommandManager.argument("target", EntityArgumentType.players())
																.executes(context -> executeQuery(context, EntityArgumentType.getPlayers(context, "target")))
												)
								)
								.then(
										CommandManager.literal("list")
												.executes(context -> executeList(context.getSource()))
								)
						)
		);
	}

	private static int executeSet(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> targets, TransformationArgument transArgument) {
		int i = 0;

		for(ServerPlayerEntity serverPlayerEntity : targets) {
			TransformationHandler transHandler = ((TransformableEntity)(PlayerEntity)serverPlayerEntity).getTransHandler();
			TransformationType currentTransformation = transHandler.getTransformation();
			TransformationType targetTransformation = (transArgument == null) ? null : transArgument.getTransformation();
			if(currentTransformation != targetTransformation) {
				i++;
				transHandler.setTransformation(targetTransformation);
				if(currentTransformation != null && targetTransformation != null) {
					context.getSource().sendFeedback(Text.translatable("commands.soul_under_sculk.transform.set.yty", serverPlayerEntity.getEntityName(), currentTransformation.getName(), targetTransformation.getName()), true);
				} else if (currentTransformation == null) {
					context.getSource().sendFeedback(Text.translatable("commands.soul_under_sculk.transform.set.nty", serverPlayerEntity.getEntityName(), targetTransformation.getName()), true);
				} else {
					context.getSource().sendFeedback(Text.translatable("commands.soul_under_sculk.transform.set.ytn", serverPlayerEntity.getEntityName(), currentTransformation.getName()), true);
				}
			}
		}

		return i;
	}

	private static int executeQuery(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> targets) {
		int i = 0;

		for(ServerPlayerEntity serverPlayerEntity : targets) {
			TransformationType transformationType = ((TransformableEntity)(PlayerEntity)serverPlayerEntity).getTransHandler().getTransformation();
			if(transformationType == null) {
				context.getSource().sendFeedback(Text.translatable("commands.soul_under_sculk.transform.query.notransform", serverPlayerEntity.getEntityName()), false);
			} else {
				context.getSource().sendFeedback(Text.translatable("commands.soul_under_sculk.transform.query.hastransform", serverPlayerEntity.getEntityName(), transformationType.getName()), false);
				i++;
			}
		}

		return i;
	}

	private static int executeList(ServerCommandSource source) {
		int i = (int)SoulUnderSculk.TRANSFORMATIONS.stream().count();
		source.sendFeedback(Text.translatable("commands.soul_under_sculk.transform.list.header", i), false);
		for (TransformationType transformation : SoulUnderSculk.TRANSFORMATIONS.stream().toList()) {
			source.sendFeedback(Text.translatable("commands.soul_under_sculk.transform.list.element", transformation.getName(), transformation.getRegistryId()), false);
		}
		return i;
	}
}
