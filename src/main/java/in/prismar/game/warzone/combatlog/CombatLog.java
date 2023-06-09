package in.prismar.game.warzone.combatlog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Setter
public class CombatLog {

    private Player player;
    private Player target;
    private long until;

    public CombatLog(Player player, Player target) {
        this.player = player;
        this.target = target;
    }
}
