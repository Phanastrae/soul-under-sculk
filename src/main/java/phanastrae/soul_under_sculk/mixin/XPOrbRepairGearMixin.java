package phanastrae.soul_under_sculk.mixin;

import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import phanastrae.soul_under_sculk.item.ModItems;
import phanastrae.soul_under_sculk.item.VerumItem;

@Mixin(ExperienceOrbEntity.class)
public class XPOrbRepairGearMixin { // TODO: check if this accidentally overmends stuff, then decide if i actually care
	@ModifyVariable(method = "repairPlayerGears", at = @At("HEAD"))
	public int SoulUnderSculk_repairPlayerGears(int amount, PlayerEntity player) {
		for(ItemStack stack : player.getItemsHand()) {
			if(stack.getItem().equals(ModItems.VERUM)) {
				amount = VerumItem.consumeXp(stack, amount);
			}
		}
		return amount;
	}
}
