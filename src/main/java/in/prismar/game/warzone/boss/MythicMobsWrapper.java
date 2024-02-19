package in.prismar.game.warzone.boss;

import in.prismar.api.clan.ClanBuff;
import io.lumine.mythic.bukkit.MythicBukkit;

import java.util.HashMap;

public final class MythicMobsWrapper {

    public static Boss createBoss(String id, ClanBuff buff, String... mobs) {
        Boss boss = new Boss();
        boss.setId(id);
        boss.setBuff(buff);
        boss.setMaxLives(1);
        boss.setLives(1);
        boss.setBossBars(new HashMap<>());
        boss.setDamagers(new HashMap<>());
        boss.setMythicMobs(new HashMap<>());
        if(mobs.length == 0) {
            if(MythicBukkit.inst().getMobManager().getMythicMob(id).isPresent()) {
                boss.getMythicMobs().put(id.toLowerCase(), MythicBukkit.inst().getMobManager().getMythicMob(id).get());
            }

        } else {
            for(String mob : mobs) {
                if(MythicBukkit.inst().getMobManager().getMythicMob(mob).isPresent()) {
                    boss.getMythicMobs().put(mob.toLowerCase(), MythicBukkit.inst().getMobManager().getMythicMob(mob).get());
                }

            }
        }
        return boss;
    }

}
