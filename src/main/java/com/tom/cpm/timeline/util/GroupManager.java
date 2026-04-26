package com.tom.cpm.timeline.util;

import com.tom.cpl.math.Vec3f;
import com.tom.cpm.shared.editor.Editor;
import com.tom.cpm.shared.editor.actions.ActionBuilder;
import com.tom.cpm.shared.editor.elements.ElementType;
import com.tom.cpm.shared.editor.elements.ModelElement;
import com.tom.cpm.shared.editor.elements.MultiSelector;
import java.util.ArrayList;
import java.util.List;

public class GroupManager {

    public static void groupSelected(Editor editor) {
        if (editor.selectedElement == null) return;
        
        List<ModelElement> selected = new ArrayList<>();
        if (editor.selectedElement instanceof MultiSelector) {
            ((MultiSelector) editor.selectedElement).forEachSelected(te -> {
                if (te instanceof ModelElement) selected.add((ModelElement) te);
            });
        } else if (editor.selectedElement instanceof ModelElement) {
            selected.add((ModelElement) editor.selectedElement);
        }

        if (selected.isEmpty()) return;

        ActionBuilder ab = editor.action("add", "action.cpm.group");

        // 1. Determine common parent for the group
        ModelElement first = selected.get(0);
        ModelElement commonParent = first.parent;
        List<ModelElement> targetList = (commonParent == null) ? editor.elements : commonParent.children;

        // 2. Create the Group Element
        ModelElement group = new ModelElement(editor, ElementType.NORMAL, null);
        group.name = "Group";
        group.parent = commonParent;
        group.size = new Vec3f(0, 0, 0);
        group.texture = false;
        
        // 3. Simple positioning: use the average of children's positions (local to parent)
        Vec3f avgPos = new Vec3f();
        for (ModelElement e : selected) avgPos = avgPos.add(e.pos);
        avgPos = avgPos.mul(1f / selected.size());
        group.pos = new Vec3f(avgPos);

        // 4. Add group to parent
        ab.addToList(targetList, group);

        // 5. Move children and compensate transforms
        for (ModelElement child : selected) {
            List<ModelElement> sourceList = (child.parent == null) ? editor.elements : child.parent.children;
            
            // To keep visual position stable IF sharing same parent:
            // newPos = oldPos - groupPos
            Vec3f newPos = child.pos.sub(group.pos);

            ab.removeFromList(sourceList, child);
            ab.addToList(group.children, child);
            ab.updateValueOp(child, child.pos, newPos, (me, v) -> me.pos = v);
            
            // Critical: Update parent reference in the action
            ModelElement oldP = child.parent;
            ab.onRun(() -> child.parent = group);
            ab.onUndo(() -> child.parent = oldP);
        }

        ab.onAction(editor::updateGui);
        ab.execute();
        editor.selectedElement = group;
        editor.updateGui();
    }
}
