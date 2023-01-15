package in.prismar.game.hardpoint.config;

import in.prismar.game.hardpoint.HardpointTeam;
import lombok.Data;
import org.bukkit.Location;

import java.util.List;
import java.util.Map;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
public class HardpointConfig {

    private List<Hardpoint> points;
    private Map<HardpointTeam, List<Location>> spawns;

    private int maxSpaceBetweenTeams;

    public List<Location> getSpawns(HardpointTeam team) {
        return spawns.get(team);
    }

}
