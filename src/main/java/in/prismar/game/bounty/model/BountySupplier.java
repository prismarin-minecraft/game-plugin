package in.prismar.game.bounty.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
@NoArgsConstructor
public class BountySupplier {

    private String uuid;
    private String name;
    private double money;

    public BountySupplier(Player player, double money) {
        this.uuid = player.getUniqueId().toString();
        this.name = player.getName();
        this.money = money;
    }
}
