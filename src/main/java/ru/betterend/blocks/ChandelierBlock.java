package ru.betterend.blocks;

import java.util.EnumMap;

import com.google.common.collect.Maps;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import ru.betterend.blocks.basis.AttachedBlock;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;

public class ChandelierBlock extends AttachedBlock implements IRenderTypeable {
	private static final EnumMap<Direction, VoxelShape> BOUNDING_SHAPES = Maps.newEnumMap(Direction.class);
	
	public ChandelierBlock(FabricBlockSettings settings) {
		super(settings.noCollision().nonOpaque().requiresTool().luminance(15));
	}
	
	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return BOUNDING_SHAPES.get(state.get(FACING));
	}
	
	static {
		BOUNDING_SHAPES.put(Direction.UP, Block.createCuboidShape(5, 0, 5, 11, 13, 11));
		BOUNDING_SHAPES.put(Direction.DOWN, Block.createCuboidShape(5, 3, 5, 11, 16, 11));
		BOUNDING_SHAPES.put(Direction.NORTH, VoxelShapes.cuboid(0.0, 0.0, 0.5, 1.0, 1.0, 1.0));
		BOUNDING_SHAPES.put(Direction.SOUTH, VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 1.0, 0.5));
		BOUNDING_SHAPES.put(Direction.WEST, VoxelShapes.cuboid(0.5, 0.0, 0.0, 1.0, 1.0, 1.0));
		BOUNDING_SHAPES.put(Direction.EAST, VoxelShapes.cuboid(0.0, 0.0, 0.0, 0.5, 1.0, 1.0));
	}
}