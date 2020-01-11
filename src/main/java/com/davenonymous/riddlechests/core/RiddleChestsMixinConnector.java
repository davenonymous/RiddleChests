package com.davenonymous.riddlechests.core;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class RiddleChestsMixinConnector implements IMixinConnector {
    @Override
    public void connect() {
        MixinBootstrap.init();
        Mixins.addConfiguration("assets/riddlechests/mixins.riddlechests.json");
    }
}
