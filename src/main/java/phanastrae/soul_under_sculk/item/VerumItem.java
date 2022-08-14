package phanastrae.soul_under_sculk.item;

import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import phanastrae.soul_under_sculk.block.CreativeVerumChargerBlock;
import phanastrae.soul_under_sculk.block.ModBlocks;

public class VerumItem extends Item {
	public VerumItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if(stack.getItem() instanceof VerumItem) {
			if(getIsTransCharged(stack)) {
				transformPlayer(stack, user);
				user.getItemCooldownManager().set(this, 20);
			} else {
				yoinkXp(user, 5);
				user.getItemCooldownManager().set(this, 20);
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
		player.kill();
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
