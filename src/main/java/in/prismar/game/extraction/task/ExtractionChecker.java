package in.prismar.game.extraction.task;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.warp.WarpProvider;
import in.prismar.game.airdrop.AirDrop;
import in.prismar.game.extraction.ExtractionFacade;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class ExtractionChecker implements Runnable {

    private final ExtractionFacade facade;

    private SimpleDateFormat dateFormat;

    private LocalTime openingTime;
    private LocalTime endingTime;

    public ExtractionChecker(ExtractionFacade facade) {
        this.facade = facade;
        this.openingTime = LocalTime.parse(facade.getMapFile().getEntity().getOpeningTime());
        this.endingTime = LocalTime.parse(facade.getMapFile().getEntity().getEndingTime());
        this.dateFormat = new SimpleDateFormat("HH:mm:ss");
    }

    @Override
    public void run() {
        final String current = dateFormat.format(new Date());
        if(!facade.isRunning()) {
            if(isOnTime(openingTime, current)) {
                facade.open();
            }
        } else {
            if(isOnTime(endingTime, current)) {
                facade.close();
            }
            for(String airDropTime : facade.getMapFile().getEntity().getAirdropTimes()) {
                LocalTime time = LocalTime.parse(airDropTime);
                if(isOnTime(time, current) && !isSpawned(time)) {
                    Location location = facade.getMapFile().findAirDropRandomLocation();
                    if(location != null){
                        Bukkit.getScheduler().runTask(facade.getGame(), () -> {
                            facade.getAirDropRegistry().callAirDrop(location);
                        });
                    }
                }
            }
        }
    }

    private boolean isSpawned(LocalTime time) {
        for(AirDrop drop : facade.getAirDropRegistry().getAirDrops()) {
            if(isOnTime(time, dateFormat.format(new Date(drop.getSpawned())))) {
                return true;
            }
        }
        return false;
    }

    private boolean isOnTime(LocalTime start, String check) {
        LocalTime target = LocalTime.parse(check);
        if(target.equals(start)) {
            return true;
        }
        if(target.isAfter(start) && target.isBefore(start.plusSeconds(10))) {
            return true;
        }
        return false;
    }
}