package mrtjp.projectred.fabrication.gui;

import codechicken.lib.colour.EnumColour;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.vec.Vector3;
import com.mojang.blaze3d.matrix.MatrixStack;
import mrtjp.core.vec.Point;
import mrtjp.core.vec.Rect;
import mrtjp.fengine.TileCoord;
import mrtjp.projectred.fabrication.editor.ICWorkbenchEditor;
import mrtjp.projectred.fabrication.engine.ICCompilerLog;
import mrtjp.projectred.fabrication.gui.screen.ICWorkbenchCompileTab;
import mrtjp.projectred.redui.AbstractGuiNode;
import mrtjp.projectred.redui.ScrollBarNode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.text.ITextProperties;

import java.util.List;

import static net.minecraft.client.gui.AbstractGui.blit;

public class CompileStackTab extends AbstractGuiNode implements ICompileOverlayRenderer {

    private final ICWorkbenchEditor editor;

    private final CTNListNode ctnListNode = new CTNListNode();

    public CompileStackTab(ICWorkbenchEditor editor) {
        this.editor = editor;

        setSize(91, 134);
        initSubNodes();
    }

    private void initSubNodes() {

        // Stack
        ctnListNode.setPosition(6, 31);
        ctnListNode.setSize(67, 95);
        addChild(ctnListNode);

        // Scrollbar //TODO
        ScrollBar scrollBar = new ScrollBar();
        scrollBar.setPosition(77, 31);
        scrollBar.setZPosition(0.2);
        scrollBar.setSize(8, 95);
        scrollBar.setSliderSize(8, 16);
        addChild(scrollBar);
    }

    @Override
    public void drawBack(MatrixStack stack, Point mouse, float partialFrame) {
        TextureUtils.changeTexture(ICWorkbenchCompileTab.TAB_BACKGROUND);
        blit(stack, getFrame().x(), getFrame().y(), 0, 223, getFrame().width(), getFrame().height(), 512, 512);
    }

    @Override
    public void update() {
        if (!isHidden()) {
            List<ICCompilerLog.CompileTreeNode> execStack = editor.getStateMachine().getCompilerLog().getCurrentStack();
            ctnListNode.setNodeList(execStack);
        }
    }

    private void renderCompileTreeNode(ICCompilerLog.CompileTreeNode node, CCRenderState ccrs, IRenderTypeBuffer getter, MatrixStack matrixStack) {
        ccrs.reset();
        ccrs.bind(ICRenderTypes.selectionRenderType, Minecraft.getInstance().renderBuffers().bufferSource(), matrixStack);
        ccrs.baseColour = EnumColour.LIGHT_BLUE.rgba(200);

        for (TileCoord pos : node.tileCoords) {
            Vector3 p = new Vector3(pos.x, pos.y, pos.z);
            ICRenderTypes.renderSelection(ccrs, p, p.copy().add(0.01), 3 / 16D, 2 / 16D);
        }
    }

    //region ICompileTabOverlayRenderer
    @Override
    public void renderOverlay(ICRenderNode renderNode, Vector3 mousePosition, boolean isFirstHit, CCRenderState ccrs, IRenderTypeBuffer getter, MatrixStack matrixStack) {
        for (ICCompilerLog.CompileTreeNode node : editor.getStateMachine().getCompilerLog().getCurrentStack()) {
            renderCompileTreeNode(node, ccrs, getter, matrixStack);
        }
    }

    @Override
    public void buildTooltip(ICRenderNode renderNode, Vector3 mousePosition, List<ITextProperties> tooltip) {

    }
    //endregion

    private class ScrollBar extends ScrollBarNode {

        public ScrollBar() {
            super(ScrollAxis.VERTICAL);
        }

        @Override
        protected void drawSlider(MatrixStack stack, Rect sliderFrame) {
            TextureUtils.changeTexture(ICWorkbenchCompileTab.TAB_BACKGROUND);
            blit(stack, sliderFrame.x(), sliderFrame.y(), 305, 58, sliderFrame.width(), sliderFrame.height(), 512, 512);
        }

        @Override
        protected void adjustContent(double scrollPercentage) {
            //TODO adjust scroll
        }
    }
}
