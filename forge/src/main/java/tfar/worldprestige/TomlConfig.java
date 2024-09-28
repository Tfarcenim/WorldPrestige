package tfar.worldprestige;

import com.google.common.collect.Lists;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;
import tfar.worldprestige.platform.MLConfig;

import java.util.List;

public class TomlConfig implements MLConfig {


    public static final Server SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;

    static {
        final Pair<Server, ForgeConfigSpec> specPair2 = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER_SPEC = specPair2.getRight();
        SERVER = specPair2.getLeft();
    }
    public static final List<String> defaults = Lists.newArrayList(
            "minecraft:end/kill_dragon");


    @Override
    public List<String> getRequiredAdvancements() {
        return (List<String>) Server.required_advancements.get();
    }

    @Override
    public String getBossEntityType() {
        return Server.boss_entity_type.get();
    }

    public static class Server {
        public static ForgeConfigSpec.ConfigValue<List<? extends String>> required_advancements;
        public static ForgeConfigSpec.ConfigValue<String> boss_entity_type;


        public Server(ForgeConfigSpec.Builder builder) {
            builder.push("general");
            required_advancements = builder
                    .comment("Required Advancements for final boss")
                    .defineList("required_advancements",() -> defaults, String.class::isInstance);

            boss_entity_type = builder.comment("Entity Type of boss")
                            .define("boss_entity_type",() -> BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.EVOKER).toString(),String.class::isInstance);
            builder.pop();
        }
    }

    static void configLoad(ModConfigEvent e) {
    }
}
