package tfar.worldprestige.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import tfar.worldprestige.WorldPrestige;

public class ModBlockstateProvider extends BlockStateProvider {
    public ModBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, WorldPrestige.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

    }
}
