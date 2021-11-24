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

package me.desht.pneumaticcraft.client.render.pneumatic_armor.block_tracker;

import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IBlockTrackEntry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

import static me.desht.pneumaticcraft.api.PneumaticRegistry.RL;
import static me.desht.pneumaticcraft.common.util.PneumaticCraftUtils.xlate;

public class BlockTrackEntryEndPortalFrame implements IBlockTrackEntry {
    private static final ResourceLocation ID = RL("block_tracker.module.end_portal");

    @Override
    public boolean shouldTrackWithThisEntry(IBlockReader world, BlockPos pos, BlockState state, TileEntity te) {
        return state.getBlock() == Blocks.END_PORTAL_FRAME;
    }

    @Override
    public List<BlockPos> getServerUpdatePositions(TileEntity te) {
        return Collections.emptyList();
    }

    @Override
    public int spamThreshold() {
        return 10;
    }

    @Override
    public void addInformation(World world, BlockPos pos, TileEntity te, Direction face, List<ITextComponent> infoList) {
        if (world.getBlockState(pos).getValue(EndPortalFrameBlock.HAS_EYE)) {
            infoList.add(xlate("pneumaticcraft.blockTracker.info.endportal.eye"));
        } else {
            infoList.add(xlate("pneumaticcraft.blockTracker.info.endportal.noEye"));
        }
    }

    @Override
    public ResourceLocation getEntryID() {
        return ID;
    }
}
