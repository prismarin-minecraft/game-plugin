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

    private transient MythicMob mythicMob;
    private transient Map<UUID, BossBar> bossBars;
    private transient Map<UUID, Map<UUID, BossDamager>> damagers;


}
