package in.prismar.game.party;

import lombok.Data;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
public class Party {

    private Player owner;
    private List<Player> members;
    private List<Player> invites;

    public Party(Player owner) {
        this.owner = owner;
        this.members = new ArrayList<>();
        this.invites = new ArrayList<>();
    }
}
