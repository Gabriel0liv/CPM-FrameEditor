package com.tom.cpm.timeline.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.tom.cpl.gui.elements.Checkbox;
import com.tom.cpl.gui.elements.GuiElement;
import com.tom.cpl.gui.elements.Panel;
import com.tom.cpl.gui.elements.ButtonIcon;
import com.tom.cpl.math.Box;
import com.tom.cpm.shared.editor.Editor;
import com.tom.cpm.shared.editor.gui.AnimPanel;
import com.tom.cpm.shared.editor.gui.popup.ColorButton;
import com.tom.cpm.timeline.CPMAnimatorUtilsCore;

@Mixin(value = AnimPanel.class, remap = false)
public abstract class AnimPanelMixin extends Panel {
    @Shadow private Editor editor;

    public AnimPanelMixin(com.tom.cpl.gui.IGui gui) {
        super(gui);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void cpmTimeline$addTimelineControls(com.tom.cpl.gui.IGui gui, com.tom.cpm.shared.editor.gui.EditorGui e, CallbackInfo ci) {
        for (GuiElement elem : getElements()) {
            if (elem instanceof Panel) {
                Panel p = (Panel) elem;
                boolean isTargetPanel = false;
                for (GuiElement sub : p.getElements()) {
                    if (sub instanceof ButtonIcon) {
                        if (sub.getBounds().x == 55 && sub.getBounds().h == 20) {
                            isTargetPanel = true;
                            break;
                        }
                    }
                }
                
                if (isTargetPanel) {
                    // 1. Checkbox (x=80)
                    Checkbox timelineCb = new Checkbox(gui, "T");
                    timelineCb.setBounds(new Box(80, 0, 30, 20));
                    timelineCb.setSelected(CPMAnimatorUtilsCore.timelineEnabled);
                    timelineCb.setAction(() -> {
                        boolean newState = !timelineCb.isSelected();
                        timelineCb.setSelected(newState);
                        CPMAnimatorUtilsCore.timelineEnabled = newState;
                    });
                    
                    // 2. Color Button (x=115) - Smaller size
                    ColorButton frameColorBtn = new ColorButton(gui, e, color -> {
                        if (editor.selectedAnim != null) {
                            Object selFrame = editor.selectedAnim.getSelectedFrame();
                            if (selFrame != null) {
                                CPMAnimatorUtilsCore.frameColors.put(selFrame, color);
                                // Save to disk
                                String path = editor.file != null ? editor.file.getAbsolutePath() : "untitled";
                                int idx = editor.selectedAnim.getFrames().indexOf(selFrame);
                                com.tom.cpm.timeline.TimelineDataManager.setFrameColor(path, editor.selectedAnim.toString(), idx, color);
                            }
                        }
                    });
                    frameColorBtn.setBounds(new Box(115, 0, 25, 20));
                    
                    // Update controls state
                    editor.updateGui.add(() -> {
                        boolean hasAnim = editor.selectedAnim != null;
                        timelineCb.setEnabled(hasAnim);
                        timelineCb.setSelected(CPMAnimatorUtilsCore.timelineEnabled);
                        
                        frameColorBtn.setEnabled(hasAnim && editor.selectedAnim.getSelectedFrame() != null);
                        if (hasAnim && editor.selectedAnim.getSelectedFrame() != null) {
                            Integer col = CPMAnimatorUtilsCore.frameColors.get(editor.selectedAnim.getSelectedFrame());
                            frameColorBtn.setColor(col != null ? col : 0xFFFFFF);
                        }
                    });
                    
                    p.addElement(timelineCb);
                    p.addElement(frameColorBtn);
                    break;
                }
            }
        }
    }
}
