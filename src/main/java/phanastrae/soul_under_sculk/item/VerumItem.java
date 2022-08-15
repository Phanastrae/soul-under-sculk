package phanastrae.soul_under_sculk.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import phanastrae.soul_under_sculk.block.CreativeVerumChargerBlock;
import phanastrae.soul_under_sculk.transformation.ModTransformations;
import phanastrae.soul_under_sculk.transformation.TransformationType;
import phanastrae.soul_under_sculk.util.PlayerEntityExtension;
import phanastrae.soul_under_sculk.transformation.TransformationHandler;

import java.util.List;

public class VerumItem extends Item {
	public VerumItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if(stack.getItem() instanceof VerumItem) {
			if(world instanceof ServerWorld) {
				if (getIsTransCharged(stack)) {
					transformPlayer(stack, user);
					user.getItemCooldownManager().set(this, 20);
				} else {
					yoinkXp(user, 5);
					user.getItemCooldownManager().set(this, 20);
				}
			}
			return TypedActionResult.success(stack);
		}
		return TypedActionResult.fail(stack);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if(context.getWorld().getBlockState(context.getBlockPos()).getBlock() instanceof CreativeVerumChargerBlock) {
			setCharge(context.getStack(), getMaxCharge(context.getStack()));
			return ActionResult.SUCCESS;
		}
		return ActionResult.PASS;
	}

	public static boolean transformPlayer(ItemStack stack, PlayerEntity player) {
		if (!getIsTransCharged(stack)) return false;

		addCharge(stack, -getTransCharge(stack));

		TransformationHandler transHandler = ((PlayerEntityExtension)player).getTransHandler();
		TransformationType transType = transHandler.getTransformation();
		if(transType == ModTransformations.SCULKMATE) {
			transHandler.setTransformation(null);
		} else {
			transHandler.setTransformation(ModTransformations.SCULKMATE);
		}
		// TODO: add Egg stage
		return true;
	}

	public static void yoinkXp(PlayerEntity player, int levels) {
		if(player.world instanceof ServerWorld) {
			int xpYoinked = 0;
			for (int i = 0; i < levels; i++) {
				xpYoinked += player.experienceProgress * player.getNextLevelExperience();
				if (player.experienceLevel == 0) {
					player.experienceProgress = 0.0F;
				} else if (player.experienceLevel > 0) {
					player.addExperienceLevels(-1);
					xpYoinked += (1 - player.experienceProgress) * player.getNextLevelExperience();
				} else {
					break;
				}
			}
			ExperienceOrbEntity.spawn((ServerWorld) player.world, player.getPos(), xpYoinked);
			player.damage(DamageSource.thorns(player), levels); // TODO: add custom damage source and death mesage
		}
	}

	public static int consumeXp(ItemStack stack, int amount) {
		int xpConsumed = Math.min(getMaxCharge(stack) - getCharge(stack), amount);
		if(xpConsumed > 0) {
			amount -= xpConsumed;
			addCharge(stack, xpConsumed);
		}
		return amount;
	}

	@Override
	public boolean isItemBarVisible(ItemStack stack) {
		return true;
	}

	@Override
	public int getItemBarColor(ItemStack stack) {
		return getIsTransCharged(stack) ? 0x00ffff : 0xffff00;
	}

	@Override
	public int getItemBarStep(ItemStack stack) {
		int barCap = getIsTransCharged(stack) ? getMaxCharge(stack) : getTransCharge(stack);
		return Math.max(0, Math.min(13, Math.round(getCharge(stack) * 13.0F / barCap)));
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return getIsTransCharged(stack) || super.hasGlint(stack); // TODO: see if this color can be changed, maybe scrap it entirely and just change item model instead
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		if(!context.isAdvanced()) {
			if (getIsTransCharged(stack)) {
				tooltip.add(Text.translatable("item.soul_under_sculk.trans_ready").formatted(Formatting.AQUA));
			} else {
				tooltip.add(Text.translatable("item.soul_under_sculk.trans_not_ready").formatted(Formatting.WHITE));
			}
		} else {
			if (getIsTransCharged(stack)) {
				tooltip.add(Text.translatable("item.soul_under_sculk.trans_ready_debug", getTransCharge(stack)).formatted(Formatting.AQUA));
			} else {
				tooltip.add(Text.translatable("item.soul_under_sculk.trans_not_ready_debug", getTransCharge(stack)).formatted(Formatting.WHITE));
			}
			tooltip.add(Text.translatable("item.soul_under_sculk.charge", getCharge(stack), getMaxCharge(stack)).formatted(Formatting.GREEN));
		}
	}

	public static int getCharge(ItemStack stack) {
		return stack.getNbt() == null ? 0 : stack.getNbt().getInt("XPCharge");
	}

	public static void setCharge(ItemStack stack, int charge) {
		stack.getOrCreateNbt().putInt("XPCharge", Math.min(Math.max(0, charge), getMaxCharge(stack)));
	}

	public static void addCharge(ItemStack stack, int charge) {
		stack.getOrCreateNbt().putInt("XPCharge", Math.min(Math.max(0, getCharge(stack) + charge), getMaxCharge(stack)));
	}

	public static int getMaxCharge(ItemStack stack) {
		return 2000; // TODO: add config option? or make this a more thought-through number? maybe make it dynamic based on upgrades?
	}

	public static int getTransCharge(ItemStack stack) {
		return 500; // TODO: make transformation charge change based on upgrades?
	}

	public static boolean getIsTransCharged(ItemStack stack) {
		return getCharge(stack) >= getTransCharge(stack);
	}
}
