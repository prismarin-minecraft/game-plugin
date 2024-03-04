package in.prismar.game.warzone.combatlog.npc;

import in.prismar.library.spigot.raytrace.hitbox.RaytraceHitbox;
import in.prismar.library.spigot.raytrace.hitbox.RaytraceHitboxFace;
import in.prismar.library.spigot.raytrace.result.RaytraceHit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class TemporaryNpcHitbox implements RaytraceHitbox {

    private TemporaryNpc npc;
    private final Vector minVector;
    private final Vector maxVector;

    public TemporaryNpcHitbox(TemporaryNpc npc) {
        this.npc = npc;
        this.minVector = npc.getLocation().toVector().subtract(new Vector(0.3f, 0f, 0.3f));
        this.maxVector = npc.getLocation().toVector().add(new Vector(0.3f, 1.8f, 0.3f));
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
        return new RaytraceNpcHit(npc, point);
    }

    public class RaytraceNpcHit extends RaytraceHit<TemporaryNpc> {

        public RaytraceNpcHit(TemporaryNpc target, Location point) {
            super(target, point);
        }
    }
}


