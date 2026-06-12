package com.tom.cpm.timeline;

import java.util.Map;
import java.util.WeakHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CPMAnimatorUtilsCore {
    public static final String MOD_ID = "cpm_animator_utils";
    public static final String MOD_NAME = "CPM Animator Utils";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static boolean timelineEnabled = true;
    public static final Map<Object, Integer> frameColors = new WeakHashMap<>();
    public static long scrubTime = -1;

    private CPMAnimatorUtilsCore() {
    }

    public static void init(String loader) {
        LOGGER.info("========================================");
        LOGGER.info("{} loaded on {}!", MOD_NAME, loader);
        LOGGER.info("========================================");
    }
}
