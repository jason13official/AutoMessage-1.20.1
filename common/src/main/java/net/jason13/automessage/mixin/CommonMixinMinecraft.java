package net.jason13.automessage.mixin;

import net.jason13.automessage.CommonConstants;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class CommonMixinMinecraft {
    
    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(CallbackInfo info) {
        
        CommonConstants.LOG.info("This line is printed by an example mod common mixin!");
        CommonConstants.LOG.info("MC Version: {}", Minecraft.getInstance().getVersionType());
    }
}