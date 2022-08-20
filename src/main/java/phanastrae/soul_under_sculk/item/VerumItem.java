package phanastrae.soul_under_sculk.item;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import phanastrae.soul_under_sculk.transformation.ModTransformations;
import phanastrae.soul_under_sculk.transformation.TransformationType;
import phanastrae.soul_under_sculk.util.TransformableEntity;
import phanastrae.soul_under_sculk.transformation.TransformationHandler;

import java.util.List;

public class VerumItem extends Item {
	public VerumItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		user.setCurrentHand(hand);
		return TypedActionResult.consume(stack);
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if(!(user instanceof PlayerEntity)) return stack;
		PlayerEntity player = (PlayerEntity)user;
		if(!world.isClient) {
			if (getIsTransCharged(stack)) {
				transformPlayer(stack, player, true);
				player.getItemCooldownManager().set(this, 60);
			} else {
				yoinkXp(player, player, stack, 5);
				player.getItemCooldownManager().set(this, 20);
			}
		}
		return stack;
	}

	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		if(world.isClient) {
			if(getIsTransCharged(stack)) {
				RandomGenerator randomGenerator = world.getRandom();
				for (int i = 0; i < 8; i++) {
					float dx = user.getWidth() * (randomGenerator.nextFloat() - 0.5F);
					float dy = user.getWidth() * (randomGenerator.nextFloat() - 0.5F);
					float dz = user.getWidth() * (randomGenerator.nextFloat() - 0.5F);
					float vx = (randomGenerator.nextFloat() * 2 - 1) * 0.1F;
					float vy = (randomGenerator.nextFloat() * 2 - 1) * 0.1F;
					float vz = (randomGenerator.nextFloat() * 2 - 1) * 0.1F;
					world.addParticle(ParticleTypes.SCULK_CHARGE_POP, user.getX() + dx, user.getY() + dy, user.getZ() + dz, vx, vy, vz);
				}
			}
		}
		super.usageTick(world, user, stack, remainingUseTicks);
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return getIsTransCharged(stack) ? 40 : 12;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.SPEAR;
	}

	public static boolean transformPlayer(ItemStack stack, PlayerEntity player, boolean consumeCharge) {
		if (consumeCharge && !getIsTransCharged(stack)) return false;

		if(consumeCharge) {
			addCharge(stack, -getTransCharge(stack));
		}

		TransformationHandler transHandler = ((TransformableEntity)player).getTransHandler();
		TransformationType currentTransformation = transHandler.getTransformation();
		if(currentTransformation == ModTransformations.SCULKMATE) {
			transHandler.setTransformation(null);
		} else {
			transHandler.setTransformation(ModTransformations.SCULKMATE);
			if(!player.getWorld().isClient) {
				player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 60, 9));
			}
		}
		// TODO: add Egg stage

		if(!player.getWorld().isClient) {
			player.getWorld().playSound(
					null,
					player.getX(),
					player.getY(),
					player.getZ(),
					SoundEvents.ENTITY_ENDERMAN_DEATH,
					SoundCategory.PLAYERS,
					2.0F,
					0.5F
			);
			player.getWorld().playSound(
					null,
					player.getX(),
					player.getY(),
					player.getZ(),
					SoundEvents.ENTITY_ENDERMAN_DEATH,
					SoundCategory.PLAYERS,
					2.0F,
					1.5F
			);
			player.getWorld().playSound(
					null,
					player.getX(),
					player.getY(),
					player.getZ(),
					SoundEvents.ENTITY_WARDEN_ROAR,
					SoundCategory.PLAYERS,
					4.0F,
					1.5F
			);
			((ServerWorld)player.world).spawnParticles(ParticleTypes.SCULK_SOUL, player.getX(), player.getY(), player.getZ(), 64, 0, 0, 0, 0.1F);
		}
		return true;
	}

	public static void yoinkXp(LivingEntity yoinker, PlayerEntity yoinkee, ItemStack stack, int levels) {
		if(!yoinkee.world.isClient) {
			boolean yoinkeeCreative = yoinkee.getAbilities().creativeMode;
			int xpYoinked = 0;
			if(!yoinkeeCreative) {
				for (int i = 0; i < levels; i++) {
					xpYoinked += yoinkee.experienceProgress * yoinkee.getNextLevelExperience();
					if (yoinkee.experienceLevel == 0) {
						yoinkee.experienceProgress = 0.0F;
					} else if (yoinkee.experienceLevel > 0) {
						yoinkee.addExperienceLevels(-1);
						xpYoinked += (1 - yoinkee.experienceProgress) * yoinkee.getNextLevelExperience();
					} else {
						break;
					}
				}
			} else {
				xpYoinked = (int)((getMaxCharge(stack) - getCharge(stack)) * 1.1F); // give bonus because why not
			}
			ExperienceOrbEntity.spawn((ServerWorld) yoinker.world, yoinker.getPos(), xpYoinked);
			if(!yoinkeeCreative) {
				yoinkee.damage(VerumDamageSource.getVerumDamageSource(yoinker), levels); // TODO: add custom damage source and death mesage
			}
		}
	}

	public static int consumeXp(ItemStack stack, int amount, PlayerEntity player) {
		int xpConsumed = Math.min(getMaxCharge(stack) - getCharge(stack), amount);
		if(xpConsumed > 0) {
			amount -= xpConsumed;
			boolean wasCharged = getIsTransCharged(stack);
			addCharge(stack, xpConsumed);
			if(!player.getWorld().isClient && getIsTransCharged(stack) && !wasCharged) {
				player.getWorld().playSound(
						null,
						player.getX(),
						player.getY(),
						player.getZ(),
						SoundEvents.ENTITY_ENDERMAN_SCREAM,
						SoundCategory.PLAYERS,
						3.0F,
						1.5F
				);
				player.getWorld().playSound(
						null,
						player.getX(),
						player.getY(),
						player.getZ(),
						SoundEvents.BLOCK_SCULK_SENSOR_CLICKING,
						SoundCategory.PLAYERS,
						3.0F,
						0.5F
				);
				RandomGenerator random = player.getRandom();
				((ServerWorld)player.world).spawnParticles(ParticleTypes.SCULK_CHARGE_POP, player.getX(), player.getY(), player.getZ(), 32, player.getWidth() / 2, player.getHeight() / 2, player.getWidth() / 2, 0.1F);
			}
		}
		return amount;
	}

	@Override
	public boolean isItemBarVisible(ItemStack stack) {
		NbtCompound nbt = stack.getNbt();
		if(nbt == null) return true;
		return !stack.getNbt().getBoolean("hideItemBar");
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
		TransformationHandler handler = ((TransformableEntity)MinecraftClient.getInstance().player).getTransHandler();
		boolean isSculkmate = handler != null && ModTransformations.SCULKMATE.equals(handler.getTransformation());
		Formatting color = Formatting.WHITE;
		String string = "item.soul_under_sculk.verum";
		if(isSculkmate) {
			string += ".revert";
		} else {
			string += ".transform";
		}
		if(getIsTransCharged(stack)) {
			string += ".ready";
			if(isSculkmate) {
				color = Formatting.RED;
			} else {
				color = Formatting.AQUA;
			}
		} else {
			string += ".not_ready";
		}
		Text debug = context.isAdvanced() ? Text.literal(" ").append(Text.translatable("item.soul_under_sculk.verum.cost", getTransCharge(stack))) : Text.empty();
		tooltip.add(Text.translatable(string).append(debug).formatted(color));

		if(context.isAdvanced()) {
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
