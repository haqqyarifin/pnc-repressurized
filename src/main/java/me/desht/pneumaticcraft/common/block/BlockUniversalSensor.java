/*
 * This file is part of pnc-repressurized.
 *
 *     pnc-repressurized is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     pnc-repressurized is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with pnc-repressurized.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.desht.pneumaticcraft.common.block;

import me.desht.pneumaticcraft.common.core.ModBlocks;
import me.desht.pneumaticcraft.common.tileentity.TileEntityUniversalSensor;
import me.desht.pneumaticcraft.common.util.PneumaticCraftUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class BlockUniversalSensor extends BlockPneumaticCraft {
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 4, 16);

    public BlockUniversalSensor() {
        super(ModBlocks.defaultProps());

        registerDefaultState(getStateDefinition().any()
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(EAST, false)
        );
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);

        builder.add(BlockPneumaticCraft.NORTH, BlockPneumaticCraft.SOUTH, BlockPneumaticCraft.WEST, BlockPneumaticCraft.EAST);
    }

    @Override
    protected Class<? extends TileEntity> getTileEntityClass() {
        return TileEntityUniversalSensor.class;
    }

    @Override
    public int getDirectSignal(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return PneumaticCraftUtils.getTileEntityAt(blockAccess, pos, TileEntityUniversalSensor.class)
                .map(te -> side == Direction.UP ? te.redstoneStrength : 0)
                .orElse(0);
    }

    @Override
    public int getSignal(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return PneumaticCraftUtils.getTileEntityAt(blockAccess, pos, TileEntityUniversalSensor.class)
                .map(te -> te.redstoneStrength).orElse(0);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }
}
