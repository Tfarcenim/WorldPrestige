package tfar.worldprestige.mixin;

import tfar.worldprestige.WorldPrestige;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class MixinTitleScreen {

    @Inject(at = @At("HEAD"), method = "init()V")
    private void init(CallbackInfo info) {

        WorldPrestige.LOG.info("This line is printed by an example mod mixin from Forge!");
        WorldPrestige.LOG.info("MC Version: {}", Minecraft.getInstance().getVersionType());
    }
}