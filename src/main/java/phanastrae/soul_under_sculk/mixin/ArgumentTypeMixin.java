package phanastrae.soul_under_sculk.mixin;

import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.command.argument.ArgumentTypeInfo;
import net.minecraft.command.argument.ArgumentTypeInfos;
import net.minecraft.command.argument.SingletonArgumentInfo;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import phanastrae.soul_under_sculk.command.TransformationArgumentType;

// no idea if this is how i should be doing this but looks like the normal way to do it might just not exist in 1.19?
// i may just be doing this wrong though
@Mixin(ArgumentTypeInfos.class)
public abstract class ArgumentTypeMixin {

	@Shadow
	private static <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> ArgumentTypeInfo<A, T> register(
			Registry<ArgumentTypeInfo<?, ?>> registry, String id, Class<? extends A> entry, ArgumentTypeInfo<A, T> type
	) {
		return null;
	};

	@Inject(method = "bootstrap", at = @At("HEAD"))
	private static void SoulUnderSculk_bootstrap(Registry<ArgumentTypeInfo<?, ?>> registry, CallbackInfoReturnable ci) {
		register(registry, "soul_under_sculk:transformation", TransformationArgumentType.class, SingletonArgumentInfo.contextAware(TransformationArgumentType::transformation));
	}
}
