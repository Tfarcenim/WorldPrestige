package tfar.worldprestige.datagen;

import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.function.Consumer;

public class WorldPrestigeAdvancements implements ForgeAdvancementProvider.AdvancementGenerator {

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
     /*   Advancement root = Advancement.Builder.advancement()
                .display(Items.NETHER_STAR, TextComponents.ROOT, TextComponents.ROOT_DESC,
                        new ResourceLocation("textures/gui/advancements/backgrounds/adventure.png"),
                        FrameType.TASK, true, true, false)
                .addCriterion("r", PlayerTrigger.TriggerInstance.)
                .save(saver, WorldPrestige.id("root").toString());*/

    }
}
