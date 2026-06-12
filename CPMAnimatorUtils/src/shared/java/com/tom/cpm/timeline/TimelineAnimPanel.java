package com.tom.cpm.timeline;

import java.lang.reflect.Field;

import com.tom.cpl.gui.IGui;
import com.tom.cpl.gui.MouseEvent;
import com.tom.cpl.gui.elements.Panel;
import com.tom.cpl.math.Box;

public class TimelineAnimPanel extends Panel {
    private final Object editor;
    private TimelinePanel timelinePanel;
    private Object lastAnim;
    private int lastFrameCount = -1;

    private static Field selectedAnimField;
    private static Field viewTypeField;

    static {
        try {
            Class<?> editorGuiClass = Class.forName("com.tom.cpm.shared.editor.gui.EditorGui");
            selectedAnimField = Class.forName("com.tom.cpm.shared.editor.Editor").getField("selectedAnim");
            viewTypeField = editorGuiClass.getDeclaredField("viewType");
            viewTypeField.setAccessible(true);
        } catch (Exception e) {
            System.err.println("[CPM Timeline] Failed to init TimelineAnimPanel reflection: " + e.getMessage());
        }
    }

    private final Object editorGui;

    public TimelineAnimPanel(IGui gui, Object editorGui) {
        super(gui);
        this.editorGui = editorGui;
        Object ed = null;
        try {
            Field editorField = editorGui.getClass().getDeclaredField("editor");
            editorField.setAccessible(true);
            ed = editorField.get(editorGui);
        } catch (Exception e) {
            System.err.println("[CPM Timeline] Failed to get Editor from EditorGui: " + e.getMessage());
        }
        this.editor = ed;
    }

    @Override
    public void draw(MouseEvent event, float partialTicks) {
        try {
            if (!com.tom.cpm.timeline.CPMTimelineAddon.timelineEnabled || !isAnimationTabActive()) return;

            Box b = getBounds();
            if (b == null || b.w <= 0 || b.h <= 0) return;

            // Lazy init after bounds are set
            if (timelinePanel == null) {
                timelinePanel = new TimelinePanel(gui, editor);
                addElement(timelinePanel);
                timelinePanel.rebuildTimeline();
            }

            // Sync bounds
            if (timelinePanel != null) {
                timelinePanel.setBounds(new Box(0, 0, b.w, b.h));
            }

            // Refresh when selected animation or frame count changes
            if (timelinePanel != null && selectedAnimField != null) {
                Object currentAnim = selectedAnimField.get(editor);
                int currentFrameCount = getFrameCount(currentAnim);
                
                if (currentAnim != lastAnim || currentFrameCount != lastFrameCount) {
                    lastAnim = currentAnim;
                    lastFrameCount = currentFrameCount;
                    timelinePanel.rebuildTimeline();
                }
            }

            super.draw(event, partialTicks);
        } catch (Exception e) {
            // Silently ignore to prevent GUI crash
        }
    }

    private int getFrameCount(Object anim) {
        if (anim == null) return 0;
        try {
            java.lang.reflect.Method getFrames = anim.getClass().getMethod("getFrames");
            java.util.List<?> frames = (java.util.List<?>) getFrames.invoke(anim);
            return frames != null ? frames.size() : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void mouseClick(MouseEvent event) {
        try {
            if (com.tom.cpm.timeline.CPMTimelineAddon.timelineEnabled && isAnimationTabActive() && timelinePanel != null) {
                super.mouseClick(event);
            }
        } catch (Exception ignored) {}
    }

    @Override
    public void mouseRelease(MouseEvent event) {
        try {
            if (com.tom.cpm.timeline.CPMTimelineAddon.timelineEnabled && isAnimationTabActive() && timelinePanel != null) {
                super.mouseRelease(event);
            }
        } catch (Exception ignored) {}
    }

    @Override
    public void mouseDrag(MouseEvent event) {
        try {
            if (com.tom.cpm.timeline.CPMTimelineAddon.timelineEnabled && isAnimationTabActive() && timelinePanel != null) {
                super.mouseDrag(event);
            }
        } catch (Exception ignored) {}
    }

    @Override
    public void mouseWheel(MouseEvent event) {
        try {
            if (isAnimationTabActive() && timelinePanel != null) {
                super.mouseWheel(event);
            }
        } catch (Exception ignored) {}
    }

    private boolean isAnimationTabActive() {
        try {
            if (selectedAnimField != null) {
                Object anim = selectedAnimField.get(editor);
                if (anim != null) return true;
            }

            Field tabsField = editorGui.getClass().getDeclaredField("tabs");
            tabsField.setAccessible(true);
            Object tabsManager = tabsField.get(editorGui);
            if (tabsManager != null) {
                Field activeTabField = tabsManager.getClass().getDeclaredField("activeTab");
                activeTabField.setAccessible(true);
                Object activeTab = activeTabField.get(tabsManager);
                if (activeTab != null) {
                    String tabInfo = activeTab.toString();
                    if (tabInfo.toLowerCase().contains("anim") || tabInfo.toLowerCase().contains("tab: 2")) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
