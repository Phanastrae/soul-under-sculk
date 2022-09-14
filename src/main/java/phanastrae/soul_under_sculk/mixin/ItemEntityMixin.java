package phanastrae.soul_under_sculk.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import phanastrae.soul_under_sculk.item.ModItems;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	public void SoulUnderSculk_damage(DamageSource source, float amount, CallbackInfoReturnable cir) {
		ItemEntity ie = (ItemEntity)(Object)this;
		if(source == null) return; //(this should probably never happen)
		if(!source.isFire()) return;

		ItemStack stack = ie.getStack();
		if(stack == null) return;

		if(!ModItems.BIOMASS.equals(stack.getItem())) return;

		cir.setReturnValue(false);

		NbtCompound biomassNbt = stack.getSubNbt("Biomass");
		if(biomassNbt == null) return;

		World world = ie.getWorld();
		if(world == null) return;
		if(!world.isClient) {
			world.playSound(null, ie.getX(), ie.getY(), ie.getZ(), SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.BLOCKS, 0.1F, 1.4F);
			((ServerWorld) world).spawnParticles(ParticleTypes.SCULK_SOUL, ie.getX(), ie.getY(), ie.getZ(), 5, ie.getWidth() / 2, ie.getHeight() / 2, ie.getWidth() / 2, 0.04F);

			int[] colors = biomassNbt.getIntArray("Colors");

			int ppercol = 1;
			if (colors.length > 0) {
				ppercol = 24 / colors.length;
			}
			if (ppercol <= 1) ppercol = 1;

			for (int i = 0; i < Math.min(colors.length, 40); i++) {
				int col = colors[i];
				int r = (col & 0xFF0000) >> 16;
				int g = (col & 0xFF00) >> 8;
				int b = (col & 0xFF);
				((ServerWorld) world).spawnParticles(new DustParticleEffect(new Vec3f(r / 255F, g / 255F, b / 255F), 0.35f), ie.getX(), ie.getY(), ie.getZ(), ppercol, ie.getWidth() / 2, ie.getHeight() / 2, ie.getWidth() / 2, 0.4F);
			}
		}

		stack.removeSubNbt("Biomass");
	}
}
