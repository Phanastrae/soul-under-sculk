package phanastrae.soul_under_sculk.recipe;

import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.registry.Registry;
import phanastrae.soul_under_sculk.SoulUnderSculk;

public class ModRecipes {

	public static SpecialRecipeSerializer<BiomassRecipe> BIOMASS_RECIPE = Registry.register(Registry.RECIPE_SERIALIZER, SoulUnderSculk.id("biomass_recipe"), new SpecialRecipeSerializer<>(BiomassRecipe::new));

	public static void init() {
	}
}
