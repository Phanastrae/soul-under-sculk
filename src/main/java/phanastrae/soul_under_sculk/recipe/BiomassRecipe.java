package phanastrae.soul_under_sculk.recipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import phanastrae.soul_under_sculk.item.ModItems;
import phanastrae.soul_under_sculk.transformation.CompositeColorEntry;

import java.util.List;
import java.util.Map;

public class BiomassRecipe extends SpecialCraftingRecipe {

	public static final Map<DyeColor, float[]> DYE_COLORS = Util.make(Maps.newHashMap(), hashMap -> {
		hashMap.put(DyeColor.WHITE, new float[]{1F, 1F, 1F});
		hashMap.put(DyeColor.LIGHT_GRAY, new float[]{0.65F, 0.65F, 0.65F});
		hashMap.put(DyeColor.GRAY, new float[]{0.25F, 0.25F, 0.25F});
		hashMap.put(DyeColor.BLACK, new float[]{0F, 0F, 0F});
		hashMap.put(DyeColor.RED, new float[]{1F, 0F, 0F});
		hashMap.put(DyeColor.GREEN, new float[]{0F, 1F, 0F});
		hashMap.put(DyeColor.BLUE, new float[]{0F, 0F, 1F});
		hashMap.put(DyeColor.YELLOW, new float[]{1F, 1F, 0F});
		hashMap.put(DyeColor.MAGENTA, new float[]{1F, 0F, 1F});
		hashMap.put(DyeColor.CYAN, new float[]{0F, 1F, 1F});
		hashMap.put(DyeColor.PINK, new float[]{1F, 0.65F, 0.85F});
		hashMap.put(DyeColor.LIME, new float[]{0.65F, 1F, 0.4F});
		hashMap.put(DyeColor.LIGHT_BLUE, new float[]{0.65F, 0.85F, 1F});
		hashMap.put(DyeColor.PURPLE, new float[]{0.75F, 0F, 1F});
		hashMap.put(DyeColor.ORANGE, new float[]{1F, 0.65F, 0F});
		hashMap.put(DyeColor.BROWN, new float[]{0.5F, 0.35F, 0F});
	});

	public static final Map<Item, String> VERUM_ITEM_KEY_MAP = Util.make(Maps.newHashMap(), hashMap -> {
		hashMap.put(Items.ENDER_EYE, "Eye");
		hashMap.put(Items.SCULK, "Sculk");
		hashMap.put(Items.SCULK_SENSOR, "GlowSculk");
		hashMap.put(Items.BONE_BLOCK, "Bone");
		hashMap.put(Items.OBSIDIAN, "Obsidian");
		hashMap.put(Items.GLOWSTONE, "Glowstone");
		hashMap.put(Items.CRYING_OBSIDIAN, "Crying");
		hashMap.put(Items.GUNPOWDER, "Particle");
	});

	public static final Map<Item, String> EAR_ITEM_NAME_MAP = Util.make(Maps.newHashMap(), hashMap -> {
		hashMap.put(Items.STONE, "Variant1");
		hashMap.put(Items.DEEPSLATE, "Variant2");
		hashMap.put(Items.WHITE_WOOL, "Fluffy");
		hashMap.put(Items.LIGHT_GRAY_WOOL, "Fluffy");
		hashMap.put(Items.GRAY_WOOL, "Fluffy");
		hashMap.put(Items.BLACK_WOOL, "Fluffy");
		hashMap.put(Items.RED_WOOL, "Fluffy");
		hashMap.put(Items.YELLOW_WOOL, "Fluffy");
		hashMap.put(Items.GREEN_WOOL, "Fluffy");
		hashMap.put(Items.CYAN_WOOL, "Fluffy");
		hashMap.put(Items.BLUE_WOOL, "Fluffy");
		hashMap.put(Items.MAGENTA_WOOL, "Fluffy");
		hashMap.put(Items.PINK_WOOL, "Fluffy");
		hashMap.put(Items.LIME_WOOL, "Fluffy");
		hashMap.put(Items.LIGHT_BLUE_WOOL, "Fluffy");
		hashMap.put(Items.PURPLE_WOOL, "Fluffy");
		hashMap.put(Items.ORANGE_WOOL, "Fluffy");
		hashMap.put(Items.BROWN_WOOL, "Fluffy");
		hashMap.put(Items.MOSS_BLOCK, "Ogre");
		hashMap.put(Items.LAPIS_BLOCK, "Troll");
		hashMap.put(Items.WHITE_BED, "Loss");
		hashMap.put(Items.GLASS_BOTTLE, "Empty");
	});

	public static final int DEFAULT_TIME = 60;
	public static final int MIN_TIME = 5;
	public static final int MAX_TIME = 20*60*60; //max 1 hour
	public static final int CHANGE_NORMAL = 20;
	public static final int CHANGE_SMALL = 1;
	public static final int CHANGE_BIG = 600;

	private static final Ingredient BIOMASS = Ingredient.ofItems(ModItems.BIOMASS);
	private static final Ingredient VERUM = Ingredient.ofItems(ModItems.VERUM);
	private static final Ingredient VERUM_MODIFIERS = Ingredient.ofItems(Items.ENDER_EYE, Items.SCULK, Items.SCULK_SENSOR, Items.BONE_BLOCK, Items.OBSIDIAN, Items.GLOWSTONE, Items.CRYING_OBSIDIAN, Items.GUNPOWDER);
	private static final Ingredient EAR_MODIFIERS = Ingredient.ofItems(Items.STONE, Items.DEEPSLATE, Items.WHITE_WOOL, Items.LIGHT_GRAY_WOOL, Items.GRAY_WOOL, Items.BLACK_WOOL, Items.RED_WOOL, Items.YELLOW_WOOL, Items.GREEN_WOOL, Items.CYAN_WOOL, Items.BLUE_WOOL, Items.MAGENTA_WOOL, Items.PINK_WOOL, Items.LIME_WOOL, Items.LIGHT_BLUE_WOOL, Items.PURPLE_WOOL, Items.ORANGE_WOOL, Items.BROWN_WOOL, Items.MOSS_BLOCK, Items.LAPIS_BLOCK, Items.WHITE_BED, Items.GLASS_BOTTLE);
	private static final Ingredient SHORTEN = Ingredient.ofItems(Items.GLOWSTONE_DUST);
	private static final Ingredient LENGTHEN = Ingredient.ofItems(Items.REDSTONE);
	private static final Ingredient SMALL_CHANGE = Ingredient.ofItems(Items.SOUL_SAND);
	private static final Ingredient BIG_CHANGE = Ingredient.ofItems(Items.QUARTZ);
	private static final Ingredient ANY_CHANGE_MOD = Ingredient.ofItems(Items.QUARTZ, Items.SOUL_SAND);
	private static final Ingredient ENABLE_SPEEDSCALE = Ingredient.ofItems(Items.GOLD_NUGGET);
	private static final Ingredient DISABLE_SPEEDSCALE = Ingredient.ofItems(Items.IRON_NUGGET);
	private static final Ingredient ENABLE_SMOOTH = Ingredient.ofItems(Items.GLASS);
	private static final Ingredient DISABLE_SMOOTH = Ingredient.ofItems(Items.GRAVEL);

	public BiomassRecipe(Identifier identifier) {
		super(identifier);
	}

	@Override
	public boolean matches(CraftingInventory inventory, World world) {
		boolean hasVerum = false;
		boolean hasBiomass = false;
		boolean hasVerumModifier = false;
		boolean hasEarModifier = false;
		boolean hasDye = false;
		boolean hasLengthen = false;
		boolean hasShorten = false;
		boolean hasChangeMod = false;
		boolean hasEnableSpeedScale = false;
		boolean hasDisableSpeedScale = false;
		boolean hasEnableSmooth = false;
		boolean hasDisableSmooth = false;

		ItemStack biomass = null;
		for (int i = 0; i < inventory.size(); ++i) {
			ItemStack itemStack = inventory.getStack(i);
			if (!itemStack.isEmpty()) {
				if (BIOMASS.test(itemStack)) {
					if (hasBiomass) {
						return false;
					}
					hasBiomass = true;
					biomass = itemStack;
				} else if (VERUM.test(itemStack)) {
					if (hasVerum) {
						return false;
					}
					hasVerum = true;
				} else if (VERUM_MODIFIERS.test(itemStack)) {
					if (hasVerumModifier) {
						return false;
					}
					hasVerumModifier = true;
				} else if (EAR_MODIFIERS.test(itemStack)) {
					if (hasEarModifier) {
						return false;
					}
					hasEarModifier = true;
				} else if (LENGTHEN.test(itemStack)) {
					if (hasShorten) {
						return false;
					}
					hasLengthen = true;
				} else if (SHORTEN.test(itemStack)) {
					if (hasLengthen) {
						return false;
					}
					hasShorten = true;
				} else if (ANY_CHANGE_MOD.test(itemStack)) {
					if (hasChangeMod) {
						return false;
					}
					hasChangeMod = true;
				} else if (ENABLE_SPEEDSCALE.test(itemStack)) {
					if (hasEnableSpeedScale || hasDisableSpeedScale) {
						return false;
					}
					hasEnableSpeedScale = true;
				} else if (DISABLE_SPEEDSCALE.test(itemStack)) {
					if (hasEnableSpeedScale || hasDisableSpeedScale) {
						return false;
					}
					hasDisableSpeedScale = true;
				} else if (ENABLE_SMOOTH.test(itemStack)) {
					if (hasEnableSmooth || hasDisableSmooth) {
						return false;
					}
					hasEnableSmooth = true;
				} else if (DISABLE_SMOOTH.test(itemStack)) {
					if (hasEnableSmooth || hasDisableSmooth) {
						return false;
					}
					hasDisableSmooth = true;
				} else if (itemStack.getItem() instanceof DyeItem) {
					hasDye = true;
				} else {
					return false;
				}
			}
		}
		boolean canShorten = false;
		boolean canLengthen = false;
		boolean biomassDyed = false;
		boolean currentSpeedScale = CompositeColorEntry.DEFAULT_SCALE_WITH_SPEED;
		boolean currentSmooth = CompositeColorEntry.DEFAULT_DO_INTERPOLTION;
		if (biomass != null) {
			NbtCompound nbt = biomass.getSubNbt("Biomass");
			if (nbt != null) {
				int[] times = nbt.getIntArray("Times");
				if(!hasDye) {
					if(times != null && times.length >= 1) {
						if (times[times.length - 1] > MIN_TIME) canShorten = true;
						if (times[times.length - 1] < MAX_TIME) canLengthen = true;
					}
				} else {
					canLengthen = canShorten = true;
				}
				if (nbt.getIntArray("Colors").length > 0) biomassDyed = true;
				if(nbt.contains("ScaleWithSpeed")) {
					currentSpeedScale = nbt.getBoolean("ScaleWithSpeed");
				}
				if(nbt.contains("DoInterpolation")) {
					currentSmooth = nbt.getBoolean("DoInterpolation");
				}
			} else {
				if(hasDye) {
					canShorten = canLengthen = true;
				}
			}
		}

		boolean conflictVerum = (hasVerum || hasVerumModifier) && (hasDye || hasShorten || hasLengthen || hasEnableSmooth || hasDisableSmooth || hasEnableSpeedScale || hasDisableSpeedScale || hasChangeMod);
		boolean conflictNoVerum = !hasVerum && (hasVerumModifier || hasEarModifier);
		boolean conflictVerumAddition = (hasBiomass || hasVerumModifier) && hasEarModifier;
		boolean conflictShortenLengthen = (hasShorten && hasLengthen);
		boolean conflictFailedShorten = (hasShorten && !canShorten);
		boolean conflictFailedLengthen = (hasLengthen && !canLengthen);
		boolean conflictModifyNothing = hasChangeMod && !hasShorten && !hasLengthen;
		boolean settingsConflict = (hasEnableSmooth && hasDisableSmooth) || (hasEnableSpeedScale && hasDisableSpeedScale);
		boolean targetSettingsAlready = (hasEnableSmooth && currentSmooth) || (hasDisableSmooth && !currentSmooth) || (hasEnableSpeedScale && currentSpeedScale) || (hasDisableSpeedScale && !currentSpeedScale);
		if (conflictVerum || conflictShortenLengthen || conflictFailedShorten || conflictFailedLengthen || conflictModifyNothing || settingsConflict || conflictNoVerum || conflictVerumAddition || targetSettingsAlready) return false;
		if (hasBiomass && (hasDye || (biomassDyed || hasDye) && (hasShorten || hasLengthen || hasEnableSmooth || hasDisableSmooth || hasEnableSpeedScale || hasDisableSpeedScale))) return true;
		if(hasVerum && hasBiomass && hasVerumModifier) return true;
		if(hasVerum && hasEarModifier && !hasBiomass) return true;
		return false;
	}

	@Override
	public ItemStack craft(CraftingInventory inventory) {
		ItemStack output = getOutput();
		NbtCompound outputNbt = output.getOrCreateSubNbt("Biomass");

		int lengthChange = 0;
		int lengthChangeAmount = CHANGE_NORMAL;
		boolean changeSpeedScale = false;
		boolean targetSpeedScale = CompositeColorEntry.DEFAULT_SCALE_WITH_SPEED;
		boolean changeSmooth = false;
		boolean targetSmooth = CompositeColorEntry.DEFAULT_DO_INTERPOLTION;
		ItemStack biomass = null;
		ItemStack verum = null;
		String verumBiomassKey = "";
		String earKey = "";
		List<float[]> colorList = Lists.newArrayList();
		for(int i = 0; i < inventory.size(); ++i) {
			ItemStack stack = inventory.getStack(i);
			if (!stack.isEmpty()) {
				if (BIOMASS.test(stack)) {
					biomass = stack;
				} else if (VERUM.test(stack)) {
					verum = stack;
				} else if (VERUM_MODIFIERS.test(stack)) {
					if(stack.getItem() != null) {
						verumBiomassKey = VERUM_ITEM_KEY_MAP.get(stack.getItem());
					}
				} else if (EAR_MODIFIERS.test(stack)) {
					if(stack.getItem() != null) {
						earKey = EAR_ITEM_NAME_MAP.get(stack.getItem());
					}
				} else if (LENGTHEN.test(stack)) {
					lengthChange += 1;
				} else if (SHORTEN.test(stack)) {
					lengthChange -= 1;
				} else if (SMALL_CHANGE.test(stack)) {
					lengthChangeAmount = CHANGE_SMALL;
				} else if (BIG_CHANGE.test(stack)) {
					lengthChangeAmount = CHANGE_BIG;
				} else if (ENABLE_SPEEDSCALE.test(stack)) {
					targetSpeedScale = true;
					changeSpeedScale = true;
				} else if (DISABLE_SPEEDSCALE.test(stack)) {
					targetSpeedScale = false;
					changeSpeedScale = true;
				} else if (ENABLE_SMOOTH.test(stack)) {
					targetSmooth = true;
					changeSmooth = true;
				} else if (DISABLE_SMOOTH.test(stack)) {
					targetSmooth = false;
					changeSmooth = true;
				} else if (stack.getItem() instanceof DyeItem) {
					DyeColor dyeColor = ((DyeItem)stack.getItem()).getColor();
					float [] color = DYE_COLORS.get(dyeColor);
					if(color == null) {
						color = ((DyeItem) stack.getItem()).getColor().getColorComponents();
					}
					if(color != null && color.length == 3) {
						colorList.add(color);
					}
				}
			}
		}

		if(verum != null) {
			boolean verumUpdated = false;
			ItemStack verumOutput = new ItemStack(ModItems.VERUM);
			if(verumBiomassKey != null && verumBiomassKey != "") {
				verumOutput = verum.copy();
				NbtCompound verumBiomassNbt = verumOutput.getOrCreateSubNbt("Biomass");
				NbtCompound biomassBiomassNbt = biomass.getOrCreateSubNbt("Biomass");
				verumBiomassNbt.put(verumBiomassKey, biomassBiomassNbt);
				verumUpdated = true;
			}
			if(earKey != null && earKey != "") {
				verumOutput = verum.copy();
				NbtCompound verumBiomassNbt = verumOutput.getOrCreateSubNbt("Biomass");
				verumBiomassNbt.putString("EarType", earKey);
				verumUpdated = true;
			}
			if(verumUpdated) return verumOutput;
			return ItemStack.EMPTY;
		}

		if(biomass == null) return ItemStack.EMPTY;
		NbtCompound inputNbt = biomass.getSubNbt("Biomass");
		List<Integer> colors = Lists.newArrayList();
		List<Integer> times = Lists.newArrayList();
		if(inputNbt != null) {
			int[] colorsArray = inputNbt.getIntArray("Colors");
			int[] timesArray = inputNbt.getIntArray("Times");
			for(int i = 0; i < colorsArray.length; i++) {
				colors.add(colorsArray[i]);
				if(i < timesArray.length) {
					times.add(timesArray[i]);
				} else {
					times.add(DEFAULT_TIME);
				}
			}
		}

		float[] sumColor = new float[]{0, 0, 0};
		int added = 0;
		for(float[] color : colorList) {
			if(color.length == 3) {
				sumColor[0] += color[0];
				sumColor[1] += color[1];
				sumColor[2] += color[2];
				added++;
			}
		}
		if(added >= 1) {
			int rColor = (int)Math.max(0, Math.min(255, 255F * sumColor[0] / added));
			int gColor = (int)Math.max(0, Math.min(255, 255F * sumColor[1] / added));
			int bColor = (int)Math.max(0, Math.min(255, 255F * sumColor[2] / added));

			colors.add((rColor << 16) + (gColor << 8) + (bColor));
			times.add(DEFAULT_TIME);
		}

		if(lengthChange != 0 && times.size() > 0) {
			int time = times.get(times.size() - 1);
			time += lengthChangeAmount * lengthChange;
			time = (int)Math.max(MIN_TIME, Math.min(MAX_TIME, time));
			times.set(times.size() - 1, time);
		}


		outputNbt.putBoolean("ScaleWithSpeed", targetSpeedScale);
		outputNbt.putBoolean("DoInterpolation", targetSmooth);

		if(inputNbt != null) {
			if (inputNbt.contains("ScaleWithSpeed") && !changeSpeedScale) {
				outputNbt.putBoolean("ScaleWithSpeed", inputNbt.getBoolean("ScaleWithSpeed"));
			}
			if (inputNbt.contains("DoInterpolation") && !changeSmooth) {
				outputNbt.putBoolean("DoInterpolation", inputNbt.getBoolean("DoInterpolation"));
			}
		}

		outputNbt.putIntArray("Colors", colors);
		outputNbt.putIntArray("Times", times);

		return output;
	}

	@Override
	public boolean fits(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public ItemStack getOutput() {
		return new ItemStack(ModItems.BIOMASS);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipes.BIOMASS_RECIPE;
	}
}
