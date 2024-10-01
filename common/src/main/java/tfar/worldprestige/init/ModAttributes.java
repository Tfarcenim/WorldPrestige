package tfar.worldprestige.init;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class ModAttributes {

    public static final Attribute DIG_SPEED = new RangedAttribute("attribute.name.worldprestige.dig_speed", 0, -65536, 65536).setSyncable(true);

}
