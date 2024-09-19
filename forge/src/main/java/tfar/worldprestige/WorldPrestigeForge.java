package tfar.worldprestige;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tfar.worldprestige.datagen.ModDatagen;

@Mod(WorldPrestige.MOD_ID)
public class WorldPrestigeForge {
    
    public WorldPrestigeForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.

        bus.addListener(ModDatagen::gather);
    
        // Use Forge to bootstrap the Common mod.
        WorldPrestige.init();
        
    }
}