package keystrokesmod.module.impl.player;

import keystrokesmod.minecraft.Vec3;
import keystrokesmod.module.Module;
import keystrokesmod.module.ModuleManager;
import keystrokesmod.module.impl.player.blink.FakeLagBlink;
import keystrokesmod.module.impl.player.blink.NormalBlink;
import keystrokesmod.module.setting.impl.ModeValue;
import keystrokesmod.utility.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.AxisAlignedBB;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Blink extends Module {
    public static final int color = new Color(255, 255, 255, 200).getRGB();
    private final ModeValue mode;

    public Blink() {
        super("Blink", category.player);
        this.registerSetting(mode = new ModeValue("Mode", this)
                .add(new NormalBlink("Normal", this))
                .add(new FakeLagBlink("FakeLag", this))
        );
    }

    public static boolean isBlinking() {
        if (ModuleManager.blink == null) return false;
        if (ModuleManager.blink.isEnabled()) return true;
        if (ModuleManager.blink.mode.getSelected() instanceof FakeLagBlink) {
            if (ModuleManager.blink.mode.getSelected().isEnabled())
                return true;
            return ((FakeLagBlink) ModuleManager.blink.mode.getSelected()).needToDisable;
        }
        return false;
    }

    public static void drawBox(@NotNull Vec3 pos) {
        GlStateManager.pushMatrix();
        double x = pos.xCoord - mc.getRenderManager().viewerPosX;
        double y = pos.yCoord - mc.getRenderManager().viewerPosY;
        double z = pos.zCoord - mc.getRenderManager().viewerPosZ;
        AxisAlignedBB bbox = mc.player.getEntityBoundingBox().expand(0.1D, 0.1, 0.1);
        AxisAlignedBB axis = new AxisAlignedBB(bbox.minX - mc.player.posX + x, bbox.minY - mc.player.posY + y, bbox.minZ - mc.player.posZ + z, bbox.maxX - mc.player.posX + x, bbox.maxY - mc.player.posY + y, bbox.maxZ - mc.player.posZ + z);
        float a = (float) (color >> 24 & 255) / 255.0F;
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(2.0F);
        GL11.glColor4f(r, g, b, a);
        RenderUtils.drawBoundingBox(axis, r, g, b);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GlStateManager.popMatrix();
    }

    @Override
    public void onEnable() throws Throwable {
        mode.enable();
    }

    @Override
    public void onDisable() throws Throwable {
        mode.disable();
    }

    @Override
    public String getInfo() {
        return mode.getSelected().getInfo();
    }
}

