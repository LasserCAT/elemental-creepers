package T145.elementalcreepers.entities;

import T145.elementalcreepers.config.ModConfig;
import T145.elementalcreepers.entities.base.EntityBaseCreeper;
import T145.elementalcreepers.explosion.ExplosionWind;
import T145.elementalcreepers.explosion.base.ExplosionSpecial;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class EntityWindCreeper extends EntityBaseCreeper {

	public EntityWindCreeper(World world) {
		super(world);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		fallDistance = 0.0F;

		if (!onGround && motionY < 0.0D) {
			motionY *= 0.6D;
		}
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
	}

	@Override
	public void createExplosion(int explosionPower, boolean canGrief) {
		if (!world.isRemote) {
			int radius = ModConfig.explosionRadii.windCreeperRadius * explosionPower;
			Biome biome = world.getBiome(pos.setPos(this));
			boolean causesFire = biome != null && (BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.WASTELAND));
			ExplosionSpecial explosion = new ExplosionWind(world, this, posX, posY, posZ, ModConfig.explosionPower.windCreeperPower, radius, causesFire, canGrief);
			explosion.doExplosionA();
		}
	}
}