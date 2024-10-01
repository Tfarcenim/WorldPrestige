package tfar.worldprestige.world;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import tfar.worldprestige.init.ModAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PrestigePowers {

    public static Map<String,PrestigePower> powers = new HashMap<>();

    public static final PrestigePower ARMOR = register(new AttributeModifierPower("armor",
            Attributes.ARMOR, UUID.fromString("feb1e505-dccb-4181-9bc5-eafbb7ed90f1"),2, AttributeModifier.Operation.ADDITION));

    public static final PrestigePower SPEED = register(new AttributeModifierPower("speed",
            Attributes.MOVEMENT_SPEED, UUID.fromString("feb1e505-dccb-4181-9bc5-eafbb7ed90f1"),.1, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final PrestigePower DIG_SPEED = register(new AttributeModifierPower("dig_speed",
            ModAttributes.DIG_SPEED, UUID.fromString("feb1e505-dccb-4181-9bc5-eafbb7ed90f1"),1, AttributeModifier.Operation.ADDITION));

    public static final PrestigePower HEALTH = register(new AttributeModifierPower("health",
            Attributes.MAX_HEALTH, UUID.fromString("feb1e505-dccb-4181-9bc5-eafbb7ed90f1"),2, AttributeModifier.Operation.ADDITION));

    public static PrestigePower register(PrestigePower power) {
        powers.put(power.getId(),power);
        return power;
    }
}
