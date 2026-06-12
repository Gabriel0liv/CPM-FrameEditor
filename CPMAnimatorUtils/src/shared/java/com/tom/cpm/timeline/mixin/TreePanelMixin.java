package com.tom.cpm.timeline.mixin;

import com.tom.cpl.gui.IGui;
import com.tom.cpl.gui.elements.Button;
import com.tom.cpl.math.Box;
import com.tom.cpm.shared.editor.Editor;
import com.tom.cpm.shared.editor.gui.EditorGui;
import com.tom.cpm.shared.editor.gui.TreePanel;
import com.tom.cpm.timeline.util.GroupManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TreePanel.class, remap = false)
public abstract class TreePanelMixin extends com.tom.cpl.gui.elements.Panel {
    @Shadow private Editor editor;

    public TreePanelMixin(IGui gui) {
        super(gui);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void cpmTimeline$onInit(IGui gui, EditorGui e, int width, int height, boolean enableMod, CallbackInfo ci) {
        if (enableMod) {
            Button groupBtn = new Button(gui, "📁", () -> GroupManager.groupSelected(editor));
            // Positioned after the visibility button (which is at 55 prefix if enableMod is true)
            // VisBtn (55, height-25, 20, 20) -> Next at 80
            groupBtn.setBounds(new Box(80, height - 25, 20, 20));
            groupBtn.setTooltip(new com.tom.cpl.gui.elements.Tooltip(e, "Group Selected (Folder)"));
            addElement(groupBtn);
            
            // Enable button only when multiple elements or at least one is selected
            editor.setDelEn.add(groupBtn::setEnabled);
        }
    }
}
