package in.prismar.game.missions;

import in.prismar.api.PrismarinApi;
import in.prismar.api.mission.Mission;
import in.prismar.api.mission.MissionProvider;
import in.prismar.api.mission.MissionType;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.missions.impl.*;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.SafeInitialize;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@Service
@Getter
public class MissionWrapper {

    private static MissionWrapper instance;

    private final MissionProvider<Mission> missionProvider;

    @Inject
    private CustomItemRegistry customItemRegistry;

    public MissionWrapper() {
        missionProvider = PrismarinApi.getProvider(MissionProvider.class);
        instance = this;
    }

    @SafeInitialize
    private void initialize() {
        missionProvider.register(new Kill25ZombiesMission());
        missionProvider.register(new Kill100ZombiesMission(customItemRegistry));
        missionProvider.register(new KillAnderson10Mission(customItemRegistry));
        missionProvider.register(new KillZaku10Mission(customItemRegistry));
        missionProvider.register(new CreateOrJoinPartyMission());
        missionProvider.register(new KillKindletron2Mission(customItemRegistry));
        missionProvider.register(new ActivateQuarryMission());


        missionProvider.register(new MoneyBasedMission("kill_5_players_in_ffa", MissionType.DAILY, Material.WOODEN_HOE,
                "§3FFA player", "§cKill 5 players in ffa", 5, 3000));
        missionProvider.register(new MoneyBasedMission("kill_5_players_in_hardpoint", MissionType.DAILY, Material.WOODEN_AXE,
                "§3Hardpoint player", "§cKill 5 players in hardpoint", 5, 3000));

    }

    public static void progress(Player player, String id, int stage, long progress) {
        instance.getMissionProvider().addProgress(player, id, stage, progress);
    }


}
