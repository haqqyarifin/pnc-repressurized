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

package me.desht.pneumaticcraft.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.desht.pneumaticcraft.client.model.entity.ModelTransferGadget;
import me.desht.pneumaticcraft.common.entity.semiblock.EntityTransferGadget;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderTransferGadget extends RenderSemiblockBase<EntityTransferGadget> {
    public static final IRenderFactory<EntityTransferGadget> FACTORY = RenderTransferGadget::new;

    private final ModelTransferGadget model = new ModelTransferGadget();

    private RenderTransferGadget(EntityRendererManager rendererManager) {
        super(rendererManager);
    }

    @Override
    public void render(EntityTransferGadget entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        if (entity.isAir()) {
            return;
        }
        
        matrixStackIn.pushPose();

        if (entity.getTimeSinceHit() > 0) {
            wobble(entity, partialTicks, matrixStackIn);
        }

        Direction side = entity.getSide();
        matrixStackIn.translate(0, side.getAxis() == Axis.Y ? 1.5 : -1.5, 0);
        switch (side) {
            case UP:
                matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(90));
                matrixStackIn.translate(-1.5, -1.5, 0);
                break;
            case DOWN:
                matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(-90));
                matrixStackIn.translate(1.5, -1.5, 0);
                break;
            case NORTH:
                matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90));
                break;
            case SOUTH:
                matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-90));
                break;
            case WEST:
                matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180));
                break;
        }

        IVertexBuilder builder = bufferIn.getBuffer(RenderType.entitySolid(getTextureLocation(entity)));
        model.renderToBuffer(matrixStackIn, builder, kludgeLightingLevel(entity, packedLightIn), OverlayTexture.pack(0F, false), 1f, 1f, 1f, 1f);

        matrixStackIn.popPose();
    }

    @Override
    public Vector3d getRenderOffset(EntityTransferGadget entityIn, float partialTicks) {
        VoxelShape shape = entityIn.getBlockState().getShape(entityIn.getWorld(), entityIn.getBlockPos());
        double yOff = (shape.max(Axis.Y) - shape.min(Axis.Y)) / 2.0;
        switch (entityIn.getSide()) {
            case DOWN: return new Vector3d(0, shape.min(Axis.Y), 0);
            case UP: return new Vector3d(0, shape.max(Axis.Y), 0);
            case NORTH: return new Vector3d(0, yOff, shape.min(Axis.Z) - 0.5);
            case SOUTH: return new Vector3d(0, yOff, shape.max(Axis.Z) - 0.5);
            case WEST: return new Vector3d(shape.min(Axis.X) - 0.5, yOff, 0);
            case EAST: return new Vector3d(shape.max(Axis.X) - 0.5, yOff, 0);
            default: return Vector3d.ZERO;
        }
    }

    @Override
    public ResourceLocation getTextureLocation(EntityTransferGadget entityTransferGadget) {
        return entityTransferGadget.getIOMode().getTexture();
    }
}
