package ru.betterend.world.features.terrain.caves;

import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;

import com.google.common.collect.Sets;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.world.biome.cave.EndCaveBiome;
import ru.betterend.world.features.DefaultFeature;

public class CaveChunkPopulatorFeature extends DefaultFeature {
	private Supplier<EndCaveBiome> supplier;
	
	public CaveChunkPopulatorFeature(Supplier<EndCaveBiome> biome) {
		this.supplier = biome;
	}
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		Set<BlockPos> floorPositions = Sets.newHashSet();
		Set<BlockPos> ceilPositions = Sets.newHashSet();
		int sx = (pos.getX() >> 4) << 4;
		int sz = (pos.getZ() >> 4) << 4;
		Mutable min = new Mutable().set(pos);
		Mutable max = new Mutable().set(pos);
		fillSets(sx, sz, world.getChunk(pos), floorPositions, ceilPositions, min, max);
		EndCaveBiome biome = supplier.get();
		BlockState surfaceBlock = biome.getBiome().getGenerationSettings().getSurfaceConfig().getTopMaterial();
		placeFloor(world, biome, floorPositions, random, surfaceBlock);
		placeCeil(world, biome, ceilPositions, random);
		BlocksHelper.fixBlocks(world, min, max);
		return true;
	}
	
	protected void fillSets(int sx, int sz, Chunk chunk, Set<BlockPos> floorPositions, Set<BlockPos> ceilPositions, Mutable min, Mutable max) {
		Mutable mut = new Mutable();
		Mutable mut2 = new Mutable();
		Mutable mut3 = new Mutable();
		for (int x = 0; x < 16; x++) {
			mut.setX(x);
			mut2.setX(x);
			for (int z = 0; z < 16; z++) {
				mut.setZ(z);
				mut2.setZ(z);
				mut2.setY(0);
				for (int y = 1; y < chunk.getHeight(); y++) {
					mut.setY(y);
					BlockState top = chunk.getBlockState(mut);
					BlockState bottom = chunk.getBlockState(mut2);
					if (top.isAir() && (bottom.isIn(EndTags.GEN_TERRAIN) || bottom.isOf(Blocks.STONE))) {
						mut3.set(mut2).move(sx, 0, sz);
						floorPositions.add(mut3.toImmutable());
						updateMin(mut3, min);
						updateMax(mut3, max);
					}
					else if (bottom.isAir() && (top.isIn(EndTags.GEN_TERRAIN)|| top.isOf(Blocks.STONE))) {
						mut3.set(mut).move(sx, 0, sz);
						ceilPositions.add(mut3.toImmutable());
						updateMin(mut3, min);
						updateMax(mut3, max);
					}
					mut2.setY(y);
				}
			}
		}
	}
	
	private void updateMin(BlockPos pos, Mutable min) {
		if (pos.getX() < min.getX()) {
			min.setX(pos.getX());
		}
		if (pos.getY() < min.getY()) {
			min.setY(pos.getY());
		}
		if (pos.getZ() < min.getZ()) {
			min.setZ(pos.getZ());
		}
	}
	
	private void updateMax(BlockPos pos, Mutable max) {
		if (pos.getX() > max.getX()) {
			max.setX(pos.getX());
		}
		if (pos.getY() > max.getY()) {
			max.setY(pos.getY());
		}
		if (pos.getZ() > max.getZ()) {
			max.setZ(pos.getZ());
		}
	}
	
	protected void placeFloor(StructureWorldAccess world, EndCaveBiome biome, Set<BlockPos> floorPositions, Random random, BlockState surfaceBlock) {
		float density = biome.getFloorDensity();
		floorPositions.forEach((pos) -> {
			BlocksHelper.setWithoutUpdate(world, pos, surfaceBlock);
			if (density > 0 && random.nextFloat() <= density) {
				Feature<?> feature = biome.getFloorFeature(random);
				if (feature != null) {
					feature.generate(world, null, random, pos.up(), null);
				}
			}
		});
	}
	
	protected void placeCeil(StructureWorldAccess world, EndCaveBiome biome, Set<BlockPos> ceilPositions, Random random) {
		float density = biome.getCeilDensity();
		ceilPositions.forEach((pos) -> {
			BlockState ceilBlock = biome.getCeil(pos);
			if (ceilBlock != null) {
				BlocksHelper.setWithoutUpdate(world, pos, ceilBlock);
			}
			if (density > 0 && random.nextFloat() <= density) {
				Feature<?> feature = biome.getCeilFeature(random);
				if (feature != null) {
					feature.generate(world, null, random, pos.down(), null);
				}
			}
		});
	}
}