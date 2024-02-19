package in.prismar.game.missions;

import in.prismar.api.PrismarinApi;
import in.prismar.api.mission.Mission;
import in.prismar.api.mission.MissionProvider;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.SafeInitialize;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;

@Service
@Getter
public class MissionWrapper {

    private final MissionProvider<Mission> missionProvider;

    @Inject
    private CustomItemRegistry customItemRegistry;

    public MissionWrapper() {
        missionProvider = PrismarinApi.getProvider(MissionProvider.class);
    }

    @SafeInitialize
    private void initialize() {
        missionProvider.register(new Kill25ZombiesMission());
        missionProvider.register(new KillAnderson10Mission(customItemRegistry));
        missionProvider.register(new KillZaku10Mission(customItemRegistry));
    }


}
