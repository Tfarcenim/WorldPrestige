package tfar.worldprestige.mixin;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.WorldData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tfar.worldprestige.WorldPrestige;

import java.io.File;

@Mixin(LevelStorageSource.LevelStorageAccess.class)
public class LevelStorageSourceMixin {

    @Inject(method = "saveDataTag(Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/world/level/storage/WorldData;Lnet/minecraft/nbt/CompoundTag;)V",at = @At(value = "INVOKE",
            target = "Lnet/minecraft/nbt/CompoundTag;put(Ljava/lang/String;Lnet/minecraft/nbt/Tag;)Lnet/minecraft/nbt/Tag;")
    ,locals = LocalCapture.CAPTURE_FAILHARD)
    private void modifyData(RegistryAccess $$0, WorldData $$1, CompoundTag $$2, CallbackInfo ci, File $$3, CompoundTag $$4, CompoundTag $$5){
        WorldPrestige.modifyData($$4);
    }
}
