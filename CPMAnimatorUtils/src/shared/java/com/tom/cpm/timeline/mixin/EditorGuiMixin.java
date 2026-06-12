package com.tom.cpm.timeline.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.tom.cpl.math.Box;
import com.tom.cpm.shared.editor.gui.EditorGui;
import com.tom.cpm.timeline.CPMAnimatorUtilsCore;
import com.tom.cpm.timeline.TimelineAnimPanel;

@Mixin(value = com.tom.cpm.shared.editor.gui.EditorGui.class, remap = false)
public abstract class EditorGuiMixin {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void cpmTimeline$onInit(com.tom.cpl.gui.IGui gui, CallbackInfo ci) {
        CPMAnimatorUtilsCore.LOGGER.info("[CPM Timeline] EditorGui instance created! Mixin is active.");
    }

    @Inject(method = "initFrame", at = @At("RETURN"))
    private void cpmTimeline$injectTimelinePanel(int width, int height, CallbackInfo ci) {
        try {
            EditorGui self = (EditorGui) (Object) this;
            com.tom.cpl.gui.IGui gui = self.getGui();
            
            TimelineAnimPanel timelinePanel = self.getElements().stream()
                    .filter(e -> e instanceof TimelineAnimPanel)
                    .map(e -> (TimelineAnimPanel) e)
                    .findFirst().orElse(null);

            int timelineX = 170;
            int timelineW = width - 170 - 150;
            int timelineH = 75;
            int timelineY = height - 20 - timelineH;
            Box bounds = new Box(timelineX, timelineY, timelineW, timelineH);

            if (timelinePanel == null) {
                timelinePanel = new TimelineAnimPanel(gui, self);
                timelinePanel.setBounds(bounds);
                self.addElement(timelinePanel);
                CPMAnimatorUtilsCore.LOGGER.info("[CPM Timeline] Timeline panel successfully injected into Animation Editor (Size: {}x{})", timelineW, timelineH);
            } else {
                timelinePanel.setBounds(bounds);
            }
        } catch (Exception e) {
            CPMAnimatorUtilsCore.LOGGER.error("[CPM Timeline] Failed to inject timeline panel", e);
        }
    }
}
