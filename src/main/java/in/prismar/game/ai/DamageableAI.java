package in.prismar.game.ai;

import in.prismar.game.Game;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;

public abstract class DamageableAI extends AI {
    public DamageableAI(Game game, String name, Location startLocation) {
        super(game, name, startLocation);
    }

    public void damage(double damage, LivingEntity damager) {
        LivingEntity entity = asLivingEntity();
        entity.damage(damage, damager);
    }

    public void setMaxHealth(double max) {
        LivingEntity livingEntity = asLivingEntity();
        AttributeInstance attribute = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attribute != null) {
            attribute.setBaseValue(max);
        }
    }

    public LivingEntity asLivingEntity() {
        return (LivingEntity) npc.getEntity();
    }
}
