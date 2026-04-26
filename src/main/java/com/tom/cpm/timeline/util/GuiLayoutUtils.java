package com.tom.cpm.timeline.util;

import java.util.ArrayList;
import java.util.List;

import com.tom.cpl.gui.elements.GuiElement;
import com.tom.cpl.math.Box;

/**
 * Utility for managing GUI layouts according to ServerFixes standards.
 */
public class GuiLayoutUtils {

    public static class VerticalLayoutBuilder {
        private final List<LayoutEntry> entries = new ArrayList<>();
        private int currentY;
        private int gap = 2;
        private final int startX;
        private final int width;

        public VerticalLayoutBuilder(int startX, int startY, int width) {
            this.startX = startX;
            this.currentY = startY;
            this.width = width;
        }

        public VerticalLayoutBuilder gap(int gap) {
            this.gap = gap;
            return this;
        }

        public VerticalLayoutBuilder add(GuiElement element) {
            return add(element, width);
        }

        public VerticalLayoutBuilder add(GuiElement element, int customWidth) {
            entries.add(new LayoutEntry(element, customWidth));
            return this;
        }

        public void apply() {
            int y = currentY;
            for (LayoutEntry entry : entries) {
                if (entry.element.isVisible()) {
                    entry.element.setBounds(new Box(startX, y, entry.width, entry.element.getBounds().h));
                    y += entry.element.getBounds().h + gap;
                }
            }
        }

        public int getNextY() {
            int y = currentY;
            for (LayoutEntry entry : entries) {
                if (entry.element.isVisible()) {
                    y += entry.element.getBounds().h + gap;
                }
            }
            return y;
        }

        private static class LayoutEntry {
            GuiElement element;
            int width;

            LayoutEntry(GuiElement element, int width) {
                this.element = element;
                this.width = width;
            }
        }
    }

    /**
     * Interface for elements that can reflow their contents.
     */
    public interface IReflowable {
        void recalculateLayout();
    }
}
