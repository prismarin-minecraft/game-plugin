package in.prismar.game.warzone.listener;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.battlepass.BattlePassProvider;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.Game;
import in.prismar.game.missions.MissionWrapper;
import in.prismar.game.warzone.WarzoneService;
import in.prismar.game.warzone.boss.Boss;
import in.prismar.game.warzone.boss.BossService;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.common.math.NumberFormatter;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class MobDeathListener implements Listener {

    @Inject
    private WarzoneService warzoneService;

    @Inject
    private MissionWrapper missionWrapper;

    private final ConfigStore configStore;
    private final UserProvider<User> userProvider;
    private BattlePassProvider battlePassProvider;


    public MobDeathListener() {
        this.configStore = PrismarinApi.getProvider(ConfigStore.class);
        this.userProvider = PrismarinApi.getProvider(UserProvider.class);
    }

    @EventHandler
    public void onCall(MythicMobDeathEvent event) {
        if(event.getKiller() instanceof Player player) {
            final String name = event.getMob().getType().getInternalName();
            final String moneyRaw = configStore.getProperty("warzone.mobs." + name.toLowerCase() + ".reward");
            final String battlePassRaw = configStore.getProperty("warzone.mobs." + name.toLowerCase() + ".battlepass");
            if(moneyRaw != null) {
                final String[] split = moneyRaw.split("-");
                final String[] splitBattlePass = battlePassRaw.split("-");
                double money = MathUtil.randomDouble(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
                int battlepass = MathUtil.random(Integer.parseInt(splitBattlePass[0]), Integer.parseInt(splitBattlePass[1]));
                User user = userProvider.getUserByUUID(player.getUniqueId());
                user.getSeasonData().setBalance(user.getSeasonData().getBalance() + money);
                getBattlePassProvider().addExp(user, battlepass);
                player.sendMessage(PrismarinConstants.PREFIX + "§7You received §6" + NumberFormatter.formatDoubleToThousands(money) + "$ §7for killing a §c" + event.getMob().getDisplayName());
                player.sendMessage(PrismarinConstants.PREFIX + "§7You received §a" + NumberFormatter.formatDoubleToThousands(money) + " Battlepass EXP §7for killing a §c" + event.getMob().getDisplayName());
                userProvider.saveAsync(user, true);
                warzoneService.getClanStatsProvider().addMobKills(player);

                missionWrapper.getMissionProvider().addProgress(player, "kill25zombies", 1, 1);
            }
        }
    }

    public BattlePassProvider getBattlePassProvider() {
        if(battlePassProvider == null) {
            battlePassProvider = PrismarinApi.getProvider(BattlePassProvider.class);
        }
        return battlePassProvider;
    }
}
