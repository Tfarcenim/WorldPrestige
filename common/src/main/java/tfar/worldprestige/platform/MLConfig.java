package tfar.worldprestige.platform;

import net.minecraft.world.entity.EntityType;

import java.util.List;

public interface MLConfig {
    List<String> getRequiredAdvancements();
    String getBossEntityType();
}
