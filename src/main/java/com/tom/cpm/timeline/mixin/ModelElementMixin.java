package com.tom.cpm.timeline.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.tom.cpl.gui.elements.PopupMenu;
import com.tom.cpm.shared.editor.Editor;
import com.tom.cpm.shared.editor.elements.ModelElement;
import com.tom.cpm.timeline.util.ElementUtils;

@Mixin(value = ModelElement.class, remap = false)
public abstract class ModelElementMixin {

    @Shadow public Editor editor;

    @Inject(method = "populatePopup", at = @At("RETURN"))
    private void cpmTimeline$onPopulatePopup(PopupMenu popup, CallbackInfo ci) {
        ModelElement thisElem = (ModelElement) (Object) this;
        if (thisElem.size != null) {
            popup.addMenuButton("Align Pivot", () -> {
                PopupMenu sub = new PopupMenu(popup.getGui(), popup.getGui().getFrame());
                sub.addButton("Center", () -> ElementUtils.alignPivot(thisElem, 0.5f, 0.5f, 0.5f));
                sub.addButton("Top",    () -> ElementUtils.alignPivot(thisElem, 0.5f, 0.0f, 0.5f));
                sub.addButton("Bottom", () -> ElementUtils.alignPivot(thisElem, 0.5f, 1.0f, 0.5f));
                sub.addButton("Left",   () -> ElementUtils.alignPivot(thisElem, 0.0f, 0.5f, 0.5f));
                sub.addButton("Right",  () -> ElementUtils.alignPivot(thisElem, 1.0f, 0.5f, 0.5f));
                sub.addButton("Front",  () -> ElementUtils.alignPivot(thisElem, 0.5f, 0.5f, 1.0f));
                sub.addButton("Back",   () -> ElementUtils.alignPivot(thisElem, 0.5f, 0.5f, 0.0f));
                return sub;
            });
        }
    }
}
