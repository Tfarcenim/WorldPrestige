package tfar.worldprestige.datagen;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import tfar.worldprestige.TextComponents;
import tfar.worldprestige.WorldPrestige;

import java.util.function.Consumer;

public class WorldPrestigeAdvancements implements ForgeAdvancementProvider.AdvancementGenerator {

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        Advancement root = Advancement.Builder.advancement()
                .display(Items.NETHER_STAR, TextComponents.ROOT, TextComponents.ROOT_DESC,
                        new ResourceLocation("textures/gui/advancements/backgrounds/adventure.png"),
                        FrameType.TASK, true, true, false)
                .addCriterion("required_advancement_0", PlayerTrigger.TriggerInstance.tick())
                .save(saver, WorldPrestige.id("root").toString());

    }
}
