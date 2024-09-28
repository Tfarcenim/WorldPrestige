package tfar.worldprestige.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Unique;
import tfar.worldprestige.MobDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.worldprestige.WorldPrestige;

@Mixin(Mob.class)
public class MobMixin implements MobDuck {

    @Unique boolean triggersPrestige;
    
    @Inject(at = @At("RETURN"), method = "readAdditionalSaveData")
    private void loadExtra(CompoundTag $$0, CallbackInfo ci) {
        triggersPrestige = $$0.getBoolean(WorldPrestige.TRIGGERS_PRESTIGE);
    }

    @Inject(at = @At("RETURN"), method = "addAdditionalSaveData")
    private void saveExtra(CompoundTag $$0, CallbackInfo ci) {
        $$0.putBoolean(WorldPrestige.TRIGGERS_PRESTIGE,triggersPrestige);
    }

    @Override
    public boolean triggersPrestige() {
        return triggersPrestige;
    }

    @Override
    public void setTriggersPrestige(boolean triggersPrestige) {
        this.triggersPrestige = triggersPrestige;
    }
}