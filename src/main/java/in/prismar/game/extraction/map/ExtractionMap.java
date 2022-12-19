package in.prismar.game.extraction.map;

import lombok.Data;
import org.bukkit.Location;

import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
public class ExtractionMap {

    private String openingTime;
    private String endingTime;

    private List<String> airdropTimes;
    private List<Location> airdropLocations;
    private List<Location> spawns;
}
