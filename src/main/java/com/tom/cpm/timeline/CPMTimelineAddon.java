package com.tom.cpm.timeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraftforge.fml.common.Mod;

@Mod("cpm_timeline")
public class CPMTimelineAddon {
    public static final Logger LOGGER = LoggerFactory.getLogger("CPM Timeline");

    public static boolean timelineEnabled = true;
    public static final java.util.Map<Object, Integer> frameColors = new java.util.WeakHashMap<>();
    public static long scrubTime = -1;

    public CPMTimelineAddon() {
        LOGGER.info("========================================");
        LOGGER.info("CPM Timeline Addon loaded successfully!");
        LOGGER.info("========================================");
    }
}
