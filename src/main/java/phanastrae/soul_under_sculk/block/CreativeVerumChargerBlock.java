package phanastrae.soul_under_sculk.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import phanastrae.soul_under_sculk.item.ModItems;
import phanastrae.soul_under_sculk.item.VerumItem;

public class CreativeVerumChargerBlock extends Block {
	public CreativeVerumChargerBlock(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			boolean worked = false;
			for(ItemStack is : player.getItemsHand()) {
				if(is != null && ModItems.VERUM.equals(is.getItem())) {
					worked = true;
					VerumItem.consumeXp(is, VerumItem.getMaxCharge(is), player);
					world.playSound(
							null,
							player.getX(),
							player.getY(),
							player.getZ(),
							SoundEvents.ENTITY_PLAYER_LEVELUP,
							SoundCategory.PLAYERS,
							1.0F,
							0.8F
					);
				}
			}
			if(worked) {
				return ActionResult.CONSUME;
			}
			return super.onUse(state, world, pos, player, hand, hit);
		}
	}
}
