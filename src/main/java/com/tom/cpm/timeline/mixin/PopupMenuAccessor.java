package com.tom.cpm.timeline.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.tom.cpl.gui.elements.PopupMenu;

@Mixin(value = PopupMenu.class, remap = false)
public interface PopupMenuAccessor {
    @Accessor("x")
    int getX();

    @Accessor("y")
    int getY();
}
