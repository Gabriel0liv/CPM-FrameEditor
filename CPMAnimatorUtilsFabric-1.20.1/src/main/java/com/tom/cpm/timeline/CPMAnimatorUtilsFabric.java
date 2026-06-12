package com.tom.cpm.timeline;

import net.fabricmc.api.ClientModInitializer;

public class CPMAnimatorUtilsFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CPMAnimatorUtilsCore.init("Fabric");
    }
}
