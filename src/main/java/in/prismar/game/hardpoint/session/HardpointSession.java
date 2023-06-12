package in.prismar.game.hardpoint.session;

import in.prismar.game.hardpoint.HardpointTeam;
import in.prismar.game.hardpoint.config.Hardpoint;
import in.prismar.library.common.random.UniqueRandomizer;
import in.prismar.library.spigot.hologram.Hologram;
import lombok.Data;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
public class HardpointSession {

    private Map<HardpointTeam, Map<UUID, HardpointSessionPlayer>> players;
    private Map<HardpointTeam, Long> teamPoints;

    private Hardpoint point;
    private long pointUntil;

    private long moveUntil;

    private HardpointSessionState pointState;

    private HardpointTeam capturedTeam;

    public HardpointSession() {
        this.players = new HashMap<>();
        this.teamPoints = new HashMap<>();
        for(HardpointTeam team : HardpointTeam.values()) {
            teamPoints.put(team, 0L);
            players.put(team, new HashMap<>());
        }
    }

    public boolean isAllowedToMove() {
        return System.currentTimeMillis() >= moveUntil;
    }

}
