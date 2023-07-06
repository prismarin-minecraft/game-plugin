package in.prismar.game.warzone.boss;

import in.prismar.api.clan.ClanBuff;
import io.lumine.mythic.api.mobs.MythicMob;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.boss.BossBar;

import java.util.Map;
import java.util.UUID;


/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Setter
public class Boss {

    private String id;
    private ClanBuff buff;

    private int lives;
    private int maxLives;

    private Map<String, MythicMob> mythicMobs;

    private Map<UUID, BossBar> bossBars;
    private Map<UUID, BossDamager> damagers;


}
