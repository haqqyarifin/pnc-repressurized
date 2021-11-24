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
import me.desht.pneumaticcraft.client.model.entity.semiblocks.ModelHeatFrame;
import me.desht.pneumaticcraft.client.util.TintColor;
import me.desht.pneumaticcraft.common.entity.semiblock.EntityHeatFrame;
import me.desht.pneumaticcraft.common.heat.HeatUtil;
import me.desht.pneumaticcraft.lib.Textures;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderHeatFrame extends RenderSemiblockBase<EntityHeatFrame> {
    public static final IRenderFactory<EntityHeatFrame> FACTORY = RenderHeatFrame::new;

    private final ModelHeatFrame model = new ModelHeatFrame();

    private RenderHeatFrame(EntityRendererManager rendererManager) {
        super(rendererManager);
    }

    @Override
    public void render(EntityHeatFrame entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        TintColor tint = HeatUtil.getColourForTemperature(entityIn.getSyncedTemperature());
        float[] f = tint.getComponents(null);
        AxisAlignedBB aabb = entityIn.getBoundingBox();

        matrixStackIn.pushPose();
        matrixStackIn.scale((float) aabb.getXsize(), (float) aabb.getYsize(), (float) aabb.getZsize());
        matrixStackIn.translate(0, -0.5, 0);
        if (entityIn.getTimeSinceHit() > 0) {
            wobble(entityIn, partialTicks, matrixStackIn);
        }

        IVertexBuilder builder = bufferIn.getBuffer(RenderType.entityCutout(getTextureLocation(entityIn)));
        model.renderToBuffer(matrixStackIn, builder, kludgeLightingLevel(entityIn, packedLightIn), OverlayTexture.pack(0F, false), f[0], f[1], f[2], f[3]);

        matrixStackIn.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityHeatFrame entityHeatFrame) {
        return Textures.MODEL_HEAT_FRAME;
    }
}
