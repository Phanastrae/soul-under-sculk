package phanastrae.soul_under_sculk.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import phanastrae.soul_under_sculk.recipe.BiomassRecipe;
import phanastrae.soul_under_sculk.transformation.CompositeColorEntry;
import phanastrae.soul_under_sculk.util.TimeHelper;

import java.util.List;

public class BiomassItem extends Item {
	public BiomassItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if(user.isSneaking()) {
			NbtCompound biomassNbt = stack.getSubNbt("Biomass");
			if (biomassNbt != null) {
				int[] colorsOld = biomassNbt.getIntArray("Colors");
				if(colorsOld.length >= 2) {
					int[] colorsNew = new int[colorsOld.length];
					colorsNew[0] = colorsOld[colorsOld.length - 1];
					System.arraycopy(colorsOld, 0, colorsNew, 1, colorsOld.length - 1);
					biomassNbt.putIntArray("Colors", colorsNew);
				}

				int[] timesOld = biomassNbt.getIntArray("Times");
				if(timesOld.length >= 2) {
					int[] timesNew = new int[timesOld.length];
					timesNew[0] = timesOld[timesOld.length - 1];
					System.arraycopy(timesOld, 0, timesNew, 1, timesOld.length - 1);
					biomassNbt.putIntArray("Times", timesNew);
				}
				return TypedActionResult.success(stack);
			}
		}

		return TypedActionResult.pass(stack);
	}

	@Override
	public Text getName(ItemStack stack) {
		if(stack == null) return Text.translatable(this.getTranslationKey());

		NbtCompound nbtCompound = stack.getSubNbt("Biomass");
		if (nbtCompound != null) {
			int[] colors = nbtCompound.getIntArray("Colors");
			int[] times = nbtCompound.getIntArray("Times");
			if(colors.length > 0) {
				boolean doInterpolation = nbtCompound.getBoolean("DoInterpolation");
				CompositeColorEntry cce = new CompositeColorEntry(doInterpolation, false);
				for (int i = 0; i < colors.length; i++) {
					int time = (i < times.length) ? times[i] : BiomassRecipe.DEFAULT_TIME;
					cce.addColorEntry(colors[i], time);
				}
				return Text.translatable(this.getTranslationKey()).setStyle(Style.EMPTY.withColor(cce.getColorAtTime(TimeHelper.timeElapsedTicksDouble())));
			}
		}

		return super.getName(stack);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		NbtCompound nbtCompound = stack.getSubNbt("Biomass");
		if(nbtCompound == null) {
			tooltip.add(Text.translatable("item.soul_under_sculk.biomass.is_empty").formatted(Formatting.DARK_GRAY));
			return;
		}

		if(nbtCompound.contains("DoInterpolation")) {
			boolean doInterpolation = nbtCompound.getBoolean("DoInterpolation");
			if(doInterpolation) {
				tooltip.add(Text.translatable("item.soul_under_sculk.biomass.do_interpolation").formatted(Formatting.WHITE).append(Text.literal(" ").append(Text.translatable("item.soul_under_sculk.biomass.use_to_toggle", Text.translatable(Items.GRAVEL.getTranslationKey()))).formatted(Formatting.DARK_GRAY)));
			} else {
				tooltip.add(Text.translatable("item.soul_under_sculk.biomass.do_interpolation.not").formatted(Formatting.WHITE).append(Text.literal(" ").append(Text.translatable("item.soul_under_sculk.biomass.use_to_toggle", Text.translatable(Items.GLASS.getTranslationKey())).formatted(Formatting.DARK_GRAY))));
			}
		}
		if(nbtCompound.contains("ScaleWithSpeed")) {
			boolean scaleWithSpeed = nbtCompound.getBoolean("ScaleWithSpeed");
			if(scaleWithSpeed) {
				tooltip.add(Text.translatable("item.soul_under_sculk.biomass.scale_with_speed").formatted(Formatting.WHITE).append(Text.literal(" ").append(Text.translatable("item.soul_under_sculk.biomass.use_to_toggle", Text.translatable(Items.IRON_NUGGET.getTranslationKey())).formatted(Formatting.DARK_GRAY))));
			} else {
				tooltip.add(Text.translatable("item.soul_under_sculk.biomass.scale_with_speed.not").formatted(Formatting.WHITE).append(Text.literal(" ").append(Text.translatable("item.soul_under_sculk.biomass.use_to_toggle", Text.translatable(Items.GOLD_NUGGET.getTranslationKey())).formatted(Formatting.DARK_GRAY))));
			}
		}

		int[] colors = nbtCompound.getIntArray("Colors");
		int[] times = nbtCompound.getIntArray("Times");
		int j = (int)Math.min(colors.length, times.length) - 1;
		for(int i = j; i >= 0; i--) {
			int time = times[i];
			tooltip.add(Text.literal(String.format("%x: #%06x ", i+1, (0xFFFFFF & colors[i]))).append(Text.translatable("item.soul_under_sculk.biomass.ticks", time)).setStyle(Style.EMPTY.withColor(colors[i])));
			if(i == j) {
				tooltip.add(Text.translatable("item.soul_under_sculk.biomass.use_to_lengthen", Text.translatable(Items.REDSTONE.getTranslationKey()), BiomassRecipe.CHANGE_NORMAL).formatted(Formatting.DARK_GRAY));
				tooltip.add(Text.translatable("item.soul_under_sculk.biomass.use_to_shorten", Text.translatable(Items.GLOWSTONE.getTranslationKey()), BiomassRecipe.CHANGE_NORMAL).formatted(Formatting.DARK_GRAY));
				tooltip.add(Text.translatable("item.soul_under_sculk.biomass.use_to_change", Text.translatable(Items.QUARTZ.getTranslationKey()), BiomassRecipe.CHANGE_BIG).formatted(Formatting.DARK_GRAY));
				tooltip.add(Text.translatable("item.soul_under_sculk.biomass.use_to_change", Text.translatable(Items.SOUL_SAND.getTranslationKey()), BiomassRecipe.CHANGE_SMALL).formatted(Formatting.DARK_GRAY));
			}
		}
	}

}
