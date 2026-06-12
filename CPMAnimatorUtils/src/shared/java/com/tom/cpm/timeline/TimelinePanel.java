package com.tom.cpm.timeline;

import com.tom.cpl.gui.IGui;
import com.tom.cpl.gui.MouseEvent;
import com.tom.cpl.gui.elements.Label;
import com.tom.cpl.gui.elements.Panel;
import com.tom.cpl.math.Box;
import com.tom.cpm.timeline.util.GuiLayoutUtils.IReflowable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TimelinePanel extends Panel implements IReflowable {
    private final Object editor;
    private Object currentAnim;
    private int selectedFrameIndex = -1;
    private boolean isDragging = false;
    private boolean isScrubbing = false;
    private int dragStartIndex = -1;
    private int hoveredFrame = -1;
    private float scrubProgress = 0;

    private static final int ACCENT_COLOR = 0xFF00FF00;
    private static final int BG_COLOR = 0xAA111111;

    private static Field selectedAnimField;
    private static Method getFramesMethod;
    private static Field viewTypeField;

    static {
        try {
            Class<?> editorClass = Class.forName("com.tom.cpm.shared.editor.Editor");
            selectedAnimField = editorClass.getField("selectedAnim");
            getFramesMethod = Class.forName("com.tom.cpm.shared.editor.anim.EditorAnim").getMethod("getFrames");
            
            Class<?> guiClass = Class.forName("com.tom.cpm.shared.editor.gui.EditorGui");
            viewTypeField = guiClass.getDeclaredField("viewType");
            viewTypeField.setAccessible(true);
        } catch (Exception e) {
            System.err.println("[CPM Timeline] Failed to init reflection: " + e.getMessage());
        }
    }

    public TimelinePanel(IGui gui, Object editor) {
        super(gui);
        this.editor = editor;
        setBackgroundColor(BG_COLOR);
    }

    public void init() {
        rebuildTimeline();
    }

    private boolean isAnimationTab() {
        try {
            if (viewTypeField == null) return true;
            Object viewType = viewTypeField.get(gui.getFrame());
            return viewType != null && viewType.toString().equals("ANIMATION");
        } catch (Exception e) {
            return true;
        }
    }

    public void rebuildTimeline() {
        elements.clear();
        Box b = getBounds();
        if (b == null) return;
        Label header = new Label(gui, "§7Timeline | Frames: §f" + (currentAnim != null ? getFrameCount(currentAnim) : 0));
        header.setBounds(new Box(10, 2, b.w - 20, 10));
        addElement(header);
        Panel timelineArea = new Panel(gui);
        timelineArea.setBounds(new Box(5, 14, b.w - 10, 30));
        timelineArea.setBackgroundColor(0x33000000);
        addElement(timelineArea);
        loadFrames();
    }

    private void loadFrames() {
        try {
            currentAnim = selectedAnimField.get(editor);
            if (currentAnim != null) {
                java.util.List<?> frames = (java.util.List<?>) getFramesMethod.invoke(currentAnim);
                try {
                    java.io.File f = (java.io.File) editor.getClass().getField("file").get(editor);
                    String path = f != null ? f.getAbsolutePath() : "untitled";
                    String animName = currentAnim.toString();
                    for (int i = 0; i < frames.size(); i++) {
                        Object frame = frames.get(i);
                        if (!CPMAnimatorUtilsCore.frameColors.containsKey(frame)) {
                            Integer saved = com.tom.cpm.timeline.TimelineDataManager.getFrameColor(path, animName, i);
                            if (saved != null) CPMAnimatorUtilsCore.frameColors.put(frame, saved);
                        }
                    }
                } catch (Exception ignored) {}
            }
        } catch (Exception e) {}
    }

    @Override
    public void draw(MouseEvent event, float partialTicks) {
        if (!isAnimationTab() || !CPMAnimatorUtilsCore.timelineEnabled) return;
        
        super.draw(event, partialTicks);
        if (currentAnim == null) return;

        try {
            java.lang.reflect.Method getSelectedFrame = currentAnim.getClass().getMethod("getSelectedFrame");
            Object currentFrameObj = getSelectedFrame.invoke(currentAnim);
            if (currentFrameObj != null) {
                java.util.List<?> frames = (java.util.List<?>) getFramesMethod.invoke(currentAnim);
                this.selectedFrameIndex = frames.indexOf(currentFrameObj);
            }
        } catch (Exception ignored) {}

        Box b = getBounds();
        int timelineX = 10, timelineY = 14, timelineW = b.w - 20;
        hoveredFrame = -1;

        int duration = 1000;
        try { duration = currentAnim.getClass().getField("duration").getInt(currentAnim); } catch (Exception ignored) {}

        try {
            int intervalMs = 250;
            if (duration > 2000) intervalMs = 500;
            if (duration > 5000) intervalMs = 1000;
            for (int ms = 0; ms <= duration; ms += intervalMs) {
                float progress = (float) ms / duration;
                int x = timelineX + (int) (progress * timelineW);
                gui.drawBox(x, timelineY + 30, 1, 3, 0x22FFFFFF);
                gui.drawText(x - 10, timelineY + 42, (ms / 1000.0f) + "s", 0x55FFFFFF);
            }
        } catch (Exception ignored) {}

        drawPlayhead(timelineX, timelineY, timelineW, duration);

        // Keyframes
        try {
            java.util.List<?> frames = (java.util.List<?>) getFramesMethod.invoke(currentAnim);
            int count = frames.size();
            for (int i = 0; i < count; i++) {
                float progress = (float) i / (count > 0 ? (count == 1 ? 1 : count - 1) : 1);
                int x = timelineX + (int) (progress * timelineW);
                int y = timelineY + 15;
                if (event.x >= x - 8 && event.x <= x + 8 && event.y >= y - 10 && event.y <= y + 10) hoveredFrame = i;
                Integer customColor = CPMAnimatorUtilsCore.frameColors.get(frames.get(i));
                int color = (customColor != null) ? (customColor | 0xFF000000) : ((i == selectedFrameIndex) ? ACCENT_COLOR : 0xFFAAAAAA);
                
                // Draw diamond highlight
                if (hoveredFrame == i || (isDragging && i == dragStartIndex) || i == selectedFrameIndex) {
                    drawDiamond(gui, x, y, 9, 0x33FFFFFF);
                }
                
                // Draw the keyframe diamond
                drawDiamond(gui, x, y, 5, color);
            }
        } catch (Exception ignored) {}
    }

    private void drawDiamond(IGui gui, int x, int y, int size, int color) {
        for (int i = 0; i < size; i++) {
            int w = i <= size / 2 ? i * 2 + 1 : (size - i - 1) * 2 + 1;
            gui.drawBox(x - w / 2, y - size / 2 + i, w, 1, color);
        }
    }

    private void drawPlayhead(int x, int y, int w, int duration) {
        if (currentAnim == null || duration <= 0) return;
        try {
            Field playFullAnim = editor.getClass().getField("playFullAnim");
            boolean playing = (boolean) playFullAnim.get(editor);
            float progress;
            if (isScrubbing) {
                progress = scrubProgress;
            } else if (playing) {
                long playTime = com.tom.cpm.shared.MinecraftClientAccess.get().getPlayerRenderManager().getAnimationEngine().getTime();
                long startTime = editor.getClass().getField("playStartTime").getLong(editor);
                progress = (float) ((playTime - startTime) % duration) / duration;
            } else {
                java.util.List<?> frames = (java.util.List<?>) getFramesMethod.invoke(currentAnim);
                progress = (float) selectedFrameIndex / (frames.size() > 1 ? frames.size() - 1 : 1);
            }
            gui.drawBox(x + (int) (progress * w) - 1, y, 2, 30, 0xFFFF0000);
        } catch (Exception ignored) {}
    }

    @Override
    public void mouseClick(MouseEvent event) {
        if (!isAnimationTab() || !CPMAnimatorUtilsCore.timelineEnabled || currentAnim == null) return;
        
        Box b = getBounds();
        int timelineX = 10, timelineW = b.w - 20, timelineY = 14;

        if (event.y >= timelineY && event.y <= timelineY + 30 && hoveredFrame == -1) {
            isScrubbing = true;
            updateScrub(event.x, timelineX, timelineW);
            event.consume();
            return;
        }
        if (hoveredFrame != -1) {
            selectFrame(hoveredFrame);
            isDragging = true;
            dragStartIndex = hoveredFrame;
            event.consume();
            return;
        }
        super.mouseClick(event);
    }

    private void updateScrub(int mouseX, int startX, int width) {
        scrubProgress = Math.max(0, Math.min(1, (float) (mouseX - startX) / width));
        if (currentAnim == null) return;
        try {
            int duration = currentAnim.getClass().getField("duration").getInt(currentAnim);
            CPMAnimatorUtilsCore.scrubTime = (long) (scrubProgress * duration);
            currentAnim.getClass().getMethod("apply").invoke(currentAnim);
        } catch (Exception e) {}
    }

    @Override
    public void mouseDrag(MouseEvent event) {
        if (!isAnimationTab() || !CPMAnimatorUtilsCore.timelineEnabled) return;
        
        Box b = getBounds();
        int timelineX = 10, timelineW = b.w - 20;
        if (isScrubbing) {
            updateScrub(event.x, timelineX, timelineW);
            return;
        }
        if (isDragging && currentAnim != null && dragStartIndex != -1) {
            java.util.List<?> frames;
            try { frames = (java.util.List<?>) getFramesMethod.invoke(currentAnim); } catch (Exception e) { return; }
            float progress = Math.max(0, Math.min(1, (float) (event.x - timelineX) / timelineW));
            int targetIdx = Math.round(progress * (frames.size() - 1));
            if (targetIdx != dragStartIndex) {
                moveFrameTo(dragStartIndex, targetIdx);
                dragStartIndex = targetIdx;
                selectedFrameIndex = targetIdx;
            }
            return;
        }
        super.mouseDrag(event);
    }

    @Override
    public void mouseRelease(MouseEvent event) {
        if (!isAnimationTab() || !CPMAnimatorUtilsCore.timelineEnabled) {
            isScrubbing = false;
            isDragging = false;
            return;
        }
        
        if (isScrubbing) {
            isScrubbing = false;
            CPMAnimatorUtilsCore.scrubTime = -1;
            try { currentAnim.getClass().getMethod("apply").invoke(currentAnim); } catch (Exception ignored) {}
        }
        if (isDragging) {
            saveAllColorsToDisk();
            isDragging = false;
            dragStartIndex = -1;
        }
        super.mouseRelease(event);
    }

    private void moveFrameTo(int from, int to) {
        try {
            currentAnim.getClass().getMethod("moveFrame", int.class).invoke(currentAnim, to - from);
        } catch (Exception e) {
            try {
                @SuppressWarnings("unchecked")
                java.util.List<Object> fs = (java.util.List<Object>) getFramesMethod.invoke(currentAnim);
                fs.add(to, fs.remove(from));
            } catch (Exception ignored) {}
        }
    }

    private void saveAllColorsToDisk() {
        if (currentAnim == null) return;
        try {
            java.util.List<?> frames = (java.util.List<?>) getFramesMethod.invoke(currentAnim);
            java.io.File f = (java.io.File) editor.getClass().getField("file").get(editor);
            String path = f != null ? f.getAbsolutePath() : "untitled";
            String animName = currentAnim.toString();
            for (int i = 0; i < frames.size(); i++) {
                Integer col = CPMAnimatorUtilsCore.frameColors.get(frames.get(i));
                if (col != null) com.tom.cpm.timeline.TimelineDataManager.setFrameColor(path, animName, i, col);
            }
        } catch (Exception ignored) {}
    }

    private int getFrameCount(Object anim) {
        try { return ((java.util.List<?>) getFramesMethod.invoke(anim)).size(); } catch (Exception e) { return 0; }
    }

    public void selectFrame(int index) {
        this.selectedFrameIndex = index;
        if (currentAnim == null) return;
        try {
            Method setSelectedFrame = currentAnim.getClass().getMethod("setSelectedFrame", Class.forName("com.tom.cpm.shared.editor.anim.AnimFrame"));
            java.util.List<?> frames = (java.util.List<?>) getFramesMethod.invoke(currentAnim);
            if (index >= 0 && index < frames.size()) {
                setSelectedFrame.invoke(currentAnim, frames.get(index));
                editor.getClass().getMethod("updateGui").invoke(editor);
            }
        } catch (Exception ignored) {}
    }

    @Override
    public void recalculateLayout() { rebuildTimeline(); }
}