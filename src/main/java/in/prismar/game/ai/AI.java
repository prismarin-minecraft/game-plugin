package in.prismar.game.ai;

import in.prismar.game.Game;
import lombok.Getter;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.trait.SkinTrait;
import net.citizensnpcs.trait.SneakTrait;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class AI {

    private final Game game;
    protected final Location startLocation;
    protected net.citizensnpcs.api.npc.NPC npc;

    /**
     * Create a new AI
     * @param game The plugin class
     * @param name The color name of the AI
     * @param startLocation The location to spawn the AI
     */
    public AI(Game game, String name,  Location startLocation) {
        this.game = game;
        this.startLocation = startLocation;
        this.npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, UUID.randomUUID().toString());
        this.npc.setName(name);

        game.getAiRegistry().register(this);
    }
    public Equipment getEquipment() {
        return this.npc.getOrAddTrait(Equipment.class);
    }

    /**
     * Set the skin of the AI
     * @param name The name of the skin
     * @param signature The signature of the skin
     * @param value The value of the skin
     */
    public void setSkin(String name, String signature, String value) {
        SkinTrait skinTrait = this.npc.getOrAddTrait(SkinTrait.class);
        if(skinTrait != null) {
            skinTrait.setSkinPersistent(name, signature, value);
        }
    }

    /**
     * Set the sneaking status of the AI
     * @param sneaking The sneaking status of the AI
     */
    public void setSneaking(boolean sneaking) {
        SneakTrait sneakTrait = this.npc.getOrAddTrait(SneakTrait.class);
        sneakTrait.setSneaking(sneaking);
    }

    // Spawn the AI
    public void spawn() {
        this.npc.spawn(startLocation);
    }

    // Despawn the AI and unregister it
    public void delete() {
        CitizensAPI.getNPCRegistry().deregister(npc);
        game.getAiRegistry().unregister(this);
    }
}
