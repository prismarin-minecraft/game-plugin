package in.prismar.game.battleroyale.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public class BattleRoyaleParticipant {

    private Player player;

    @Setter
    private BattleRoyaleParticipantState state;

    private ItemStack[] savedInventoryContent;

    private ItemStack[] savedArmorContent;

    public BattleRoyaleParticipant(Player player) {
        this.player = player;
        this.state = BattleRoyaleParticipantState.ALIVE;
        this.savedInventoryContent = player.getInventory().getStorageContents();
        this.savedArmorContent = player.getInventory().getArmorContents();
    }

    public enum BattleRoyaleParticipantState {

        ALIVE, KNOCKED, DEAD

    }
}
