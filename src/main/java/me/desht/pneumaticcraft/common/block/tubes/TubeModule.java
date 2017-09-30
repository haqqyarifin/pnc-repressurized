package me.desht.pneumaticcraft.common.block.tubes;

import me.desht.pneumaticcraft.common.block.Blockss;
import me.desht.pneumaticcraft.common.item.Itemss;
import me.desht.pneumaticcraft.common.network.NetworkHandler;
import me.desht.pneumaticcraft.common.network.PacketOpenTubeModuleGui;
import me.desht.pneumaticcraft.common.thirdparty.ModInteractionUtils;
import me.desht.pneumaticcraft.common.util.PneumaticCraftUtils;
import me.desht.pneumaticcraft.lib.BBConstants;
import me.desht.pneumaticcraft.proxy.CommonProxy.EnumGuiId;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opencl.CL;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public abstract class TubeModule implements ISidedPart {
    protected IPneumaticPosProvider pressureTube;
    protected EnumFacing dir = EnumFacing.UP;
    public AxisAlignedBB[] boundingBoxes = new AxisAlignedBB[6];
    protected boolean upgraded;
    public float lowerBound = 7.5F, higherBound = 0, maxValue = 30;
    private boolean fake;
    public boolean advancedConfig;
    public boolean shouldDrop;

    public TubeModule() {
        double width = getWidth() / 2;
        double height = getHeight();

        // 0..6 = D,U,N,S,W,E
        boundingBoxes[0] = new AxisAlignedBB(0.5 - width, BBConstants.PRESSURE_PIPE_MIN_POS - height, 0.5 - width, 0.5 + width, BBConstants.PRESSURE_PIPE_MIN_POS, 0.5 + width);
        boundingBoxes[1] = new AxisAlignedBB(0.5 - width, BBConstants.PRESSURE_PIPE_MAX_POS, 0.5 - width, 0.5 + width, BBConstants.PRESSURE_PIPE_MAX_POS + height, 0.5 + width);
        boundingBoxes[2] = new AxisAlignedBB(0.5 - width, 0.5 - width, BBConstants.PRESSURE_PIPE_MIN_POS - height, 0.5 + width, 0.5 + width, BBConstants.PRESSURE_PIPE_MIN_POS);
        boundingBoxes[3] = new AxisAlignedBB(0.5 - width, 0.5 - width, BBConstants.PRESSURE_PIPE_MAX_POS, 0.5 + width, 0.5 + width, BBConstants.PRESSURE_PIPE_MAX_POS + height);
        boundingBoxes[4] = new AxisAlignedBB(BBConstants.PRESSURE_PIPE_MIN_POS - height, 0.5 - width, 0.5 - width, BBConstants.PRESSURE_PIPE_MIN_POS, 0.5 + width, 0.5 + width);
        boundingBoxes[5] = new AxisAlignedBB(BBConstants.PRESSURE_PIPE_MAX_POS, 0.5 - width, 0.5 - width, BBConstants.PRESSURE_PIPE_MAX_POS + height, 0.5 + width, 0.5 + width);
    }

    public void markFake() {
        fake = true;
    }

    public boolean isFake() {
        return fake;
    }

    public void setTube(IPneumaticPosProvider pressureTube) {
        this.pressureTube = pressureTube;
    }

    public IPneumaticPosProvider getTube() {
        return pressureTube;
    }

    public double getWidth() {
        return BBConstants.PRESSURE_PIPE_MAX_POS - BBConstants.PRESSURE_PIPE_MIN_POS;
    }

    protected double getHeight() {
        return BBConstants.PRESSURE_PIPE_MIN_POS;
    }

    public float getThreshold(int redstone) {
        double slope = (higherBound - lowerBound) / 15;
        double threshold = lowerBound + slope * redstone;
        return (float) threshold;
    }

    /**
     * Returns the item that this part drops.
     *
     * @return
     */
    public List<ItemStack> getDrops() {
        List<ItemStack> drops = new ArrayList<ItemStack>();
        if (shouldDrop) {
            drops.add(new ItemStack(ModuleRegistrator.getModuleItem(getType())));
            if (upgraded) drops.add(new ItemStack(Itemss.ADVANCED_PCB));
        }
        return drops;
    }

    @Override
    public void setDirection(EnumFacing dir) {
        this.dir = dir;
    }

    public EnumFacing getDirection() {
        return dir;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        dir = EnumFacing.getFront(nbt.getInteger("dir"));
        upgraded = nbt.getBoolean("upgraded");
        lowerBound = nbt.getFloat("lowerBound");
        higherBound = nbt.getFloat("higherBound");
        advancedConfig = !nbt.hasKey("advancedConfig") || nbt.getBoolean("advancedConfig");
    }

    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("dir", dir.ordinal());
        nbt.setBoolean("upgraded", upgraded);
        nbt.setFloat("lowerBound", lowerBound);
        nbt.setFloat("higherBound", higherBound);
        nbt.setBoolean("advancedConfig", advancedConfig);
    }

    /* TODO FMP dep @Optional.Method(modid = ModIds.FMP)
     public void writeDesc(MCDataOutput data){
         data.writeInt(dir.ordinal());
     }

     @Optional.Method(modid = ModIds.FMP)
     public void readDesc(MCDataInput data){
         dir = EnumFacing.getFront(data.readInt());
     }*/

    public void update() {
    }

    public void onNeighborTileUpdate() {
    }

    public void onNeighborBlockUpdate() {
    }

    /**
     * Used by multiparts and/or by NBT saving.
     *
     * @return
     */
    public abstract String getType();

    @SideOnly(Side.CLIENT)
    public abstract void render(float partialTicks);

    public abstract String getModelName();

    public int getRedstoneLevel() {
        return 0;
    }

    protected void updateNeighbors() {
        pressureTube.world().notifyNeighborsOfStateChange(pressureTube.pos(), pressureTube.world().getBlockState(pressureTube.pos()).getBlock(), true);
    }

    public boolean isInline() {
        return false;
    }

    public void sendDescriptionPacket() {
        ModInteractionUtils.getInstance().sendDescriptionPacket(pressureTube);
    }

    public void addInfo(List<String> curInfo) {
    }

    public void addItemDescription(List<String> curInfo) {
    }

    public boolean canUpgrade() {
        return true;
    }

    public void upgrade() {
        upgraded = true;
    }

    public boolean isUpgraded() {
        return upgraded;
    }

    public boolean onActivated(EntityPlayer player) {
        if (!player.world.isRemote && upgraded && getGuiId() != null) {
            NetworkHandler.sendTo(new PacketOpenTubeModuleGui(getGuiId().ordinal(), pressureTube.pos()), (EntityPlayerMP) player);
            return true;
        }
        return false;
    }

    protected abstract EnumGuiId getGuiId();

}
