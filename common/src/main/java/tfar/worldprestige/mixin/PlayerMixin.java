package tfar.worldprestige.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.worldprestige.init.ModAttributes;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "createAttributes",at = @At("RETURN"))
    private static void addAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.getReturnValue().add(ModAttributes.DIG_SPEED);
    }

    @Inject(method = "addAdditionalSaveData",at = @At("HEAD"))
    private void addExtra(CompoundTag tag, CallbackInfo ci) {
   //     tag.putInt("worldprestige:mining",miningSpeedBoost);
    }

    @Inject(method = "readAdditionalSaveData",at = @At("HEAD"))
    private void readExtra(CompoundTag tag, CallbackInfo ci) {
     //   miningSpeedBoost = tag.getInt("worldprestige:mining");
    }

}
