package T145.elementalcreepers.entities;

import T145.elementalcreepers.config.ModConfig;
import T145.elementalcreepers.entities.base.EntityBaseCreeper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityIceCreeper extends EntityBaseCreeper {

	public EntityIceCreeper(World world) {
		super(world);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (world.isRemote || !world.getGameRules().getBoolean("mobGriefing")) {
			return;
		}

		for (int i, j, k, l = 0; l < 4; ++l) {
			i = MathHelper.floor(posX + ((l % 2 * 2 - 1) * 0.25F));
			j = MathHelper.floor(posY);
			k = MathHelper.floor(posZ + ((l / 2 % 2 * 2 - 1) * 0.25F));
			pos.setPos(i, j, k);

			if (world.getBlockState(pos).getMaterial() == Material.AIR && Blocks.SNOW_LAYER.canPlaceBlockAt(world, pos)) {
				world.setBlockState(pos, Blocks.SNOW_LAYER.getDefaultState());
			}
		}
	}

	@Override
	public void createExplosion(int explosionPower, boolean canGrief) {
		int radius = getPowered() ? ModConfig.explosionRadii.iceCreeperRadius * explosionPower : ModConfig.explosionRadii.iceCreeperRadius;

		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				for (int z = -radius; z <= radius; z++) {
					pos.setPos(posX + x, posY + y, posZ + z);
					IBlockState state = world.getBlockState(pos);

					if (state != null && state.getBlock() != null) {
						Block block = state.getBlock();

						if (block == Blocks.WATER || block == Blocks.FLOWING_WATER) {
							world.setBlockState(pos, Blocks.ICE.getDefaultState());
						} else if (block == Blocks.LAVA || block == Blocks.FLOWING_LAVA) {
							world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
						}
					}
				}
			}
		}

		if (ModConfig.general.domeExplosion) {
			domeExplosion(radius, Blocks.SNOW);
		} else {
			for (int x = -radius; x <= radius; x++) {
				for (int y = -radius; y <= radius; y++) {
					for (int z = -radius; z <= radius; z++) {
						pos.setPos(posX + x, posY + y, posZ + z);

						if (Blocks.DIRT.canPlaceBlockAt(world, pos) && !Blocks.DIRT.canPlaceBlockAt(world, new BlockPos(posX + x, posY + y - 1, posZ + z))) {
							if (rand.nextBoolean()) {
								world.setBlockState(pos, Blocks.SNOW_LAYER.getDefaultState());
							} else {
								world.setBlockState(pos, Blocks.SNOW.getDefaultState());
							}
						}
					}
				}
			}
		}
	}
}