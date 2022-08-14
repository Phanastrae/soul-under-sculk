package phanastrae.soul_under_sculk.item;

import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import phanastrae.soul_under_sculk.SoulUnderSculk;

public class VerumItem extends Item {
	public VerumItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if(stack.getItem() instanceof VerumItem) {
			yoinkXp(user, 5);
			return TypedActionResult.success(stack);
		}
		return TypedActionResult.fail(stack);
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
		return 65467; // 65467 = #00ffbb
	}

	@Override
	public int getItemBarStep(ItemStack stack) {
		return Math.max(0, Math.min(13, Math.round(getCharge(stack) * 13.0F / getMaxCharge(stack))));
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
		return 550; // should be amount of xp for level 20; doesn't really matter.
	}
}
