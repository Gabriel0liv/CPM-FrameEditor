package com.tom.cpm.timeline.util;

import java.util.ArrayList;
import java.util.List;

import com.tom.cpl.math.Vec3f;
import com.tom.cpm.shared.editor.Editor;
import com.tom.cpm.shared.editor.actions.ActionBuilder;
import com.tom.cpm.shared.editor.elements.ModelElement;
import com.tom.cpm.shared.editor.elements.MultiSelector;

public class ElementUtils {
    public static Object selectionAnchor;

    public static void handleShiftSelection(Editor editor, ModelElement current) {
        if (selectionAnchor != null && selectionAnchor != current) {
            List<ModelElement> allElements = new ArrayList<>();
            flatten(editor.elements, allElements);
            
            int start = allElements.indexOf(selectionAnchor);
            int end = allElements.indexOf(current);
            
            if (start != -1 && end != -1) {
                int min = Math.min(start, end);
                int max = Math.max(start, end);
                
                MultiSelector.ElementImpl ms = new MultiSelector.ElementImpl(editor);
                for (int i = min; i <= max; i++) {
                    ms.add(allElements.get(i));
                }
                editor.selectedElement = ms;
                editor.updateGui();
            }
        } else {
            editor.selectedElement = current;
            editor.updateGui();
        }
    }

    private static void flatten(List<ModelElement> elements, List<ModelElement> flat) {
        for (ModelElement e : elements) {
            flat.add(e);
            flatten(e.children, flat);
        }
    }

    public static void alignPivot(ModelElement el, float fx, float fy, float fz) {
        if (el == null || el.size == null) return;
        
        ActionBuilder action = el.editor.action("action.cpm.timeline.alignPivot");
        
        Vec3f size = el.size;
        Vec3f oldPos = el.pos;
        Vec3f oldOffset = el.offset;
        
        // Calculate new offset based on factors (0.0 = min, 0.5 = center, 1.0 = max)
        Vec3f newOffset = new Vec3f(
            -size.x * fx,
            -size.y * fy,
            -size.z * fz
        );
        
        // Calculate new position to stay in place: oldPos + oldOffset - newOffset
        Vec3f newPos = new Vec3f(
            oldPos.x + oldOffset.x - newOffset.x,
            oldPos.y + oldOffset.y - newOffset.y,
            oldPos.z + oldOffset.z - newOffset.z
        );
        
        // Apply via actions for undo support
        action.updateValueOp(el, oldPos, newPos, (e, v) -> e.pos = v);
        action.updateValueOp(el, oldOffset, newOffset, (e, v) -> e.offset = v);
        
        action.execute();
        el.markDirty();
        el.editor.updateGui();
    }

    @Deprecated
    public static void centerPivot(ModelElement el) {
        alignPivot(el, 0.5f, 0.5f, 0.5f);
    }
}
