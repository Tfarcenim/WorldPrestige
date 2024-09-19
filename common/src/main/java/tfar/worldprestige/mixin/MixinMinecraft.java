package tfar.worldprestige.mixin;

import tfar.worldprestige.WorldPrestige;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    
    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(CallbackInfo info) {
        
        WorldPrestige.LOG.info("This line is printed by an example mod common mixin!");
        WorldPrestige.LOG.info("MC Version: {}", Minecraft.getInstance().getVersionType());
    }
}