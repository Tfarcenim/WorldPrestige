package tfar.worldprestige.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import tfar.worldprestige.datagen.data.ModRecipeProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModDatagen {

    public static void gather(GatherDataEvent event) {
        boolean client = event.includeClient();
        DataGenerator dataGenerator = event.getGenerator();
        PackOutput packOutput = dataGenerator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        dataGenerator.addProvider(client,new ModLangProvider(packOutput));
        dataGenerator.addProvider(client,new ModBlockstateProvider(packOutput,existingFileHelper));
        if (event.includeServer()) {
            dataGenerator.addProvider(true, new ForgeAdvancementProvider(packOutput,lookupProvider,existingFileHelper, List.of(new WorldPrestigeAdvancements())));
        }
    }
}
