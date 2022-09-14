package phanastrae.soul_under_sculk.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import phanastrae.soul_under_sculk.item.ModItems;

@Mixin(ItemStack.class)
public class ItemStackMixin {

	@ModifyVariable(method = "getName", at = @At("STORE"))
	private Text SoulUnderSculk_getName(Text text) {
		if(text == null) return null;
		ItemStack stack = (ItemStack)(Object)this;
		Item item = stack.getItem();
		if(!ModItems.BIOMASS.equals(item)) return text;
		if(stack.getSubNbt("Biomass") == null) return text;
		Text defaultText = stack.getItem().getName(stack);
		if(defaultText == null) return text;
		Style style = defaultText.getStyle();
		return text.copy().setStyle(style);
	}
}
