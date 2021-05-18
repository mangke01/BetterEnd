package ru.betterend.blocks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import ru.betterend.blocks.basis.EndAnvilBlock;
import ru.betterend.item.material.EndToolMaterial;
import ru.betterend.client.models.Patterns;
import ru.betterend.registry.EndBlocks;

public class AeterniumAnvil extends EndAnvilBlock {
	private static final IntegerProperty DESTRUCTION_LONG = BlockProperties.DESTRUCTION_LONG;
	
	public AeterniumAnvil() {
		super(EndBlocks.AETERNIUM_BLOCK.defaultMaterialColor(), EndToolMaterial.AETERNIUM.getLevel());
	}
	
	@Override
	public IntegerProperty getDestructionProperty() {
		return DESTRUCTION_LONG;
	}

	@Override
	public ResourceLocation statePatternId() {
		return Patterns.STATE_ANVIL_LONG;
	}
}
