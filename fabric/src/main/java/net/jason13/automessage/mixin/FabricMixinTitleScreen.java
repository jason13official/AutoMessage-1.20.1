package net.jason13.automessage.mixin;

import net.jason13.automessage.CommonConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class FabricMixinTitleScreen {
    
    @Inject(at = @At("HEAD"), method = "init()V")
    private void init(CallbackInfo info) {
        
        CommonConstants.LOG.info("This line is printed by an example mod mixin from Fabric!");
        CommonConstants.LOG.info("MC Version: {}", Minecraft.getInstance().getVersionType());
    }
}