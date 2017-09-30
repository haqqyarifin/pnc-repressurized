package me.desht.pneumaticcraft.common.block.tubes;

import me.desht.pneumaticcraft.client.model.module.ModelFlowDetector;
import me.desht.pneumaticcraft.common.util.PneumaticCraftUtils;
import me.desht.pneumaticcraft.lib.Names;
import me.desht.pneumaticcraft.lib.Textures;
import me.desht.pneumaticcraft.proxy.CommonProxy.EnumGuiId;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ModuleFlowDetector extends TubeModuleRedstoneEmitting implements IInfluenceDispersing {
    public float rotation, oldRotation;
    private int flow;
    private int oldFlow;
    @SideOnly(Side.CLIENT)
    private final ModelFlowDetector model = new ModelFlowDetector(this);

    @Override
    public void update() {
        super.update();
        oldRotation = rotation;
        rotation += getRedstoneLevel() / 100F;

        if (!pressureTube.world().isRemote) {
            if (setRedstone(flow / 5)) {
                sendDescriptionPacket();
            }
            oldFlow = flow;
            flow = 0;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(float partialTicks) {
        model.renderModel(0.0625f, dir, partialTicks);
    }

    @Override
    public String getType() {
        return Names.MODULE_FLOW_DETECTOR;
    }

    @Override
    public String getModelName() {
        return "flow_detector";
    }

    @Override
    public int getMaxDispersion() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void onAirDispersion(int amount) {
        flow += amount;
    }

    @Override
    public void addInfo(List<String> curInfo) {
        curInfo.add("Flow: " + TextFormatting.WHITE + oldFlow + " mL/tick");
        super.addInfo(curInfo);
    }

    @Override
    public boolean isInline() {
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        rotation = tag.getFloat("rotation");
        oldFlow = tag.getInteger("flow");//taggin it for waila purposes.
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setFloat("rotation", rotation);
        tag.setInteger("flow", oldFlow);
    }

    @Override
    public void addItemDescription(List<String> curInfo) {
        curInfo.add(TextFormatting.BLUE + "Formula: Redstone = 0.2 x flow(mL/tick)");
        curInfo.add("This module emits a redstone signal of which");
        curInfo.add("the strength is dependent on how much air");
        curInfo.add("is travelling through the tube.");
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    protected EnumGuiId getGuiId() {
        return null;
    }
}
