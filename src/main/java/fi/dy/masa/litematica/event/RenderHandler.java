package fi.dy.masa.litematica.event;

import org.joml.Matrix4f;
import com.mojang.blaze3d.vertex.PoseStack;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.gui.GuiSchematicManager;
import fi.dy.masa.litematica.render.OverlayRenderer;
import fi.dy.masa.litematica.render.infohud.InfoHud;
import fi.dy.masa.litematica.render.infohud.ToolHud;
import fi.dy.masa.litematica.tool.ToolMode;
import fi.dy.masa.malilib.interfaces.IRenderer;
import fi.dy.masa.malilib.util.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class RenderHandler implements IRenderer
{
    @Override
    public void onRenderWorldLast(PoseStack matrices, Matrix4f projMatrix)
    {
        Minecraft mc = Minecraft.getInstance();

        if (Configs.Visuals.ENABLE_RENDERING.getBooleanValue() && mc.player != null)
        {
            OverlayRenderer.getInstance().renderBoxes(matrices);

            if (Configs.InfoOverlays.VERIFIER_OVERLAY_ENABLED.getBooleanValue())
            {
                OverlayRenderer.getInstance().renderSchematicVerifierMismatches(matrices);
            }

            if (DataManager.getToolMode() == ToolMode.REBUILD)
            {
                OverlayRenderer.getInstance().renderSchematicRebuildTargetingOverlay(matrices);
            }
        }
    }

    @Override
    public void onRenderGameOverlayPost(GuiGraphics drawContext)
    {
        Minecraft mc = Minecraft.getInstance();

        if (Configs.Visuals.ENABLE_RENDERING.getBooleanValue() && mc.player != null)
        {
            // The Info HUD renderers can decide if they want to be rendered in GUIs
            InfoHud.getInstance().renderHud(drawContext);

            if (GuiUtils.getCurrentScreen() == null)
            {
                ToolHud.getInstance().renderHud(drawContext);
                OverlayRenderer.getInstance().renderHoverInfo(mc, drawContext);

                if (GuiSchematicManager.hasPendingPreviewTask())
                {
                    OverlayRenderer.getInstance().renderPreviewFrame(mc);
                }
            }
        }
    }
}
