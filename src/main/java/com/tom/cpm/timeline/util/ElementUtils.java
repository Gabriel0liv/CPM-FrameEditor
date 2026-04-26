package com.tom.cpm.timeline.util;

import com.tom.cpl.math.Vec3f;
import com.tom.cpm.shared.editor.actions.ActionBuilder;
import com.tom.cpm.shared.editor.elements.ModelElement;

public class ElementUtils {

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
