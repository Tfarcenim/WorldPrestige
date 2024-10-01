package tfar.worldprestige.world;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class AttributeModifierPower extends PrestigePower {
    private final Attribute attribute;
    private final UUID uuid;
    private final double scale;
    private final AttributeModifier.Operation operation;

    public AttributeModifierPower(String id, Attribute attribute, UUID uuid, double scale, AttributeModifier.Operation operation) {
        super(id);
        this.attribute = attribute;
        this.uuid = uuid;
        this.scale = scale;
        this.operation = operation;
    }

    @Override
    public void apply(Player player, int level) {
        AttributeInstance attributeInstance = player.getAttribute(attribute);
        if (attributeInstance != null && attributeInstance.getModifier(uuid) == null) {
            attributeInstance.addPermanentModifier(new AttributeModifier(uuid, "Prestige Power", level * scale, operation));
        }
    }

    @Override
    public void remove(Player player) {
        AttributeInstance attributeInstance = player.getAttribute(attribute);
        if (attributeInstance != null) {
            attributeInstance.removeModifier(uuid);
        }
    }
}
