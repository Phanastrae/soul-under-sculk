package phanastrae.soul_under_sculk;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.*;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;
import org.quiltmc.qsl.item.group.api.QuiltItemGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import phanastrae.soul_under_sculk.block.ModBlocks;
import phanastrae.soul_under_sculk.command.SUSCommand;
import phanastrae.soul_under_sculk.item.ModItems;
import phanastrae.soul_under_sculk.transformation.ModTransformations;
import phanastrae.soul_under_sculk.transformation.TransformationType;

public class SoulUnderSculk implements ModInitializer {

	public static String MOD_ID = "soul_under_sculk";
	public static final RegistryKey<Registry<TransformationType>> TRANSFORMATION_KEY = RegistryKey.ofRegistry(new Identifier("soul_under_sculk_transformation"));
	public static FabricRegistryBuilder<TransformationType, SimpleRegistry<TransformationType>> registryBuilder = FabricRegistryBuilder.createSimple(TransformationType.class, id("transformation"));
	public static SimpleRegistry<TransformationType> TRANSFORMATIONS = registryBuilder.buildAndRegister();

	public static ItemGroup ITEM_GROUP = QuiltItemGroup.builder(id("general")).icon(() -> new ItemStack(ModItems.SCULK_VENT)).build();

	public static final Logger LOGGER = LoggerFactory.getLogger("Soul Under Sculk");

	@Override
	public void onInitialize(ModContainer mod) {
		ModBlocks.init();
		ModItems.init();

		ModTransformations.init();

		CommandRegistrationCallback.EVENT.register(((dispatcher, buildContext, environment) -> {
			SUSCommand.register(dispatcher, buildContext);
		}));
	}

	public static Identifier id(String name) {
		return new Identifier(MOD_ID, name);
	}
}
