package tfar.worldprestige;

import com.google.common.collect.Lists;
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
            "minecraft:end/kill_dragon","minecraft:nether/summon_wither");


    @Override
    public List<String> getRequiredAdvancements() {
        return (List<String>) Server.required_advancements.get();
    }

    @Override
    public boolean useDefaultPrestige() {
        return Server.use_default_prestige.get();
    }

    public static class Server {
        public static ForgeConfigSpec.ConfigValue<List<? extends String>> required_advancements;
        public static ForgeConfigSpec.BooleanValue use_default_prestige;

        public Server(ForgeConfigSpec.Builder builder) {
            builder.push("general");
            required_advancements = builder
                    .comment("Required Advancements for final boss")
                    .defineList("required_advancements",() -> defaults, String.class::isInstance);

            use_default_prestige = builder.comment("Use the default method of prestiging a world, disable if making custom modpack")
                            .define("use_default_prestige",true);
            builder.pop();
        }
    }

    static void configLoad(ModConfigEvent e) {
    }
}
