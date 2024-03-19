package in.prismar.game.ai.hitbox;

import in.prismar.game.ai.DamageableAI;
import in.prismar.library.spigot.raytrace.hitbox.RaytraceHitbox;
import in.prismar.library.spigot.raytrace.hitbox.RaytraceHitboxFace;
import in.prismar.library.spigot.raytrace.result.RaytraceHit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class AIHitbox implements RaytraceHitbox {

    private DamageableAI ai;
    private final Vector minVector;
    private final Vector maxVector;

    public AIHitbox(DamageableAI ai) {
        this.ai = ai;
        this.minVector = ai.getNpc().getEntity().getLocation().toVector().subtract(new Vector(0.3f, 0f, 0.3f));
        this.maxVector = ai.getNpc().getEntity().getLocation().toVector().add(new Vector(0.3f, 1.8f, 0.3f));
    }

    @Override
    public Vector getMaxVector() {
        return maxVector.clone();
    }

    @Override
    public Vector getMinVector() {
        return minVector.clone();
    }

    @Override
    public RaytraceHitboxFace[] getFaces() {
        return facesFromDefaultBox(minVector, maxVector);
    }

    @Override
    public RaytraceHit asHit(Location point) {
        return new RaytraceAIHit(ai, point);
    }

    public class RaytraceAIHit extends RaytraceHit<DamageableAI> {

        public RaytraceAIHit(DamageableAI target, Location point) {
            super(target, point);
        }
    }
}


