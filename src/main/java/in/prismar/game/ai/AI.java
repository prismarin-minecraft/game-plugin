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

    public void setSkin(String name, String signature, String value) {
        SkinTrait skinTrait = this.npc.getOrAddTrait(SkinTrait.class);
        if(skinTrait != null) {
            skinTrait.setSkinPersistent(name, signature, value);
        }
    }

    public void setSneaking(boolean sneaking) {
        SneakTrait sneakTrait = this.npc.getOrAddTrait(SneakTrait.class);
        sneakTrait.setSneaking(sneaking);
    }

    public void spawn() {
        this.npc.spawn(startLocation);
    }

    public void move(Player player) {
        this.npc.getNavigator().setTarget(player.getLocation());

    }

    public void delete() {
        CitizensAPI.getNPCRegistry().deregister(npc);
        game.getAiRegistry().unregister(this);
    }
}
