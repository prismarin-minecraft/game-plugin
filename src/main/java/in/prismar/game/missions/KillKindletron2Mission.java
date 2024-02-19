package in.prismar.game.missions;

import in.prismar.api.mission.AbstractMission;
import in.prismar.api.mission.MissionType;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.library.spigot.item.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class KillKindletron2Mission extends AbstractMission {

    private final CustomItemRegistry customItemRegistry;

    public KillKindletron2Mission(CustomItemRegistry customItemRegistry) {
        super("killkindletron2", MissionType.WEEKLY, 1, Material.PISTON, "§eMechanical killer");
        this.customItemRegistry = customItemRegistry;
    }

    @Override
    public String getDescription(int stage) {
        return "§cKill Kindletron2 1x";
    }

    @Override
    public String[] getRewards() {
        return new String[]{
                "§6Medium Backpack",
        };
    }

    @Override
    public void onReceiveReward(Player player) {
        ItemUtil.giveItem(player, customItemRegistry.getItemById("MediumBackpack").build());


    }

    @Override
    public long getMaxProgress(int stage) {
        return 10;
    }
}
